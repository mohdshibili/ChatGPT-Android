package com.codeware.chatgpt.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codeware.chatgpt.R;
import com.codeware.chatgpt.model.ChatItem;
import com.codeware.chatgpt.model.MessageItem;
import com.codeware.chatgpt.preference.Preferences;
import com.codeware.chatgpt.utils.SpanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatItem> items;

    public ChatAdapter(Context context, ArrayList<ChatItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new MessageViewHolder(view);
            return viewHolder;
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false);
        viewHolder = new EmptyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // set the data in items
        if (getItemViewType(position) == 1) {
            ((MessageViewHolder) holder).message.setMovementMethod(LinkMovementMethod.getInstance());

            Editable editable;

            if (items.get(position).getChatType() == ChatItem.ChatType.REPLY) {
                StringBuilder sb = new StringBuilder();
                ArrayList<MessageItem> messageItems = items.get(position).getMessages();

                for (int index = 0; index < Objects.requireNonNull(messageItems).size(); index++) {

                    MessageItem messageItem = messageItems.get(index);

                    sb.append(messageItem.getMessage());

                    if (index != messageItems.size() - 1)
                        sb.append("\n\n");
                }

                editable = new SpannableStringBuilder(sb);
                SpanUtils.setBoldSpan(editable);
                SpanUtils.setCodeBlockSpanText(((MessageViewHolder) holder).message, editable, 45);
            } else {
                editable = new SpannableStringBuilder(items.get(position).getQuestion());
            }

            ((MessageViewHolder) holder).message.setText(editable, TextView.BufferType.SPANNABLE);

            ((MessageViewHolder) holder).time.setText(getTime(items.get(position).getTime()));

            if (items.get(position).getChatType() == ChatItem.ChatType.REPLY) {
                ((MessageViewHolder) holder).root.setBackgroundColor(Color.parseColor("#FF444654"));
                Glide.with(context).load(R.drawable.chatgpt_icon).into(((MessageViewHolder) holder).icon);

                if (items.get(position).isTypingFinished()) {
                    ((MessageViewHolder) holder).responseTime.setVisibility(View.VISIBLE);
                    ((MessageViewHolder) holder).responseTime.setText(getResponseTime(items.get(position).getTime(), items.get(position).getResponseFinishTime()));
                } else {
                    ((MessageViewHolder) holder).responseTime.setVisibility(View.GONE);
                }
            } else {
                ((MessageViewHolder) holder).root.setBackgroundColor(Color.TRANSPARENT);
                SharedPreferences sharedPreferences = context.getSharedPreferences("com.codeware.chatgpt", Context.MODE_PRIVATE);
                Glide.with(context).load(new Preferences(sharedPreferences).getUserImage()).transform(new RoundedCorners(20)).into(((MessageViewHolder) holder).icon);
                ((MessageViewHolder) holder).responseTime.setVisibility(View.GONE);
            }

            ((MessageViewHolder) holder).copy.setOnClickListener(copy -> {
                ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", ((MessageViewHolder) holder).message.getText().toString()));
                showMessage(context);
            });
        }
    }

    private void showMessage(Context context) {
        Toast.makeText(context, "Content copied", Toast.LENGTH_SHORT).show();
    }

    private String getTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());
    }

    private String getResponseTime(long time, long responseFinishedTime) {

        long millis = responseFinishedTime - time; // replace this with your actual milliseconds value
        int seconds = (int) (millis / 1000);
        if (seconds < 60) {
            // If less than a minute, return seconds
            return seconds + " s";
        } else {
            // If a minute or more, return minutes and seconds
            int minutes = seconds / 60;
            seconds = seconds % 60;
            return minutes + "m" + seconds + "s"; // concatenate minutes and seconds into a single integer
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getChatType() == ChatItem.ChatType.EMPTY)
            return 0;

        return 1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MessageViewHolder extends ViewHolder {
        TextView message, time, responseTime;
        ImageView icon, copy;
        LinearLayout root;

        public MessageViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            message = itemView.findViewById(R.id.message);
            root = itemView.findViewById(R.id.root_layout);
            icon = itemView.findViewById(R.id.icon);
            time = itemView.findViewById(R.id.time);
            responseTime = itemView.findViewById(R.id.response_time);
            copy = itemView.findViewById(R.id.copy);
        }
    }

    public static class EmptyViewHolder extends ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<ChatItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }
}

