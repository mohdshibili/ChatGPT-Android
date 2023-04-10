package com.codeware.chatgpt.model

import com.google.gson.annotations.SerializedName


data class Items(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("create_time") var createTime: String? = null,
    @SerializedName("update_time") var updateTime: String? = null
)