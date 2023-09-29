package com.example.paystackpay

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.paystackpay.api.RetrofitInstance
import com.example.paystackpay.data.InitializeCustomerTransactionRequest
import com.example.paystackpay.data.MakeTransPostRequest
import com.example.paystackpay.databinding.ActivityMainBinding
import com.example.paystackpay.utils.Constants
import com.example.paystackpay.verify.verifyapi.RetrofitInstanceVerify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val sharedPref: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.REFERENCE_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.apply {


            btnTapForPaymentResult.setOnClickListener {
                val getReference = sharedPref.getString(Constants.Ref_Code, "")

                if (!getReference.isNullOrEmpty()) {
                    showToast("verifyinh  $getReference")
                    verifyPayment()
                } else {
                    showToast("no code to verify")
                }
            }


            btnCheckout.setOnClickListener {

                makePostRequet()
            }


        }


    }


    private fun makePostRequet() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // let just assume we passing real data to the server
                val email = "solo1759@yahoo.com"
                val amount = "10000.00"

                val initializeRequest = InitializeCustomerTransactionRequest(email, amount)

                val response: Response<MakeTransPostRequest> =
                    RetrofitInstance.api.initializeTransaction(initializeRequest)


                if (response.isSuccessful) {

                    val authorizationUrl = response.body()?.data?.authorization_url

                    val editor = sharedPref.edit()
                    if (!authorizationUrl.isNullOrEmpty()) {

                        val reference = response.body()?.data?.reference?.toString()
                        editor.putString(Constants.Ref_Code, reference)
                        editor.putString(Constants.AuthorizedBearer, reference)
                        editor.apply()

                        withContext(Dispatchers.Main) {
                            val url = response.body()?.data?.authorization_url?.toString()
                            showToast(url.toString())
                            customtab(url.toString() + "")
                        }
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun customtab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        try {
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.launchUrl(this, Uri.parse(url))
            builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.black))
        } catch (e: Exception) {
            try {
                customTabsIntent.intent.setPackage("com.opera.browser")
                customTabsIntent.launchUrl(this, Uri.parse(url))
                builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.black))
            } catch (f: Exception) {
                try {
                    customTabsIntent.intent.setPackage("com.UCMobile.intl")
                    customTabsIntent.launchUrl(this, Uri.parse(url))
                    builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.black))
                } catch (g: Exception) {
                    customTabsIntent.intent.setPackage(null)
                    customTabsIntent.launchUrl(this, Uri.parse(url))
                    builder.setToolbarColor(ContextCompat.getColor(this, android.R.color.black))
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun verifyPayment() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val getReference_ID = sharedPref.getString(Constants.Ref_Code, "")
                val AuthorizedBearer = sharedPref.getString(Constants.AuthorizedBearer, "")

                val response = RetrofitInstanceVerify.api.getTransaction(getReference_ID.toString())
                Log.d("PaymentVerification", "Response code: ${response.code()}")
                //  showToast(" ${response.code()}")

                if (response.isSuccessful) {
                    val moneyDeposit = response.body()?.status?.toString()

                    if (!moneyDeposit.isNullOrEmpty()) {

                        withContext(Dispatchers.Main) {
                            showToast(moneyDeposit.toString())

                            binding.apply {

                                val status  = response.body()?.status?.toString()
                                val authorization = response.body()?.authorization?.toString()
                                val ammount = response.body()?.amount?.toString()
                                val requestammount = response.body()?.requested_amount?.toString()
                                val pos_transaction_data = response.body()?.pos_transaction_data?.toString()

                                displayResult.text = " $status   $authorization  $ammount  $requestammount  $pos_transaction_data"
                            }

                        }
                    } else {
                        showToast("No amount")
                    }

                }
                else {
                    showToast("error , it is empty")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast(e.message.toString())
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        sharedPref.edit().clear().apply()
    }


}