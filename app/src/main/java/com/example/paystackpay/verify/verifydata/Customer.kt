package com.example.paystackpay.verify.verifydata

data class Customer(
    val customer_code: String,
    val email: String,
    val first_name: Any,
    val id: Int,
    val international_format_phone: Any,
    val last_name: Any,
    val metadata: Any,
    val phone: Any,
    val risk_action: String
)