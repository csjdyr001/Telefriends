package com.cfks.telefriends.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cfks.telefriends.R;
import com.cfks.telefriends.db.Message;
import com.cfks.telefriends.recycle_views.BasicViewMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kyrylo Avramenko on 6/29/2018.
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MessageListAdapter";


    private List<Message> itemsMessages;
    private HashMap<Long, Message> batchSelected = new HashMap();

    public MessageListAdapter() {

    }

    public void setItemsMessages(List<Message> itemsMessages) {
        this.itemsMessages = itemsMessages;
        notifyDataSetChanged();
    }


    public void toggleSelection(Message message) {

        final Long key = message.getIdMessage();
        if (batchSelected.containsKey(key)) {
            //Log.d(TAG, "toggleSelection: clear messsage: " + message.text);
            batchSelected.remove(key);
            message.clearSelection();
        } else {
            //Log.d(TAG, "toggleSelection: add batchSelected lastMessageLive: " + message.text);
            batchSelected.put(key, message);
            message.makeSelection();
        }
    }

    public void clearSelectedItems() {
        //Log.d(TAG, "clearSelectedItems: ");

        for (Long key : batchSelected.keySet()) {
            batchSelected.get(key).clearSelection();
        }

        batchSelected.clear();
    }


    public List<Message> getItemsMessages() {
        return itemsMessages;
    }

    public int getSizeOfSelectedItems() {
        return batchSelected == null ? 0 : batchSelected.size();
    }

    public Set<Message> getSelectedItems() {
        return new HashSet<>(batchSelected.values());
    }

    public Message getItem(int position) {
        return itemsMessages.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @Message.MessageType int viewType) {

        //get proper id of resource layout
//        //Log.d(TAG, "onCreateViewHolder: ");
        @LayoutRes int res = viewType == Message.MessageType.RECEIVED ? R.layout.item_message_received : R.layout.item_message_sent;

        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        return new BasicViewMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = itemsMessages.get(position);

        if (message == null) {
            //Log.e(TAG, "onBindViewHolder: dont find item on position: " + position);
            return;
        }

        BasicViewMessage bvm = (BasicViewMessage) holder;


        message = bvm.bind(message);
        if (batchSelected.containsKey(message.getIdMessage())) {
            batchSelected.put(message.getIdMessage(), message);
            message.makeSelection();
//            //Log.d(TAG, "onBindViewHolder: Message " + "__ " + lastMessageLive.text + " ___ is SELECTED");
        } else {
//            //Log.d(TAG, "onBindViewHolder: Message " + "__ " + lastMessageLive.toString() + " ___ is NOT SELECTED with id: ");
            message.clearSelection();
        }

    }

    @Override
    public int getItemCount() {
        return itemsMessages == null ? 0 : itemsMessages.size();
    }

    @Override
    @Message.MessageType
    public int getItemViewType(int position) {

        Message msg = itemsMessages.get(position);
        return msg.messageType;
    }


}
