package com.codeware.chatgpt.listener

import android.webkit.JavascriptInterface
import com.codeware.chatgpt.model.Conversations
import com.codeware.chatgpt.model.Session
import com.google.gson.Gson
import org.json.JSONObject

class JSInterface(private val fetchListener: FetchListener) {

    @JavascriptInterface
    fun onFetchResponse(response: String) {
        val jsonObject = JSONObject(response)
        val url = jsonObject.getString("url")

        if (url.contains("/api/auth/session")) {
            val session = Gson().fromJson(jsonObject.getString("data"), Session::class.java)
            fetchListener.onSession(session)
        } else if (url.contains("/conversations?offset")) {
            val conversations =
                Gson().fromJson(jsonObject.getString("data"), Conversations::class.java)
            fetchListener.onConversationList(conversations)
        }
    }

    companion object {
        private const val TAG = "JSInterface"
    }
}