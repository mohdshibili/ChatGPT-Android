package com.codeware.chatgpt.model

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("picture") var picture: String? = null,
    @SerializedName("mfa") var mfa: Boolean? = null,
    @SerializedName("groups") var groups: ArrayList<String> = arrayListOf()

)