package com.example.socialframe.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.R
import com.example.socialframe.classes.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChatAdapter(var arr: MutableList<Chat>):RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var chatimage=itemView.findViewById<ImageView>(R.id.chatimage)
        var chatname=itemView.findViewById<TextView>(R.id.chatname)
        var chatmessage=itemView.findViewById<TextView>(R.id.chatmessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(OpenModel.mycontext).inflate(R.layout.layout_chatbox,parent,false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        CoroutineScope(Dispatchers.Main).launch { async {
            Glide.with(OpenModel.mycontext!!).load(arr[position].ChatUser.MyPICUrl)
                .placeholder(R.drawable.empty_profile).into(holder.chatimage)
        }}
        holder.chatname.setText(arr[position].ChatUser.Name)
        holder.chatmessage.setText(arr[position].MyMessage.message)
        holder.itemView.setOnClickListener(){
            OpenModel.MessageReciever.value=arr[position].ChatUser
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}