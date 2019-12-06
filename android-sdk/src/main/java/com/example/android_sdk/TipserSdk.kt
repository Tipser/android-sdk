package com.example.android_sdk

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class TipserSdk {

    fun addToCart() {

    }

    fun initWebView(activityContext: AppCompatActivity) {
        val myWebView = WebView(activityContext)
        myWebView.webViewClient = WebViewClient()
        activityContext.setContentView(myWebView)
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://tipser.com")
    }

    fun goToCheckout() {

    }

    fun getCartSize() {
    }

    fun directToCheckout() {

    }

}