package com.example.paystackpay.verify.verifyapi

import com.example.paystackpay.utils.Constants
import com.example.paystackpay.verify.verifydata.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface VerifyTransService {

    // make a get request to verify payment

    @Headers("Authorization: Bearer ${Constants.SERVER_KEY}", "Content-Type: application/json")
    @GET("transaction/verify/{id}")
    suspend fun getTransaction(
        @Path("id") id: String
    ): Response<Data>
}





/*package com.example.paystackpay.verify.verifyapi

import com.example.paystackpay.utils.Constants
import com.example.paystackpay.verify.verifydata.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface VerifyTransService {
    @GET("transaction/{id}")
    suspend fun getTransaction(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
    ): Response<Data>
}*/
