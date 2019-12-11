package com.example.android_sdk

import android.webkit.CookieManager

object Cookie {
    private const val COOKIE_DOMAIN = "www.tipser.com"

    init {
        CookieManager.getInstance().setAcceptCookie(true)
    }

    private fun getListOfCookieValues(): List<String>? {
        val cookie = CookieManager.getInstance().getCookie(COOKIE_DOMAIN) ?: return null

        return cookie
            .split(";")
            .map { v -> v.trim() }
    }

    fun setValue(key: String, value: String) {
        val cookieValues = this.getListOfCookieValues()

        val newCookieValues = cookieValues?.filter { v ->
            !v.startsWith("$key=")
        }?.joinToString("; ")?.plus("; $key=$value;")
            ?: "$key=$value;"

        CookieManager.getInstance().setCookie(COOKIE_DOMAIN, newCookieValues)
    }

    fun getValue(key: String): String? {
        val cookieValues = this.getListOfCookieValues() ?: return null

        val cookieValue = cookieValues
            .find { v ->
                v.startsWith("$key=")
            }
        return if (cookieValue != null) {
            cookieValue.split("=")[1]
        } else {
            null
        }
    }
}
