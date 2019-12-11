package com.example.android_sdk

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.VolleyError


val BASE_TIPSER_API_URL = "https://t3-prod-api.tipser.com"
val TOKEN_INVALID = "TOKEN_INVALID"
const val TIPSER_TOKEN_KEY = "tipserToken"

class TipserSdk(activityContext: AppCompatActivity) {
    private var queue: RequestQueue
    private lateinit var token: String
    private var apiClient: ApiClient
    private val context = activityContext

    init {
        this.queue = Volley.newRequestQueue(this.context)
        val httpClient = HttpClient(queue)
        apiClient = ApiClient(BASE_TIPSER_API_URL, httpClient)

        initToken()
    }

    private fun initToken() {
        val getNewToken = fun() {
            apiClient.getAnonymousToken(
                { newToken -> setToken(newToken) },
                { e -> e.printStackTrace() })
        }

        val tokenFromCookie = Cookie.getValue(TIPSER_TOKEN_KEY)

        if (tokenFromCookie != null) {
            token = tokenFromCookie
            apiClient.getToken(token, { newToken ->
                if (newToken == TOKEN_INVALID) {
                    getNewToken()
                } else {
                    setToken(newToken)
                }
            }, { e -> e.printStackTrace() })
        } else {
            getNewToken()
        }
    }

    fun addToCart(productId: String, quantity: Number, onSuccess: () -> Unit, onError: (e: VolleyError) -> Unit) {
        apiClient.addToCart(token, productId, quantity, onSuccess, onError)
    }

    fun goToCheckout() {
        val intent = Intent(this.context, CheckoutActivity::class.java)
        startActivity(this.context, intent, null)
    }

    private fun setToken(newToken: String) {
        Cookie.setValue(TIPSER_TOKEN_KEY, newToken)
        token = newToken
    }
}
