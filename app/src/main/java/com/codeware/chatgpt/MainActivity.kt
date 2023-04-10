package com.codeware.chatgpt

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codeware.chatgpt.adapter.ChatAdapter
import com.codeware.chatgpt.adapter.ConversationListAdapter
import com.codeware.chatgpt.databinding.*
import com.codeware.chatgpt.db.DatabaseHelper
import com.codeware.chatgpt.listener.FetchListener
import com.codeware.chatgpt.listener.JSInterface
import com.codeware.chatgpt.model.*
import com.codeware.chatgpt.preference.Preferences
import com.codeware.chatgpt.utils.JsoupUtils
import com.codeware.chatgpt.utils.ScriptUtils
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private var replaying: Boolean = false
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var conversationList: RecyclerView
    private lateinit var send: Button
    private lateinit var message: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var subtitle: TextView
    private lateinit var subtitleContainer: LinearLayout
    private lateinit var typingIndicator: LottieAnimationView
    private lateinit var userImage: ImageView
    private lateinit var userImageHeader: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var drawerLayout: DrawerLayout

    private var chatList: ArrayList<ChatItem> = ArrayList()
    private var conversationsList: ArrayList<Items> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var conversationListAdapter: ConversationListAdapter

    private lateinit var webBinding: WebBinding
    private lateinit var chatBinding: ChatBinding
    private lateinit var splashBinding: SplashBinding
    private lateinit var toolbarBinding: HomeToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        sharedPreferences = getSharedPreferences("com.codeware.chatgpt", MODE_PRIVATE)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        initViews()
        initDrawer()
        initWebView()
        initRecyclerView()
        initConversationList()
        initSend()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "Search")?.setIcon(R.drawable.baseline_search_24)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            0 -> showToast("Search clicked")

            else -> showToast("No item clicked")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun initDrawer() {
        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initSend() {
        send.setOnClickListener {
            val question = message.text.toString()

            if (question.isNotEmpty()) {
                val calendar = Calendar.getInstance()

                var chatItem = ChatItem()
                chatItem.question = question
                chatItem.chatType = ChatItem.ChatType.QUESTION
                chatItem.time = calendar.timeInMillis
                chatList.add(chatItem)

                chatAdapter.notifyItemInserted(chatList.size - 1)

                chatItem = ChatItem()
                val messageItem = MessageItem()
                messageItem.taG_TYPE = JsoupUtils.TAG_TYPE.P
                messageItem.message = ""
                val messageItems = ArrayList<MessageItem>()
                messageItems.add(messageItem)
                chatItem.setMessage(messageItems)
                chatItem.chatType = ChatItem.ChatType.REPLY
                chatItem.time = calendar.timeInMillis
                chatList.add(chatItem)

                chatAdapter.notifyItemInserted(chatList.size - 1)

                chatList.add(ChatItem(ChatItem.ChatType.EMPTY))

                chatAdapter.notifyItemInserted(chatList.size - 1)

                recyclerView.post {
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(
                        (recyclerView.layoutManager as LinearLayoutManager).itemCount - 1
                    )
                }

                webView.evaluateJavascript(
                    ScriptUtils.sendQuestion(question)
                ) {
                    message.setText("")
                    send.isEnabled = false
                    setSubtitle("typing")
                    typingIndicator.visibility = View.VISIBLE
                    replaying = true
                    getReply()
                }
            }
        }
    }

    private fun initRecyclerView() {
        getHistory()
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this, chatList)
        recyclerView.adapter = chatAdapter
        recyclerView.itemAnimator = null
        recyclerView.scrollToPosition(chatList.size - 1)
    }

    private fun initConversationList() {
        conversationList.layoutManager = LinearLayoutManager(this)
        conversationListAdapter = ConversationListAdapter(this, conversationsList)
        conversationListAdapter.setOnItemClickListener {
            conversationListAdapter.setSelectedItem(it)
        }
        conversationList.adapter = conversationListAdapter
    }

    private fun getHistory() {
        val dbHelper = DatabaseHelper(this)
        val list: ArrayList<ChatItem> = dbHelper.allMessages
        for (index in 0 until list.size) {
            var chatItem = list[index].clone() as ChatItem
            chatItem.chatType = ChatItem.ChatType.QUESTION
            chatList.add(chatItem)
            chatItem = list[index].clone() as ChatItem
            chatItem.chatType = ChatItem.ChatType.REPLY
            chatList.add(chatItem)
            chatList.add(ChatItem(ChatItem.ChatType.EMPTY))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val userAgent =
            "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.57 Mobile Safari/537.36"
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.userAgentString = userAgent


        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                splashBinding.root.visibility = View.GONE

                view?.evaluateJavascript(
                    ScriptUtils.checkLoading()
                ) { result ->
                    if (result == "null") {
                        webBinding.root.visibility = View.VISIBLE
                        checkLoading()
                    } else {
                        loadFinished()
                    }
                }
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                checkHeadLoaded()
                super.onPageStarted(view, url, favicon)
            }
        }

        webView.addJavascriptInterface(JSInterface(object : FetchListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSession(session: Session) {
                updateUiWithSession(session)
            }

            override fun onConversationList(conversations: Conversations) {
                runOnUiThread {
                    conversationsList = conversations.items
                    conversationListAdapter.updateList(conversationsList)
                }
            }

        }), "Android")

        webView.loadUrl("https://chat.openai.com/chat")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUiWithSession(session: Session) {
        runOnUiThread {
            userEmail.text = session.user?.id
            userName.text = session.user?.name

            Glide.with(applicationContext).load(session.user?.picture).into(userImageHeader)
            Glide.with(applicationContext).load(session.user?.picture)
                .transform(RoundedCorners(15)).into(userImage)

            Preferences(sharedPreferences).userImage = session.user?.picture.toString()

            chatAdapter.notifyDataSetChanged()
        }
    }

    private fun scrollRecyclerViewToBottomIfNeeded(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val lastVisiblePosition = layoutManager!!.findLastVisibleItemPosition()
        val itemCount = adapter.itemCount

        // Check if the user is at the full bottom of the RecyclerView
        val isAtFullBottom =
            (lastVisiblePosition == (itemCount - 1)) && (layoutManager.findViewByPosition(
                lastVisiblePosition
            )!!
                .bottom <= recyclerView.height)

        // Scroll to the bottom if the user is at the full bottom
        if (isAtFullBottom) {
            recyclerView.scrollToPosition(itemCount - 1)
        }
    }

    private fun getReply() {
        if (checkReplying()) {
            getReplyMessage()
            Handler(Looper.getMainLooper()).postDelayed({
                runOnUiThread { getReply() }
            }, 500)
        } else {
            finishedReplying()
        }
    }

    private fun getReplyMessage() {
        webView.evaluateJavascript(
            ScriptUtils.getCurrentResponse()
        ) { result ->
            var message = result.substring(1, result.length - 1)
            message = message.replace("\\u003C", "<").replace("\\\"", "\"")
            chatList[chatList.size - 2].setMessage(JsoupUtils.scrapMessage(message))
            chatAdapter.notifyItemChanged(chatList.size - 2)
            scrollRecyclerViewToBottomIfNeeded(recyclerView, chatAdapter)
        }
    }

    private fun finishedReplying() {
        send.isEnabled = true
        typingIndicator.visibility = View.GONE
        val calendar = Calendar.getInstance()
        chatList[chatList.size - 2].responseFinishTime = calendar.timeInMillis
        chatList[chatList.size - 2].isTypingFinished = true
        chatAdapter.notifyItemChanged(chatList.size - 2)
        setSubtitle("online")
        addMessageToDB()
    }

    private fun addMessageToDB() {
        val dbHelper = DatabaseHelper(this)
        val question = chatList[chatList.size - 3].question
        val answer = Gson().toJson(chatList[chatList.size - 2].messages)
        val answerHtml = chatList[chatList.size - 2].answerHtml
        val time = chatList[chatList.size - 2].time
        val responseTime = chatList[chatList.size - 2].responseFinishTime

        dbHelper.insertMessage(question, answer, answerHtml, time, responseTime)
    }

    private fun checkReplying(): Boolean {
        webView.evaluateJavascript(
            ScriptUtils.checkReplaying()
        ) { result ->
            replaying = result == "true"
        }
        return replaying
    }

    private fun loadFinished() {
        splashBinding.root.visibility = View.GONE
        webBinding.root.visibility = View.INVISIBLE
        chatBinding.root.visibility = View.VISIBLE
        subtitleContainer.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        setSubtitle("online")
    }

    private fun setSubtitle(text: String) {
        subtitle.text = text
    }

    private fun checkLoading() {
        webView.evaluateJavascript(
            ScriptUtils.checkLoading()
        ) { result ->
            if (result == "null") {
                checkLoading()
            } else {
                loadFinished()
            }
        }
    }

    private fun checkHeadLoaded() {
        webView.evaluateJavascript(
            ScriptUtils.checkHasHeader()
        ) { result ->
            if (result == "null") {
                checkHeadLoaded()
            } else {
                injectInterceptor()
            }
        }
    }

    private fun injectInterceptor() {
        webView.evaluateJavascript(
            ScriptUtils.interceptFetch()
        ) {
        }
    }

    private fun initViews() {
        webBinding = activityMainBinding.webLayout
        chatBinding = activityMainBinding.chatLayout
        splashBinding = activityMainBinding.splashLayout

        webView = webBinding.webView
        recyclerView = chatBinding.recyclerView
        conversationList = activityMainBinding.conversationList
        send = chatBinding.send
        message = chatBinding.message
        toolbar = activityMainBinding.toolbar
        drawerLayout = activityMainBinding.drawer
        toolbarBinding = activityMainBinding.homeToolbar
        subtitle = toolbarBinding.subtitle
        subtitleContainer = toolbarBinding.subtitleContainer
        typingIndicator = toolbarBinding.typingIndicator
        userImage = chatBinding.userImage

        userImageHeader = activityMainBinding.userIconHeader
        userName = activityMainBinding.name
        userEmail = activityMainBinding.email

        Glide.with(applicationContext)
            .load(Preferences(sharedPreferences).userImage)
            .transform(RoundedCorners(15))
            .into(userImage)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }
}