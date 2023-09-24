package com.example.paystackpay.data

// we create this class template as default data needed to make the Post request
data class InitializeCustomerTransactionRequest(
    val email: String,
    val amount: String
)
