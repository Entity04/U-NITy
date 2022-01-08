package com.entity.unity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.R
import com.entity.unity.model.User

class ChatUserAdapter(val context: Context,val userList:ArrayList<User>):RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder>() {

    class ChatUserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvName :TextView = itemView.findViewById(R.id.tvChatUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.user_layout,parent,false)
        return ChatUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val currentUser=userList[position]
        holder.tvName.text=currentUser.name
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}