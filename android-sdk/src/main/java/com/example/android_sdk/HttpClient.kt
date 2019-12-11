package com.example.android_sdk

import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class HttpClient(private val requestQueue: RequestQueue) {
    fun get(
        url: String,
        onSuccess: (response: String) -> Unit,
        onError: (e: VolleyError) -> Unit,
        headers: Map<String, String> = HashMap()
    ) {
        requestQueue.add(object : StringRequest(
            Method.GET,
            url,
            Response.Listener<String> { r -> onSuccess(r) },
            Response.ErrorListener { e -> onError(e) }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        })
    }

    fun post(
        url: String,
        onSuccess: (response: String) -> Unit,
        onError: (e: VolleyError) -> Unit,
        body: JSONObject,
        headers: Map<String, String> = HashMap()
    ) {
        requestQueue.add(object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { r -> onSuccess(r) },
            Response.ErrorListener { e -> onError(e) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return body.toString().toByteArray()
            }
        })
    }
}
