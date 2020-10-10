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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.database.model.Contact
import br.net.easify.openfiredroid.databinding.FragmentContactsBinding
import br.net.easify.openfiredroid.view.adapters.ContactsAdapter
import br.net.easify.openfiredroid.viewmodel.ContactsViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment(), ContactsAdapter.OnItemClick,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: ContactsViewModel
    private lateinit var dataBinding: FragmentContactsBinding
    private lateinit var contactsAdapter: ContactsAdapter

    private val contactsObserver = Observer<List<Contact>> { data: List<Contact> ->
        data.let {
            contactsAdapter.updateContacts(it)
            dataBinding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_contacts,
            container,
            false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsAdapter =
            ContactsAdapter(this, arrayListOf(), requireActivity())

        dataBinding.contacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
        }

        dataBinding.swipeRefreshLayout.setOnRefreshListener(this)

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
                    contactsAdapter.removeItem(viewHolder.adapterPosition)
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
        itemTouchHelper.attachToRecyclerView(contacts)

        viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        viewModel.contacts.observe(viewLifecycleOwner, contactsObserver)
    }

    override fun onItemClick(contact: Contact) {
        val action = ContactsFragmentDirections.actionViewMessage()
        Navigation.findNavController(contacts).navigate(action)
    }

    override fun onItemDelete(contactId: Long) {

    }

    override fun onRefresh() {
        viewModel.loadContacts()
    }
}