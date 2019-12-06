package com.example.android_sdk

import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.AuthFailureError
import org.json.JSONObject
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity


val BASE_TIPSER_API_URL = "https://t3-prod-api.tipser.com"
val COOKIE_DOMAIN = "www.tipser.com"
val TOKEN_INVALID = "TOKEN_INVALID"
val TIPSER_TOKEN_KEY = "tipserToken"

class TipserSdk(activityContext: AppCompatActivity) {
    private lateinit var webView: WebView
    private lateinit var queue: RequestQueue
    private lateinit var token: String
    private val context = activityContext;

    fun addToCart(productId: String, onSuccess: () -> Unit) {
        val token = this.token
        val jsonBody = JSONObject()
        jsonBody.put("posArticle", "tipser")
        jsonBody.put("posData", "")
        jsonBody.put("posId", "")
        jsonBody.put("productId", productId)
        jsonBody.put("quantity", 1)
        val requestBody = jsonBody.toString()

        val jsObjRequest = object : StringRequest(
            Request.Method.POST,
            "$BASE_TIPSER_API_URL/v3/shoppingcart/items",
            Response.Listener<String> { response -> onSuccess()},
            Response.ErrorListener { e -> e.printStackTrace() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["authorization"] = "Bearer $token"
                return params
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        queue.add(jsObjRequest)
    }

    fun init() {
        this.queue = Volley.newRequestQueue(this.context)
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
                        } else if (response != this.token) {
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

    fun goToCheckout() {
        val intent = Intent(this.context, CheckoutActivity::class.java)
        startActivity(this.context, intent, null)
    }

}