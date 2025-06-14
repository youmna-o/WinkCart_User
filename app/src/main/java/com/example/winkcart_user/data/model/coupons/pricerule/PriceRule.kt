package com.example.winkcart_user.data.model.coupons.pricerule

/*data class PriceRule(
    val id: Long,
    val value_type: String,
    val value: String,
    val usage_limit: Int?,
    val starts_at: String,
    val ends_at: String,

    val title: String,
)*/

//

data class PriceRule(
    val id: Long,
    val value_type: String,
    val value: String,
    val customer_selection: String,
    val target_type: String,
    val target_selection: String,
    val allocation_method: String,
    val allocation_limit: Int?,
    val once_per_customer: Boolean,
    val usage_limit: Int?,
    val starts_at: String,
    val ends_at: String,
    val created_at: String,
    val updated_at: String,
    val entitled_product_ids: List<Long>,
    val entitled_variant_ids: List<Long>,
    val entitled_collection_ids: List<Long>,
    val entitled_country_ids: List<Long>,
    val prerequisite_product_ids: List<Long>,
    val prerequisite_variant_ids: List<Long>,
    val prerequisite_collection_ids: List<Long>,
    val customer_segment_prerequisite_ids: List<Long>,
    val prerequisite_customer_ids: List<Long>,
    val prerequisite_subtotal_range: PrerequisiteSubtotalRange?,
    val prerequisite_quantity_range: Any?, // Use custom class if needed
    val prerequisite_shipping_price_range: Any?, // Use custom class if needed
    val prerequisite_to_entitlement_quantity_ratio: ToEntitlementQuantityRatio,
    val prerequisite_to_entitlement_purchase: ToEntitlementPurchase,
    val title: String,
    val admin_graphql_api_id: String
)
