package com.codeware.chatgpt.listener

import com.codeware.chatgpt.model.Conversations
import com.codeware.chatgpt.model.Session

interface FetchListener {
    fun onSession(session: Session)
    fun onConversationList(conversations: Conversations)
}