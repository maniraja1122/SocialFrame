package com.example.socialframe.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialframe.R
import com.example.socialframe.classes.MessageModel
import java.text.DateFormat


class MessageAdapter(context: Context, list: List<MessageModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context: Context
    var list: List<MessageModel>

    private inner class MessageInViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView
        var dateTV: TextView
        fun bind(position: Int) {
            val messageModel = list[position]
            messageTV.text = messageModel.message
            dateTV.setText(
                DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime)
            )
        }

        init {
            messageTV = itemView.findViewById(R.id.message_text)
            dateTV = itemView.findViewById(R.id.date_text)
        }
    }

    private inner class MessageOutViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView
        var dateTV: TextView
        fun bind(position: Int) {
            val messageModel = list[position]
            messageTV.text = messageModel.message
            dateTV.setText(
                DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime)
            )
        }

        init {
            messageTV = itemView.findViewById(R.id.message_text)
            dateTV = itemView.findViewById(R.id.date_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_TYPE_IN) {
            MessageInViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_text_in, parent, false)
            )
        } else MessageOutViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_text_out, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].messageType === MESSAGE_TYPE_IN) {
            (holder as MessageInViewHolder).bind(position)
        } else {
            (holder as MessageOutViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].messageType
    }

    companion object {
        const val MESSAGE_TYPE_IN = 2
        const val MESSAGE_TYPE_OUT = 1
    }

    init { // you can pass other parameters in constructor
        this.context = context
        this.list = list
    }
}