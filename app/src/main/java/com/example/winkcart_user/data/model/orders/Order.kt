
package com.example.winkcart_user.data.model.orders

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Order(
    @SerializedName("id")
    val id: Long,

    @SerializedName("cancelled_at")
    val cancelledAt: String?,

    @SerializedName("contact_email")
    val contactEmail: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("current_subtotal_price")
    val currentSubtotalPrice: String,

    @SerializedName("current_subtotal_price_set")
    val currentSubtotalPriceSet: MoneySet,

    @SerializedName("current_total_additional_fees_set")
    val currentTotalAdditionalFeesSet: Any?,

    @SerializedName("current_total_discounts")
    val currentTotalDiscounts: String,

    @SerializedName("current_total_discounts_set")
    val currentTotalDiscountsSet: MoneySet,

    @SerializedName("current_total_duties_set")
    val currentTotalDutiesSet: Any?,

    @SerializedName("current_total_price")
    val currentTotalPrice: String,

    @SerializedName("current_total_price_set")
    val currentTotalPriceSet: MoneySet,

    @SerializedName("current_total_tax")
    val currentTotalTax: String,

    @SerializedName("current_total_tax_set")
    val currentTotalTaxSet: MoneySet,

    @SerializedName("discount_codes")
    val discountCodes: List<Any>,

    @SerializedName("duties_included")
    val dutiesIncluded: Boolean,

    @SerializedName("email")
    val email: String,

    @SerializedName("estimated_taxes")
    val estimatedTaxes: Boolean,

    @SerializedName("financial_status")
    val financialStatus: String,

    @SerializedName("fulfillment_status")
    val fulfillmentStatus: String?,

    @SerializedName("landing_site")
    val landingSite: String?,

    @SerializedName("landing_site_ref")
    val landingSiteRef: String?,

    @SerializedName("location_id")
    val locationId: Long?,

    @SerializedName("merchant_business_entity_id")
    val merchantBusinessEntityId: String,

    @SerializedName("merchant_of_record_app_id")
    val merchantOfRecordAppId: Any?,

    @SerializedName("name")
    val name: String,

    @SerializedName("note")
    val note: String?,

    @SerializedName("note_attributes")
    val noteAttributes: List<Any>,

    @SerializedName("number")
    val number: Int,

    @SerializedName("order_number")
    val orderNumber: Int,

    @SerializedName("order_status_url")
    val orderStatusUrl: String,

    @SerializedName("original_total_additional_fees_set")
    val originalTotalAdditionalFeesSet: Any?,

    @SerializedName("original_total_duties_set")
    val originalTotalDutiesSet: Any?,

    @SerializedName("payment_gateway_names")
    val paymentGatewayNames: List<String>,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("po_number")
    val poNumber: String?,

    @SerializedName("presentment_currency")
    val presentmentCurrency: String,

    @SerializedName("processed_at")
    val processedAt: Date,

    @SerializedName("reference")
    val reference: String?,

    @SerializedName("referring_site")
    val referringSite: String?,

    @SerializedName("source_identifier")
    val sourceIdentifier: String?,

    @SerializedName("source_name")
    val sourceName: String,

    @SerializedName("source_url")
    val sourceUrl: String?,

    @SerializedName("subtotal_price")
    val subtotalPrice: String,

    @SerializedName("subtotal_price_set")
    val subtotalPriceSet: MoneySet,

    @SerializedName("tags")
    val tags: String,

    @SerializedName("tax_exempt")
    val taxExempt: Boolean,

    @SerializedName("tax_lines")
    val taxLines: List<Any>,

    @SerializedName("taxes_included")
    val taxesIncluded: Boolean,

    @SerializedName("test")
    val test: Boolean,

    @SerializedName("token")
    val token: String,

    @SerializedName("total_cash_rounding_payment_adjustment_set")
    val totalCashRoundingPaymentAdjustmentSet: MoneySet,

    @SerializedName("total_cash_rounding_refund_adjustment_set")
    val totalCashRoundingRefundAdjustmentSet: MoneySet,

    @SerializedName("total_discounts")
    val totalDiscounts: String,

    @SerializedName("total_discounts_set")
    val totalDiscountsSet: MoneySet,

    @SerializedName("total_line_items_price")
    val totalLineItemsPrice: String,

    @SerializedName("total_line_items_price_set")
    val totalLineItemsPriceSet: MoneySet,

    @SerializedName("total_outstanding")
    val totalOutstanding: String,

    @SerializedName("total_price")
    val totalPrice: String,

    @SerializedName("total_price_set")
    val totalPriceSet: MoneySet,

    @SerializedName("total_shipping_price_set")
    val totalShippingPriceSet: MoneySet,

    @SerializedName("total_tax")
    val totalTax: String,

    @SerializedName("total_tax_set")
    val totalTaxSet: MoneySet,

    @SerializedName("total_tip_received")
    val totalTipReceived: String,

    @SerializedName("total_weight")
    val totalWeight: Int,

    @SerializedName("updated_at")
    val updatedAt: Date,

    @SerializedName("user_id")
    val userId: Long?,

    @SerializedName("billing_address")
    val billingAddress: Address,

    @SerializedName("customer")
    val customer: Customer,

    @SerializedName("discount_applications")
    val discountApplications: List<DiscountApplication>,

    @SerializedName("fulfillments")
    val fulfillments: List<Any>,

    @SerializedName("line_items")
    val lineItems: List<LineItem>,

    @SerializedName("shipping_address")
    val shippingAddress: Address

)

data class Address(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("address1")
    val address1: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("zip")
    val zip: String,

    @SerializedName("province")
    val province: String?,

    @SerializedName("country")
    val country: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("address2")
    val address2: String?,

    @SerializedName("company")
    val company: String?,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("name")
    val name: String,

    @SerializedName("country_code")
    val countryCode: String,

    @SerializedName("province_code")
    val provinceCode: String?
)

data class PriceSet(
    @SerializedName("shop_money")
    val shopMoney: Money,

    @SerializedName("presentment_money")
    val presentmentMoney: Money
)

data class Money(
    @SerializedName("amount")
    val amount: String,

    @SerializedName("currency_code")
    val currencyCode: String
)

data class MoneySet(
    @SerializedName("shop_money")
    val shopMoney: Money,

    @SerializedName("presentment_money")
    val presentmentMoney: Money
)


data class DiscountApplication(
    val target_type: String,
    val type: String,
    val value: String,
    val value_type: String,
    val allocation_method: String,
    val target_selection: String,
    val code: String
)

data class CustomerAddress(
    @SerializedName("id")
    val id: Long,

    @SerializedName("customer_id")
    val customerId: Long,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("company")
    val company: String?,

    @SerializedName("address1")
    val address1: String,

    @SerializedName("address2")
    val address2: String?,

    @SerializedName("city")
    val city: String,

    @SerializedName("province")
    val province: String?,

    @SerializedName("country")
    val country: String,

    @SerializedName("zip")
    val zip: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("province_code")
    val provinceCode: String?,

    @SerializedName("country_code")
    val countryCode: String,

    @SerializedName("country_name")
    val countryName: String,

    @SerializedName("default")
    val default: Boolean
)

data class Customer(
    @SerializedName("id")
    val id: Long,

    @SerializedName("email")
    val email: String,

    @SerializedName("created_at")
    val createdAt: Date,

    @SerializedName("updated_at")
    val updatedAt: Date,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("note")
    val note: String?,

    @SerializedName("verified_email")
    val verifiedEmail: Boolean,

    @SerializedName("multipass_identifier")
    val multipassIdentifier: String?,

    @SerializedName("tax_exempt")
    val taxExempt: Boolean,

    @SerializedName("phone")
    val phone: String,


    @SerializedName("tags")
    val tags: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("tax_exemptions")
    val taxExemptions: List<String>,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,

    @SerializedName("default_address")
    val defaultAddress: CustomerAddress
)

data class LineItem(
    @SerializedName("current_quantity")
    val currentQuantity: Int,

    @SerializedName("fulfillable_quantity")
    val fulfillableQuantity: Int,

    @SerializedName("price")
    val price: String,

    @SerializedName("price_set")
    val priceSet: PriceSet,

    @SerializedName("product_exists")
    val productExists: Boolean,

    @SerializedName("product_id")
    val productId: Long?,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("taxable")
    val taxable: Boolean,

    @SerializedName("title")
    val title: String,

    @SerializedName("total_discount")
    val totalDiscount: String,

    @SerializedName("total_discount_set")
    val totalDiscountSet: MoneySet,

    @SerializedName("variant_title")
    val variantTitle: String?,

    @SerializedName("vendor")
    val vendor: String
)
