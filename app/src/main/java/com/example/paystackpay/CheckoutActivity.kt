package com.example.paystackpay

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.paystackpay.data.InitializeCustomerTransactionRequest
import com.example.paystackpay.data.MakeTransPostRequest
import com.example.paystackpay.api.RetrofitInstance
import com.example.paystackpay.databinding.ActivityCheckoutBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private var NogatWebViewActivity = "CheckoutActivity"
    private lateinit var binding: ActivityCheckoutBinding



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makePostRequet()


    }

    private fun makePostRequet() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                // let just assume we passing real data to the server
                val email = "customer@email.com"
                val amount = "20000"

                val initializeRequest = InitializeCustomerTransactionRequest(email, amount)


                val response: Response<MakeTransPostRequest> =
                    RetrofitInstance.api.initializeTransaction(initializeRequest)


                if (response.isSuccessful) {

                    val authorizationUrl = response.body()?.data?.authorization_url

                    if (!authorizationUrl.isNullOrEmpty()) {

                        withContext(Dispatchers.Main){
                            val url = response.body()?.data?.authorization_url?.toString()
                            showToast(url.toString())
                            Log.d(NogatWebViewActivity, "authorizationUrl: ${url.toString()}}")
                            loadCheckout(url.toString() + "")
                        }

                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Log.d(NogatWebViewActivity, "Request failed: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadCheckout(authorizationUrl: String) {


        binding.apply {

            webview222.settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }

            webview222.loadUrl(authorizationUrl)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
