package com.codeware.chatgpt.model

class ChatItem : Cloneable {
    var messages: ArrayList<MessageItem>? = null
    var chatType: ChatType? = null

    constructor(chatType: ChatType?) {
        this.chatType = chatType
    }

    var isTypingFinished = false
    var time: Long = 0
    var responseFinishTime: Long = 0
    var question: String? = null
    var answerHtml: String? = null
    var id = 0

    constructor() {}

    fun setMessage(messageItems: ArrayList<MessageItem>?) {
        messages = messageItems
    }

    enum class ChatType {
        QUESTION, REPLY, EMPTY
    }

    public override fun clone(): Any {
        return try {
            super.clone()
        } catch (e: CloneNotSupportedException) {
            val chatItem = ChatItem()
            chatItem.question = question
            chatItem.setMessage(messages)
            chatItem.answerHtml = answerHtml
            chatItem.id = id
            chatItem.time = time
            chatItem.responseFinishTime = responseFinishTime
            chatItem.isTypingFinished = isTypingFinished
            chatItem
        }
    }
}