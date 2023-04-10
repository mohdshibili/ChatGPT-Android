package com.codeware.chatgpt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.codeware.chatgpt.R;
import com.codeware.chatgpt.model.Items;

import java.util.ArrayList;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Items> arrayList;
    public OnItemClickListener onItemClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    private int selectedItem = 0;

    public ConversationListAdapter(Context context, ArrayList<Items> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public ConversationListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conversation_list_item, viewGroup, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.conversationTitle.setText(arrayList.get(position).getTitle());

        viewHolder.conversationRoot.setSelected(position == selectedItem);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout conversationRoot;
        TextView conversationTitle;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            conversationRoot = itemView.findViewById(R.id.conversation_root);
            conversationRoot.setSelected(false);
            conversationTitle = itemView.findViewById(R.id.conversation_title);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Items> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}