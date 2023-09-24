package com.example.paystackpay.api

import com.example.paystackpay.data.InitializeCustomerTransactionRequest
import com.example.paystackpay.data.MakeTransPostRequest
import com.example.paystackpay.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



interface PaystackService {

    @Headers("Authorization: Bearer ${Constants.SERVER_KEY}", "Content-Type: application/json")
    @POST("transaction/initialize")
    suspend fun initializeTransaction(
        @Body requestData: InitializeCustomerTransactionRequest
    ): Response<MakeTransPostRequest>
}
