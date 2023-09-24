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
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private var NogatWebViewActivity = "CheckoutActivity"
    private lateinit var binding: ActivityCheckoutBinding


    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.postDelayed(Runnable {
            makePostRequet()
        }, 3000)

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
                    Log.d(NogatWebViewActivity, "successful response: ${Gson().toJson(response)}")

                    val authorizationUrl = response.body()?.data?.authorization_url

                    if (!authorizationUrl.isNullOrEmpty()) {

                        // Handle the authorization URL here, e.g., open a WebView with this URL
                        // Example: openWebView(authorizationUrl)
                        // You can also store the access code and reference for future use.

                        val url = response.body()?.data?.authorization_url?.toString()

                        // i internationally delay ot for few seconds
                        handler.postDelayed(Runnable {
                            loadCheckout(url.toString() + "")
                            showToast(url.toString())
                        }, 2000)


                    } else {
                        showToast("Authorization URL not found")
                    }

                } else {
                    Log.d(NogatWebViewActivity, "failed response: ${response.code()}")
                    showToast("failed response: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d(NogatWebViewActivity, "Request failed: ${e.message}")
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
        handler.removeCallbacksAndMessages(null)
    }

}
