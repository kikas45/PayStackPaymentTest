package com.example.paystackpay.data

data class MakeTransPostRequest(
    val `data`: Data,
    val message: String,
    val status: Boolean
)