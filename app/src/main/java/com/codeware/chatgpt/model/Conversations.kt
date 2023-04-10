package com.codeware.chatgpt.model

import com.google.gson.annotations.SerializedName


data class Conversations(

    @SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("has_missing_conversations") var hasMissingConversations: Boolean? = null

)