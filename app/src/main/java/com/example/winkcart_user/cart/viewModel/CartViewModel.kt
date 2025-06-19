package com.example.winkcart_user.cart.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.coupons.discount.DiscountCodesResponse
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRule
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.utils.CurrencyConversion.convertCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repo: ProductRepo ) :ViewModel() {

    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    private val _currencyRate = MutableStateFlow("")
    val currencyRate = _currencyRate.asStateFlow()

    private val _customerID = MutableStateFlow("")
    val customerID = _customerID.asStateFlow()

    private val _createDraftOrderResponse = MutableStateFlow<ResponseStatus<Any>>(ResponseStatus.Loading)
    val createDraftOrderResponse = _createDraftOrderResponse.asStateFlow()

    private val _draftOrders = MutableStateFlow<ResponseStatus<DraftOrderResponse>>(ResponseStatus.Loading)
    val draftOrders = _draftOrders.asStateFlow()

    private val _deleteDraftOrders = MutableStateFlow<ResponseStatus<Unit>>(ResponseStatus.Loading)
    val deleteDraftOrders = _deleteDraftOrders.asStateFlow()

    private val _updateDraftOrder = MutableStateFlow<ResponseStatus<DraftOrderResponse>>(ResponseStatus.Loading)
    val updateDraftOrder = _updateDraftOrder.asStateFlow()

    private val _totalAmount = MutableStateFlow("0.00 EGP")
    val totalAmount = _totalAmount.asStateFlow()

    private val _priceRules = MutableStateFlow<ResponseStatus<PriceRulesResponse>>(ResponseStatus.Loading)
    val priceRules = _priceRules.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<PriceRule?>(null)
    val appliedCoupon = _appliedCoupon.asStateFlow()

    private val _discountAmount = MutableStateFlow("0.00 EGP")
    val discountAmount = _discountAmount.asStateFlow()



    private val _discountCodesByPriceRule = MutableStateFlow<Map<Long, ResponseStatus<DiscountCodesResponse>>>(emptyMap())
    val discountCodesByPriceRule: StateFlow<Map<Long, ResponseStatus<DiscountCodesResponse>>> = _discountCodesByPriceRule




    fun setAppliedCoupon(coupon: PriceRule) {
        if (_appliedCoupon.value?.id != coupon.id) {
            _appliedCoupon.value = coupon
            calculateTotalAmount()
        }
    }


    fun clearAppliedCoupon() {
        _appliedCoupon.value = null
        calculateTotalAmount()
    }

    init {
        readCurrencyCode()
        readCurrencyRate()
        viewModelScope.launch {
            repo.readCustomerID().collect { id ->
                _customerID.value = id
                getDraftOrders(id)
            }
        }
    }


    private fun calculateTotalAmount() {
        val orders = when (val currentOrders = draftOrders.value) {
            is ResponseStatus.Success -> currentOrders.result.draft_orders
            else -> emptyList()
        }

        val rate = currencyRate.value
        val code = currencyCode.value

        var total = orders.sumOf { draftOrder ->
            draftOrder.line_items.sumOf { lineItem ->
                val price = lineItem?.price?.toDoubleOrNull() ?: 0.0
                val quantity = lineItem?.quantity ?: 0
                price * quantity
            }
        }

        var discount = 0.0

        _appliedCoupon.value?.let { coupon ->
            if (coupon.target_selection == "all" && coupon.allocation_method == "across") {
                // Apply to total
                if (coupon.value_type == "percentage") {
                    discount = total * (coupon.value.replace("-", "").toDouble() / 100.0)
                } else if (coupon.value_type == "fixed_amount") {
                    discount = coupon.value.replace("-", "").toDouble()
                }
            } else if (coupon.target_selection == "entitled" && coupon.allocation_method == "each") {
                val entitledIds = coupon.entitled_product_ids ?: emptyList()
                discount = orders.sumOf { draftOrder ->
                    draftOrder.line_items.sumOf { item ->
                        val productId = item?.product_id
                        if (productId in entitledIds) {
                            val price = item?.price?.toDoubleOrNull() ?: 0.0
                            val quantity = item?.quantity ?: 0
                            if (coupon.value_type == "percentage") {
                                price * quantity * (coupon.value.replace("-", "").toDouble() / 100.0)
                            } else if (coupon.value_type == "fixed_amount") {
                                coupon.value.replace("-", "").toDouble() * quantity
                            } else 0.0
                        } else 0.0
                    }
                }
            }
        }

        total -= discount

        if (rate.isBlank() || code.isBlank()) {
            _totalAmount.value = "0.00"
            _discountAmount.value = "0.00"
            return
        }

        val convertedTotal = convertCurrency(
            amount = total.toString(),
            rate = rate,
            currencyCode = code
        )
        val convertedDiscount = convertCurrency(
            amount = discount.toString(),
            rate = rate,
            currencyCode = code
        )

        _totalAmount.value = "$convertedTotal $code"
        _discountAmount.value = "$convertedDiscount $code"

        Log.i("TAG", "CheckoutScreen: calculateTotalAmount: ${_discountAmount.value}")
    }


    fun refreshTotalAmount() {
        calculateTotalAmount()
    }


    fun createDraftCartOrder(customerId: String, draftOrderRequest: DraftOrderRequest) {
        viewModelScope.launch {
            val existingOrders = repo.getAllDraftOrders()
                .firstOrNull()

            if (existingOrders != null) {
                val isAlreadyExist = existingOrders.draft_orders.any { existingOrder ->
                    existingOrder.customer?.id == customerId.toLong() &&
                            existingOrder.line_items.map { it?.title }.toSet() == draftOrderRequest.draft_order.line_items.map { it?.title }.toSet() &&
                            existingOrder.line_items.any { lineItem ->
                                lineItem?.properties?.any { prop ->
                                    prop.name == "SavedAt" && prop.value == "Cart"
                                } == true
                            }
                }
                if (isAlreadyExist) {
                    _createDraftOrderResponse.value = ResponseStatus.Error(Exception("Draft order already exists"))
                    return@launch
                }
            }

            repo.createDraftOrder(draftOrderRequest)
                .catch {
                    _createDraftOrderResponse.value = ResponseStatus.Error(it)
                }
                .collect { response ->
                    if (response != null) {
                        _createDraftOrderResponse.value = ResponseStatus.Success(response)
                    } else {
                        _createDraftOrderResponse.value = ResponseStatus.Error(
                            NullPointerException("createDraftOrderResponse is null")
                        )
                    }
                }
        }
    }



    fun updateDraftOrder(draftOrderId: Long,draftOrderRequest: DraftOrderRequest) {
        viewModelScope.launch {
            val result = repo.updateDraftOrder(draftOrderId= draftOrderId, draftOrderRequest= draftOrderRequest)
            result.catch {
                _updateDraftOrder.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _updateDraftOrder.value= ResponseStatus.Success<DraftOrderResponse>(it)
                    getDraftOrders(customerId = customerID.value)
                    refreshTotalAmount()
                }else{
                    _updateDraftOrder.value = ResponseStatus.Error(NullPointerException("updateDraftOrder is null"))
                }
            }

        }
    }


    fun getDraftOrders(customerId: String) {
        viewModelScope.launch {
            try {
                val storedCustomerIdStr = repo.readCustomersID()
                val storedCustomerId = storedCustomerIdStr?.toLongOrNull()

                if (storedCustomerId == null) {
                    _draftOrders.value = ResponseStatus.Error(
                        IllegalStateException("Invalid customer ID: $storedCustomerIdStr")
                    )
                    return@launch //stop running
                }

                repo.getAllDraftOrders()
                    .catch { exception ->
                        _draftOrders.value = ResponseStatus.Error(exception)
                    }
                    .collect { response ->
                        if (response != null) {
                            val matchingDraftOrders = response.draft_orders
                                .filter { it.customer.id == storedCustomerId }
                                .filter { draftOrder ->
                                    draftOrder.line_items.any { lineItem ->
                                        lineItem!!.properties.any { prop ->
                                            prop.name == "SavedAt" && prop.value == "Cart"
                                        }
                                    }
                                }
                            _draftOrders.value = ResponseStatus.Success(
                                response.copy(draft_orders = matchingDraftOrders)
                            )
                        } else {
                            _draftOrders.value = ResponseStatus.Error(
                                NullPointerException("Draft Orders is null")
                            )
                        }
                    }
            } catch (e: Exception) {
                _draftOrders.value = ResponseStatus.Error(e)
            }
        }
    }


    fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repo.deleteDraftOrder(draftOrderId = draftOrderId)
                .catch { exception ->
                    _deleteDraftOrders.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _deleteDraftOrders.value = ResponseStatus.Success(response)
                        getDraftOrders(_customerID.value)

                    }else{
                        _deleteDraftOrders.value = ResponseStatus.Error(
                            NullPointerException("Delete Draft Orders is null")
                        )
                    }
                }
        }
    }

    fun writeCustomerID(id: String?){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.writeCustomerID(id.toString())
        }
    }
    fun readCustomerID(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCustomerID()
            result.collect{
                Log.i("shared", "From Shared: ${_customerID.value}")
                _customerID.value = it

            }
        }
    }

    fun readCustomersID(): String {
        return repo.readCustomersID()
    }



    fun readCurrencyCode(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCurrencyCode()
            result.collect{
                _currencyCode.value = it
            }
        }
    }



    fun readCurrencyRate(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCurrencyRate()
            result.collect{
                _currencyRate.value = it
            }
        }
    }


    fun getPriceRules() {
        viewModelScope.launch {
            repo.getPriceRules()
                .catch { exception ->
                    _priceRules.value = ResponseStatus.Error(exception)
                }
                .collect { response ->
                    if (response != null) {
                        val now = Instant.now()
                        val cartProductIds = (draftOrders.value as? ResponseStatus.Success)
                            ?.result
                            ?.draft_orders
                            ?.flatMap { it.line_items.mapNotNull { item -> item?.product_id } }
                            ?.toSet() ?: emptySet()

                        val validRules = response.price_rules.filter { rule ->
                            try {
                                val startsAt = Instant.parse(rule.starts_at)
                                startsAt.isBefore(now) || startsAt == now
                            } catch (e: Exception) {
                                false
                            }
                        }.filter { rule ->
                            when {
                                rule.target_selection == "all" && rule.allocation_method == "across" -> true
                                rule.target_selection == "entitled" && rule.allocation_method == "each" -> {
                                    rule.entitled_product_ids?.any { it in cartProductIds } ?: false
                                }
                                else -> false
                            }
                        }

                        // Check discount codes for each price rule
                        val filteredWithCodes = validRules.mapNotNull { rule ->
                            val discountCodes = try {
                                repo.getDiscountCodesByPriceRule(rule.id).firstOrNull()
                            } catch (e: Exception) {
                                null
                            }

                            if (discountCodes?.discount_codes?.isNotEmpty() == true) {
                                // Add to global map for UI (optional)
                                _discountCodesByPriceRule.update { currentMap ->
                                    currentMap + (rule.id to ResponseStatus.Success(discountCodes))
                                }
                                rule
                            } else {
                                null
                            }
                        }

                        val filteredResponse = response.copy(price_rules = filteredWithCodes)
                        _priceRules.value = ResponseStatus.Success(filteredResponse)
                    } else {
                        _priceRules.value = ResponseStatus.Error(
                            NullPointerException("Price Rules is null")
                        )
                    }
                }
        }
    }



    fun getRemainingMonthsText(endsAt: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val endDate = ZonedDateTime.parse(endsAt, formatter)
        val now = ZonedDateTime.now(endDate.zone)

        val months = ChronoUnit.MONTHS.between(now.toLocalDate(), endDate.toLocalDate())

        return when {
            months <= 0 -> "< a month remaining"
            months == 1L -> "1 month remaining"
            else -> "$months months remaining"
        }
    }


    fun getDiscountCodesByPriceRule(priceRuleId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _discountCodesByPriceRule.value += (priceRuleId to ResponseStatus.Loading)

            repo.getDiscountCodesByPriceRule(priceRuleId)
                .catch { exception ->
                    _discountCodesByPriceRule.value += (priceRuleId to ResponseStatus.Error(
                        exception
                    ))
                }
                .collect { response ->
                    if (response != null) {
                        _discountCodesByPriceRule.value += (priceRuleId to ResponseStatus.Success(
                            response
                        ))
                    } else {
                        _discountCodesByPriceRule.value += (priceRuleId to ResponseStatus.Error(
                            Throwable("Null response")
                        ))
                    }
                }
        }
    }

    @VisibleForTesting
    fun setCurrencyForTest(code: String, rate: String) {
        _currencyCode.value = code
        _currencyRate.value = rate
    }

    @VisibleForTesting
    fun setDraftOrdersForTest(response: ResponseStatus<DraftOrderResponse>) {
        _draftOrders.value = response
    }
    fun setCustomerIdForTest(id: String) {
        (this as? CartViewModel)?.apply {
            val field = this::class.java.getDeclaredField("_customerID")
            field.isAccessible = true
            val stateFlow = field.get(this) as MutableStateFlow<String>
            stateFlow.value = id
        }
    }



}
class CartFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(repo) as T
    }
}
