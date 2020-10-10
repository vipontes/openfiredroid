package br.net.easify.openfiredroid.view

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.database.model.Chat
import br.net.easify.openfiredroid.databinding.FragmentMessageBinding
import br.net.easify.openfiredroid.util.hideKeyboard
import br.net.easify.openfiredroid.view.adapters.MessageAdapter
import br.net.easify.openfiredroid.viewmodel.MessageViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_message.*


class MessageFragment : Fragment(), MessageAdapter.OnItemClick {
    private var contactId = 0L

    private lateinit var chat: Chat
    private lateinit var viewModel: MessageViewModel
    private lateinit var dataBinding: FragmentMessageBinding
    private lateinit var messageAdapter: MessageAdapter

    private val messageObserver = Observer<Chat> { data: Chat ->
        data.let {
            this.chat = it
            dataBinding.chat = this.chat
        }
    }

    private val messagesObserver = Observer<List<Chat>> { data: List<Chat> ->
        data.let {
            messageAdapter.updateMessages(it)
            dataBinding.messages.scrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        arguments?.let {
            this.contactId = MessageFragmentArgs.fromBundle(it).contactId
        }

        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_message,
            container,
            false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageAdapter = MessageAdapter(this, arrayListOf(), requireActivity())

        dataBinding.messages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    messageAdapter.removeItem(viewHolder.adapterPosition)
                }

                override fun onChildDraw(
                    c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
                ) {
                    RecyclerViewSwipeDecorator.Builder(
                        c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                        .addBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorRed
                            )
                        )
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .addSwipeRightLabel(context!!.getString(R.string.delete))
                        .setSwipeRightLabelColor(R.color.colorLight)
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                }
            }

        val itemTouchHelper =
            ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(messages)

        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        viewModel.message.observe(viewLifecycleOwner, messageObserver)
        viewModel.chatMessages.observe(viewLifecycleOwner, messagesObserver)
        viewModel.loadChat(this.contactId)
        viewModel.loadContactInfo(this.contactId)

        dataBinding.sendMessage.setOnClickListener(View.OnClickListener {
            viewModel.sendMessage()
            hideKeyboard()
        })
    }

    override fun onItemDelete(chatId: Long) {
        viewModel.deleteMessage(chatId)
    }
}