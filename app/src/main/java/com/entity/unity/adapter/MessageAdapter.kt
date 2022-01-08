package com.entity.unity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.model.MessageData
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<MessageData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve_layout, parent, false)
            return RecieveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
            return SentViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message


        } else {
            val viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message

        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT

        } else {
            return ITEM_RECEIVE

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}


class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val sentMessage :TextView= itemView.findViewById(R.id.txt_sent_message)

}

class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recieveMessage :TextView= itemView.findViewById(R.id.txt_recieve_message)

}