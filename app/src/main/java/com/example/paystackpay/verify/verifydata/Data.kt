package com.example.paystackpay.verify.verifydata

data class Data(
    val amount: Int,
    val authorization: Authorization,
    val channel: String,
    val createdAt: String,
    val created_at: String,
    val currency: String,
    val customer: Customer,
    val domain: String,
    val fees: Int,
    val fees_breakdown: Any,
    val fees_split: Any,
    val gateway_response: String,
    val id: Int,
    val ip_address: String,
    val log: Log,
    val message: Any,
    val metadata: String,
    val order_id: Any,
    val paidAt: String,
    val paid_at: String,
    val plan: Any,
    val plan_object: PlanObject,
    val pos_transaction_data: Any,
    val reference: String,
    val requested_amount: Int,
    val source: Any,
    val split: Split,
    val status: String,
    val subaccount: Subaccount,
    val transaction_date: String
)