package br.net.easify.openfiredroid.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.database.model.Chat
import br.net.easify.openfiredroid.databinding.MyMessageHolderBinding
import br.net.easify.openfiredroid.databinding.TheirMessageHolderBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class MessageAdapter(private var listener: OnItemClick,
                     private var chatMessages: ArrayList<Chat>,
                     private var parentActivity: Activity)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val myMessageView = 1
    private val theirMessageView = 2

    interface OnItemClick {
        fun onItemDelete(chatId: Long)
    }

    class MyMessageViewHolder(var view: MyMessageHolderBinding) :
        RecyclerView.ViewHolder(view.root)

    class TheirMessageViewHolder(var view: TheirMessageHolderBinding) :
        RecyclerView.ViewHolder(view.root)

    fun updateMessages(newData: List<Chat>) {
        chatMessages.clear()
        chatMessages.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = chatMessages[position]
        return if (message.owner) myMessageView else theirMessageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            myMessageView -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = DataBindingUtil.inflate<MyMessageHolderBinding>(
                    inflater,
                    R.layout.my_message_holder,
                    parent,
                    false
                )
                return MessageAdapter.MyMessageViewHolder(view)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = DataBindingUtil.inflate<TheirMessageHolderBinding>(
                    inflater,
                    R.layout.their_message_holder,
                    parent,
                    false
                )
                return MessageAdapter.TheirMessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]

        when (holder.itemViewType) {
            myMessageView -> {
                val viewHolder = holder as MyMessageViewHolder
                viewHolder.view.chatMessage = message
            }
            else -> {
                val viewHolder = holder as TheirMessageViewHolder
                viewHolder.view.chatMessage = message
            }
        }
    }

    override fun getItemCount() = chatMessages.size

    fun removeItem(position: Int) {
        Snackbar.make(
            parentActivity.findViewById(R.id.frame_layout),
            parentActivity.getString(R.string.are_you_sure),
            2500
        )
            .setAction(parentActivity.getString(R.string.delete)) {
                val chatMessage = chatMessages[position]
                listener.onItemDelete(chatMessage.chat_id!!)
            }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                override fun onDismissed(
                    transientBottomBar: Snackbar?,
                    event: Int
                ) {
                    super.onDismissed(transientBottomBar, event)
                    notifyItemChanged(position)
                }
            }).show()
    }
}