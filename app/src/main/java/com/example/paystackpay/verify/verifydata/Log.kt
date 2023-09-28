package com.example.paystackpay.verify.verifydata

data class Log(
    val attempts: Int,
    val errors: Int,
    val history: List<History>,
    val input: List<Any>,
    val mobile: Boolean,
    val start_time: Int,
    val success: Boolean,
    val time_spent: Int
)