package com.codeware.chatgpt.model

import com.google.gson.annotations.SerializedName


data class Session(

    @SerializedName("user") var user: User? = User(),
    @SerializedName("expires") var expires: String? = null,
    @SerializedName("accessToken") var accessToken: String? = null

)