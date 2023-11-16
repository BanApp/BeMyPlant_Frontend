package com.example.bemyplant.adapter

import com.example.bemyplant.data.ChatMsg
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.bemyplant.R
class MessageAdapter(
    private val itemList: ArrayList<ChatMsg>,
    private val currentUser: String,
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_OTHER = 2
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContent: TextView = itemView.findViewById(R.id.messageContent)
        private val sendTime: TextView = itemView.findViewById(R.id.sendTime)

        fun bindMessage(message: ChatMsg) {
            messageContent.text = message.content
            sendTime.text = message.sendTime

            val msgContents = message.content
            val tag3 = "MessageContents"
            Log.d(tag3, "$msgContents")


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val Tag2 = "MessageAdapter"
        val layoutResId = if (viewType == VIEW_TYPE_ME) {
            R.layout.msg_recycler_me
        } else {
            R.layout.msg_recycler_other
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        Log.d(Tag2, "$currentUser $viewType")
        return MessageViewHolder(view)
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = itemList[position]
        holder.bindMessage(message)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    //itemList[position].senderId == currentUser에서 itemList[position].senderId가 뭘 의미하는지...
    // currentUser == "jo" 대신 쓰는 코드!
    override fun getItemViewType(position: Int): Int {
        return if (currentUser == "jo" ) {
            VIEW_TYPE_ME
        } else {
            VIEW_TYPE_OTHER
        }

    }
}
