package com.example.android_sdk_bootstrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.Button
import com.example.android_sdk.TipserSdk
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdk = TipserSdk()



//        val addToCartButton = this.findViewById(R.id.button) as Button
//        val showCart= this.findViewById(R.id.button2) as Button
        sdk.init(this)
        sdk.initWebView(this)
//        addToCartButton.setOnClickListener {v -> sdk.addToCart("5da5c5139af3ba00010b41bc") }
//
//        showCart.setOnClickListener {v -> sdk.showCart()}
    }
}
