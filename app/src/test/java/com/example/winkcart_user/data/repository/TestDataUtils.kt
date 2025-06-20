package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.model.coupons.pricerule.PriceRule
import com.example.winkcart_user.data.model.coupons.pricerule.ToEntitlementPurchase
import com.example.winkcart_user.data.model.coupons.pricerule.ToEntitlementQuantityRatio
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.data.model.orders.Address
import com.example.winkcart_user.data.model.orders.Customer
import com.example.winkcart_user.data.model.orders.CustomerAddress
import com.example.winkcart_user.data.model.orders.CustomerOrder
import com.example.winkcart_user.data.model.orders.LineItem
import com.example.winkcart_user.data.model.orders.Money
import com.example.winkcart_user.data.model.orders.MoneySet
import com.example.winkcart_user.data.model.orders.Order
import com.example.winkcart_user.data.model.orders.OrderData
import com.example.winkcart_user.data.model.orders.OrderRequest
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.model.orders.PriceSet
import com.example.winkcart_user.data.model.proddata.Variant
import com.example.winkcart_user.data.model.products.Product
import java.util.Date

object FakeOrderFactory {

    fun createFakeOrdersResponse(): OrdersResponse {
        return OrdersResponse(
            orders = listOf(createFakeOrder())
        )
    }

    fun createFakeOrder(): Order {
        return Order(
            id = 123L,
            cancelledAt = null,
            contactEmail = "test@example.com",
            createdAt = "2024-01-01T10:00:00Z",
            currency = "USD",
            currentSubtotalPrice = "100.00",
            currentSubtotalPriceSet = createMoneySet("100.00"),
            currentTotalAdditionalFeesSet = null,
            currentTotalDiscounts = "10.00",
            currentTotalDiscountsSet = createMoneySet("10.00"),
            currentTotalDutiesSet = null,
            currentTotalPrice = "90.00",
            currentTotalPriceSet = createMoneySet("90.00"),
            currentTotalTax = "5.00",
            currentTotalTaxSet = createMoneySet("5.00"),
            discountCodes = emptyList(),
            dutiesIncluded = false,
            email = "test@example.com",
            estimatedTaxes = true,
            financialStatus = "paid",
            fulfillmentStatus = "fulfilled",
            landingSite = null,
            landingSiteRef = null,
            locationId = null,
            merchantBusinessEntityId = "merchant-id",
            merchantOfRecordAppId = null,
            name = "#1001",
            note = null,
            noteAttributes = emptyList(),
            number = 1,
            orderNumber = 1001,
            orderStatusUrl = "https://example.com/order/123",
            originalTotalAdditionalFeesSet = null,
            originalTotalDutiesSet = null,
            paymentGatewayNames = listOf("visa"),
            phone = null,
            poNumber = null,
            presentmentCurrency = "USD",
            processedAt = Date(),
            reference = null,
            referringSite = null,
            sourceIdentifier = null,
            sourceName = "web",
            sourceUrl = null,
            subtotalPrice = "100.00",
            subtotalPriceSet = createMoneySet("100.00"),
            tags = "test",
            taxExempt = false,
            taxLines = emptyList(),
            taxesIncluded = false,
            test = false,
            token = "token-123",
            totalCashRoundingPaymentAdjustmentSet = createMoneySet("0.00"),
            totalCashRoundingRefundAdjustmentSet = createMoneySet("0.00"),
            totalDiscounts = "10.00",
            totalDiscountsSet = createMoneySet("10.00"),
            totalLineItemsPrice = "100.00",
            totalLineItemsPriceSet = createMoneySet("100.00"),
            totalOutstanding = "0.00",
            totalPrice = "90.00",
            totalPriceSet = createMoneySet("90.00"),
            totalShippingPriceSet = createMoneySet("0.00"),
            totalTax = "5.00",
            totalTaxSet = createMoneySet("5.00"),
            totalTipReceived = "0.00",
            totalWeight = 1000,
            updatedAt = Date(),
            userId = 999L,
            billingAddress = createFakeAddress(),
            shippingAddress = createFakeAddress(),
            customer = createFakeCustomer(),
            discountApplications = emptyList(),
            fulfillments = emptyList(),
            lineItems = listOf(createFakeLineItem())
        )
    }

    private fun createMoneySet(amount: String): MoneySet {
        val money = Money(amount = amount, currencyCode = "EGY")
        return MoneySet(shopMoney = money, presentmentMoney = money)
    }

    private fun createFakeAddress(): Address {
        return Address(
            firstName = "John",
            address1 = "123 Test Street",
            phone = "1234567890",
            city = "Testville",
            zip = "12345",
            province = "Test Province",
            country = "USA",
            lastName = "Doe",
            address2 = null,
            company = "Test Inc.",
            latitude = 37.7749,
            longitude = -122.4194,
            name = "John Doe",
            countryCode = "US",
            provinceCode = "TP"
        )
    }

    private fun createFakeCustomer(): Customer {
        return Customer(
            id = 1L,
            email = "test@example.com",
            createdAt = Date(),
            updatedAt = Date(),
            firstName = "John",
            lastName = "Doe",
            state = "enabled",
            note = null,
            verifiedEmail = true,
            multipassIdentifier = null,
            taxExempt = false,
            phone = "1234567890",
            tags = "",
            currency = "USD",
            taxExemptions = emptyList(),
            adminGraphqlApiId = "gid://shopify/Customer/1",
            defaultAddress = createFakeCustomerAddress()
        )
    }

    private fun createFakeCustomerAddress(): CustomerAddress {
        return CustomerAddress(
            id = 1L,
            customerId = 1L,
            firstName = "John",
            lastName = "Doe",
            company = "Test Inc.",
            address1 = "123 Test Street",
            address2 = null,
            city = "Testville",
            province = "Test Province",
            country = "USA",
            zip = "12345",
            phone = "1234567890",
            name = "John Doe",
            provinceCode = "TP",
            countryCode = "US",
            countryName = "United States",
            default = true
        )
    }

    private fun createFakeLineItem(): LineItem {
        return LineItem(
            currentQuantity = 1,
            fulfillableQuantity = 1,
            price = "100.00",
            priceSet = PriceSet(
                shopMoney = Money("100.00", "USD"),
                presentmentMoney = Money("100.00", "USD")
            ),
            productExists = true,
            productId = 111L,
            quantity = 1,
            taxable = true,
            title = "Test Product",
            totalDiscount = "0.00",
            totalDiscountSet = createMoneySet("0.00"),
            variantTitle = "Default",
            vendor = "MockVendor"
        )
    }
}

fun createMockProduct(id: Long, title: String, price: Double,vendor:String ="MockVendor"): Product {
    val variant = Variant(
        id = id + 1000,
        product_id = id,
        title = "OS / black / 10",
        price = price.toString(),
        position = 1,
        inventory_policy = "deny",
        compare_at_price = null,
        option1 = "OS",
        option2 = "black",
        option3 = "10",
        created_at = "2025-06-08T07:23:29+03:00",
        updated_at = "2025-06-08T07:23:29+03:00",
        taxable = true,
        barcode = null,
        fulfillment_service = "manual",
        grams = 0,
        inventory_management = "shopify",
        requires_shipping = true,
        sku = "SKU-$id",
        weight = 0.0,
        weight_unit = "kg",
        inventory_item_id = id + 2000,
        inventory_quantity = 30,
        old_inventory_quantity = 30,
        admin_graphql_api_id = "gid://shopify/ProductVariant/${id + 1000}",
        image_id = null
    )
    return Product(
        id = id,
        title = title,
        body_html = "",
        vendor = vendor,
        product_type = "MockType",
        created_at = "2025-01-01T00:00:00Z",
        handle = "mock-handle",
        updated_at = "2025-01-01T00:00:00Z",
        published_at = "2025-01-01T00:00:00Z",
        template_suffix = null,
        published_scope = "global",
        tags = "",
        status = "active",
        admin_graphql_api_id = "mock-api-id",
        options = emptyList(),
        images = emptyList(),
        image = null,
        variants =  listOf(variant)
    )
}

    fun createFakeOrderRequest(): OrderRequest {
        return OrderRequest(
            order = OrderData(
                customer = CustomerOrder(id = 101),
                line_items = listOf(
                    LineItemDraft(
                        title = "Test Product",
                        price = "19.99",
                        quantity = 1,
                        variant_id = 11,
                        properties = emptyList(),
                        product_id = 1
                    )
                ),
                send_receipt = true
            )
        )
    }


fun createDraftOrderWithItems(vararg items: LineItemDraft): DraftOrder {
    return DraftOrder(
        id = 999L,
        customer = com.example.winkcart_user.data.model.draftorder.cart.Customer(123),
        line_items = items.toList()
    )
}


fun createPercentageCoupon(
    id: Long = 1L,
    value: Double = 10.0,
    entitledProductIds: List<Long> = emptyList()
): PriceRule {
    return PriceRule(
        id = id,
        value_type = "percentage",
        value = "-$value",
        customer_selection = "all",
        target_type = "line_item",
        target_selection = if (entitledProductIds.isEmpty()) "all" else "entitled",
        allocation_method = "across",
        allocation_limit = null,
        once_per_customer = false,
        usage_limit = null,
        starts_at = "2024-01-01T00:00:00Z",
        ends_at = "2025-01-01T00:00:00Z",
        created_at = "2024-01-01T00:00:00Z",
        updated_at = "2024-01-01T00:00:00Z",
        entitled_product_ids = entitledProductIds,
        entitled_variant_ids = listOf(),
        entitled_collection_ids = listOf(),
        entitled_country_ids = listOf(),
        prerequisite_product_ids = listOf(),
        prerequisite_variant_ids = listOf(),
        prerequisite_collection_ids = listOf(),
        customer_segment_prerequisite_ids = listOf(),
        prerequisite_customer_ids = listOf(),
        prerequisite_subtotal_range = null,
        prerequisite_quantity_range = null,
        prerequisite_shipping_price_range = null,
        prerequisite_to_entitlement_quantity_ratio = ToEntitlementQuantityRatio(0, 0),
        prerequisite_to_entitlement_purchase = ToEntitlementPurchase(0),
        title = "Test $value% Coupon",
        admin_graphql_api_id = "gid://shopify/PriceRule/$id"
    )
}

fun createFakeDraftOrderRequest(): DraftOrderRequest {
    return DraftOrderRequest(
        draft_order = com.example.winkcart_user.data.model.draftorder.cart.DraftOrder(
            id = 999,
            customer = com.example.winkcart_user.data.model.draftorder.cart.Customer(123),
            line_items = listOf(
                LineItemDraft(
                    title = "Test Item",
                    price = "100.0",
                    quantity = 1,
                    product_id = 1,
                    variant_id = 1,
                    properties = listOf(
                        com.example.winkcart_user.data.model.draftorder.cart.Property("SavedAt", "Cart")
                    )
                )
            )
        )
    )
}

