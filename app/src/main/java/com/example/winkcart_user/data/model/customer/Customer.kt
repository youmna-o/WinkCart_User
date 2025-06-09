package com.example.winkcart_user.data.model.customer

data class CustomerResponse(
    val customer: Customer
)

data class Customer(
    val email: String,
    val first_name: String,
    val last_name: String,
    val id: Long,
    val updated_at: String,
    val created_at: String,
    val orders_count: Int,
    val state: String,
    val total_spent: String,
    val last_order_id: Long?,
    val note: String?,
    val verified_email: Boolean,
    val multipass_identifier: String?,
    val tax_exempt: Boolean,
    val tags: String,
    val last_order_name: String?,
    val currency: String,
    val phone: String,
    val addresses: List<Address>,
    val tax_exemptions: List<String>,
    val email_marketing_consent: MarketingConsent,
    val sms_marketing_consent: SmsMarketingConsent,
    val admin_graphql_api_id: String,
    val default_address: Address
)