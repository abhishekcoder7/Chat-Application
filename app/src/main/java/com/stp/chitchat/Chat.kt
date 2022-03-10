package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stp.chitchat.databinding.FragmentChatBinding
import com.stp.chitchat.databinding.ReceivedMessageLayoutBinding
import com.stp.chitchat.databinding.SentMessageLayoutBinding

class Chat : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding

    val args: ChatArgs by navArgs()

    private var hisId: String? = null
    private var hisName: String? = null
    private var chatId: String? = null
    private lateinit var appUtil: AppUtil
    private lateinit var myId: String
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        appUtil = AppUtil()
        myId = appUtil.getUid()!!

        hisId = args.uniqueId
        hisName = args.userName

        binding!!.hisName.text = hisName

        binding!!.sendBtn.setOnClickListener {
            val message = binding!!.typeBox.text.toString()
            if(message.isEmpty())
                Toast.makeText(requireContext(), "Write something...", Toast.LENGTH_SHORT).show()
            else sendMessage(message)
        }

        if(chatId == null) checkChat(hisId!!)


        return binding?.root
    }

    private fun checkChat(hisId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        val query = databaseReference.orderByChild("member").equalTo(hisId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnap in snapshot.children) {
                        val member = dataSnap.child("member").value.toString()
                        if (hisId == member) {
                            chatId = dataSnap.key
                            readMessage(chatId!!)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun createChat(message: String) {
        var databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        chatId = databaseReference.push().key
        val myChatModel = ChatModel(chatId!!, message, System.currentTimeMillis().toString(), hisId!!)
        databaseReference.child(chatId!!).setValue(myChatModel)

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!)
        val hisChatModel = ChatModel(chatId!!, message, System.currentTimeMillis().toString(), myId)
        databaseReference.child(chatId!!).setValue(hisChatModel)

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)
        val messageModel = MessageModel(myId, hisId!!, message, type = "text")
        databaseReference.push().setValue(messageModel)


    }

    private fun sendMessage(message: String) {
        if(chatId == null) createChat(message)
        else {
            var databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)
            val messageModel = MessageModel(myId, hisId!!, message, type = "text")
            databaseReference.push().setValue(messageModel)

            val map: MutableMap <String, Any> = HashMap()
            map["lastMessage"] = message
            map["date"] = System.currentTimeMillis().toString()
            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId).child(chatId!!)
            databaseReference.updateChildren(map)

            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!).child(chatId!!)
            databaseReference.updateChildren(map)
        }
    }

    private fun readMessage(chatId: String) {
        val query = FirebaseDatabase.getInstance().getReference("Chat").child(chatId)
        val firebaseRecyclerOptions = FirebaseRecyclerOptions
                .Builder<MessageModel>()
                .setLifecycleOwner(this)
                .setQuery(query, MessageModel::class.java)
                .build()
        query.keepSynced(true)

        firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(firebaseRecyclerOptions) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                var viewDataBinding: ViewDataBinding? = null

                if(viewType == 0)
                    viewDataBinding = SentMessageLayoutBinding
                            .inflate(LayoutInflater.from(parent.context),
                                    parent,
                                    false)

                if(viewType == 1)
                    viewDataBinding = ReceivedMessageLayoutBinding
                            .inflate(LayoutInflater.from(parent.context),
                                    parent,
                                    false)

                return MessageViewHolder(viewDataBinding!!)
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, messageModel: MessageModel) {
                if(getItemViewType(position) == 0){
                    holder.viewDataBinding.setVariable(BR.message, messageModel)
                }
                if(getItemViewType(position) == 1) {
                    holder.viewDataBinding.setVariable(BR.message, messageModel)
                }
            }

            //this function is to check, if the message is sent by me or someone else
            //according to this, it will decide the side of the message
            override fun getItemViewType(position: Int): Int {
                val messageModel = getItem(position)
                return if(messageModel.senderId == myId)
                    0
                else
                    1
            }
        }

        binding?.messagesRecyclerView?.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding!!.messagesRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter!!.startListening()
    }

    class MessageViewHolder(val viewDataBinding: ViewDataBinding) :
            RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onPause() {
        super.onPause()
        if(firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter!!.stopListening()
    }

    fun onBackPressed() {
        val action = ChatDirections.actionChatToDashboard()
        findNavController().navigate(action)
    }
}