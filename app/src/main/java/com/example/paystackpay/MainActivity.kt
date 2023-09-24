package com.example.paystackpay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val price = 4000
        val mCheckout = findViewById<Button>(R.id.btn_checkout)

        mCheckout.setOnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, CheckoutActivity::class.java)
            intent.putExtra(getString(R.string.meal_name), price)
            startActivity(intent)
        }

    }


}