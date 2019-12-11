package com.example.android_sdk

import com.android.volley.*
import org.json.JSONObject

class ApiClient(private val apiBaseUrl: String, private val httpClient: HttpClient) {

    fun getAnonymousToken(onSuccess: (token: String) -> Unit, onError: (e: VolleyError) -> Unit) {
        httpClient.get(
            "$apiBaseUrl/v3/auth/anonymousToken",
            { token -> onSuccess(normalizeToken(token)) },
            onError
        )
    }

    fun getToken(previousToken: String, onSuccess: (token: String) -> Unit, onError: (e: VolleyError) -> Unit) {
        httpClient.get(
            "$apiBaseUrl/v3/auth/token",
            { token -> onSuccess(normalizeToken(token)) },
            onError,
            getHeaders(previousToken)
        )
    }

    fun addToCart(token: String, productId: String, quantity: Number, onSuccess: () -> Unit, onError: (e: VolleyError) -> Unit) {
        httpClient.post(
            "$apiBaseUrl/v3/shoppingcart/items",
            { onSuccess() },
            onError,
            getAddToCartPayload(productId, quantity),
            getHeaders(token)
        )
    }

    private fun normalizeToken(token: String): String {
        return if (token.startsWith("\"")) {
            token.substring(1, token.length - 1)
        } else {
            token
        }
    }

    private fun getHeaders(token: String): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["authorization"] = "Bearer $token"
        return headers
    }

    private fun getAddToCartPayload(productId: String, quantity: Number): JSONObject {
        val jsonBody = JSONObject()
        jsonBody.put("posArticle", "tipser")
        jsonBody.put("posData", "")
        jsonBody.put("posId", "")
        jsonBody.put("productId", productId)
        jsonBody.put("quantity", quantity)
        return jsonBody
    }
}
