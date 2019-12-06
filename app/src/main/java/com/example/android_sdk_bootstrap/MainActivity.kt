package com.example.android_sdk_bootstrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.android_sdk.TipserSdk

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdk = TipserSdk(this)
        sdk.init()

        val addToCartButton = this.findViewById(R.id.button) as Button
        val showCart = this.findViewById(R.id.button2) as Button

        addToCartButton.setOnClickListener { v ->
            sdk.addToCart("5da5c5139af3ba00010b41bc") {
                val text = "Product added to cart!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
        showCart.setOnClickListener { sdk.goToCheckout() }
    }
}
