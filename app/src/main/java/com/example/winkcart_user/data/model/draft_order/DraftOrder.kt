//package com.example.winkcart_user.data.model.draft_order
//
//import com.example.winkcart_user.data.model.customer.Customer
//import com.google.gson.annotations.SerializedName
//
//data class DraftOrderResponse(
//    val draft_order: DraftOrder
//)
//
//data class DraftOrder(
//    val id: Long,
//    val note: String?,
//    val email: String?,
//    val taxes_included: Boolean,
//    val currency: String,
//    val invoice_sent_at: String?,
//    val created_at: String,
//    val updated_at: String,
//    val tax_exempt: Boolean,
//    val completed_at: String?,
//    val name: String,
//    @SerializedName("allow_discount_codes_in_checkout?")
//    val allowDiscountCodesInCheckout: Boolean,
//    @SerializedName("b2b?")
//    val b2b: Boolean,
//    val status: String,
//    val line_items: List<LineItem>,
//    val api_client_id: Long,
//    val shipping_address:  String?,
//    val billing_address: String?,
//    val invoice_url: String,
//    val created_on_api_version_handle: String,
//    val applied_discount: AppliedDiscount?,
//    val order_id: Long?,
//    val shipping_line: String?,
//    val tax_lines: List<TaxLine>,
//    val tags: String,
//    val note_attributes: List<Any>,
//    val total_price: String,
//    val subtotal_price: String,
//    val total_tax: String,
//    val payment_terms: Any?,
//    val admin_graphql_api_id: String,
//    val customer: Customer
//)