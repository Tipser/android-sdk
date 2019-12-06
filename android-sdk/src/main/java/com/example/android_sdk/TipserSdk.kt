package com.example.android_sdk

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.AuthFailureError


val BASE_TIPSER_URL = "https://tipser.com"
val BASE_TIPSER_API_URL = "https://t3-prod-api.tipser.com"
val TOKEN =
    "eyJraWQiOm51bGwsImFsZyI6IlJTNTEyIn0.eyJpc3MiOiJUaXBzZXIiLCJqdGkiOiJ3RVhsTUhIZkxvVWdmWjB0QWxsZ2p3IiwiaWF0IjoxNTc1NjM4OTUxLCJleHAiOjE2MDcxNzQ5NTEsImNhcnRJZCI6IjVkZWE1N2E3ZGZhNzNlMDAwMTRjYzU4ZSJ9.RUAheE6_plw4QrLAUh5yyBXpdaIm5OxrXPeou4oaxqwxX_jMeEkn-kyFqE4lWPjr6a3POu_jSoON4QQyZGq9V9Kkean2kbDRhqnUmEHnl4QVRrttXbZ-sJsqHur6cYmjv_ZQl_6A-_lZRJUeilOY9RuXjo_oaZTGq-07-PHwinxCaGx49rmLmYtlPZhO-E-z86nUKg4884VvjSeel_sSaMy_m3PuJ4L8lw-zdPyMkrEQ0SRAuTxDyd40LjXMpw83vH_tQlTzZRv031Vz5iP9_5MCBq2HLYYTSXKoUvNB3sz5dJe7h9z6LiSkR2BFh6LfHX9hL-NbUKu-GeC7Sgpyfw"
val COOKIE_DOMAIN = "www.tipser.com"
val TOKEN_INVALID = "TOKEN_INVALID"
val TIPSER_TOKEN_KEY = "tipserToken"

// 1. add product to cart => response ma token
class TipserSdk {
    private lateinit var webView: WebView
    private lateinit var queue: RequestQueue
    private lateinit var token: String

    fun addToCart(productId: String) {
        val token = this.token
        val jsObjRequest = object : StringRequest(
            Request.Method.POST,
            "$BASE_TIPSER_API_URL/v3/shoppingcart/items",
            Response.Listener<String> { response ->

            },
            Response.ErrorListener { e -> e.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["authorization"] = "Bearer $token"
                return params
            }

            override fun getBody(): ByteArray {

                return super.getBody()
            }
        }
        queue.add(jsObjRequest)
        this.webView.loadUrl("https://www.tisper.com/wi/5075d7715c3d090a90585e87/direct-to-checkout?productId=$productId")
    }

    fun init(activityContext: AppCompatActivity) {
        this.queue = Volley.newRequestQueue(activityContext)
        CookieManager.getInstance().setAcceptCookie(true)
        val getNewToken = fun() {
            val jsObjRequest = StringRequest(
                Request.Method.GET,
                "$BASE_TIPSER_API_URL/v3/auth/anonymousToken",
                Response.Listener<String> { response ->
                    CookieManager.getInstance().setCookie(COOKIE_DOMAIN, "tipserToken=$response;")
                },
                Response.ErrorListener { e -> e.printStackTrace() })
            queue.add(jsObjRequest)
        }
        val cookie = CookieManager.getInstance().getCookie(COOKIE_DOMAIN)
        if (cookie != null) {
            val tipserTokenPart = cookie.split(";").map { v -> v.trim() }
                .find { v -> v.startsWith("$TIPSER_TOKEN_KEY=") }
            if (tipserTokenPart != null) {
                val token = tipserTokenPart.split("=")[1]
                this.token = token
                val jsObjRequest = object : StringRequest(
                    Request.Method.GET,
                    "$BASE_TIPSER_API_URL/v3/auth/token",
                    Response.Listener<String> { response ->
                        if (response == TOKEN_INVALID) {
                            getNewToken()
                        } else if(response != this.token){
                            CookieManager.getInstance()
                                .setCookie(COOKIE_DOMAIN, "$TIPSER_TOKEN_KEY=$response;")
                        }
                    },
                    Response.ErrorListener { e -> e.printStackTrace() }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["authorization"] = "Bearer $token"
                        return params
                    }
                }
                queue.add(jsObjRequest)
            } else {
                getNewToken()
            }
        } else {
            getNewToken()
        }
    }

    fun initWebView(activityContext: AppCompatActivity) {
//        CookieManager.getInstance().setCookie(parsedURL.host, "")
        this.webView = WebView(activityContext)

        this.webView.webViewClient = WebViewClient()


//        this.webView.visibility = View.GONE
        activityContext.setContentView(this.webView)
        this.webView.settings.domStorageEnabled = true
        this.webView.settings.javaScriptEnabled = true
        this.webView.loadUrl("https://tipser.com/checkout")
    }

    fun goToCheckout() {

    }

    fun getCartSize() {
    }

    fun directToCheckout() {

    }

    fun showCart() {
        this.webView.loadUrl("https://www.tipser.com/checkout")
    }

}