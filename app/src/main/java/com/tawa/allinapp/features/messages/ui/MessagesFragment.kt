package com.tawa.allinapp.features.messages.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBIncomingMessagesManager
import com.quickblox.chat.QBSystemMessagesManager
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.core.request.QBRequestGetBuilder
import com.quickblox.users.model.QBUser
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.extensions.visible
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentMessagesBinding
import com.tawa.allinapp.features.messages.MessagesViewModel
import com.tawa.allinapp.models.QuickBloxUser
import com.tawa.allinapp.tools.*
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MessagesFragment @Inject constructor() : BaseFragment() {

    private lateinit var binding: FragmentMessagesBinding
    //private var listUsers: ArrayList<String> = ArrayList()
    private var listUsers: ArrayList<QuickBloxUser> = ArrayList()
    private var listSelectedUsers: ArrayList<QuickBloxUser> = ArrayList()
    //private var backUpListUsers : ArrayList<String> = ArrayList()
    private var backUpListUsers : ArrayList<QuickBloxUser> = ArrayList()
    @Inject lateinit var contactUserAdapter: ContactAdapter
    @Inject lateinit var selectedUserAdapter : SelectedUserAdapter
    private lateinit var systemMessagesManager: QBSystemMessagesManager
    private lateinit var incomingMessagesManager: QBIncomingMessagesManager


    private lateinit var messageViewModel : MessagesViewModel


    //QuickBlox
    private lateinit var chatConnectionListener: ConnectionListener


    companion object{
        val TAG = "MessageFragment"
        const val DIALOGS_PER_PAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater)
        messageViewModel = viewModel(viewModelFactory){ }

        initViews()
        loginQB()
        changeViewsFragment()
        return binding.root
    }
    override fun changeViewsFragment() {

    }

    private fun loginQB() {
        val user = QBUser().apply { login = "lbruno";password = "12345678";}

        ChatTool.login(user, object: QBEntityCallback<QBUser>{
            override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                Log.d(TAG, "Login Successful")
                loginChat(p0)
            }
            override fun onError(p0: QBResponseException?) { p0?.message?.let { Log.d(TAG, p0.toString())}}
        })
    }
    private fun loginChat(qbUser: QBUser?) {
        Log.d("qbUser",qbUser.toString())
        qbUser?.apply { password = "12345678" }
        QBChatService.getInstance().login(qbUser, object : QBEntityCallback<Void> {
            override fun onSuccess(p0: Void?, p1: Bundle?) {
                initConnectionListener()
            }

            override fun onError(p0: QBResponseException?) {
                Log.d(TAG, p0.toString())
            }

        })
    }
    private fun initConnectionListener() {
        chatConnectionListener = object : ConnectionListener {
            override fun connected(p0: XMPPConnection?) {
                Log.d(TAG, "Connectado")
            }

            override fun authenticated(p0: XMPPConnection?, p1: Boolean) {
                Log.d(TAG, "Autentificado")
            }

            override fun connectionClosed() {
                Log.d(TAG, "Connexión cerrada")
            }

            override fun connectionClosedOnError(p0: Exception?) {
                Log.d(TAG, "Conexión cerrada")
            }

            override fun reconnectionSuccessful() {
                Log.d(TAG, "Reconexión")
            }

            override fun reconnectingIn(p0: Int) {
                Log.d(TAG, "Conentado")
            }

            override fun reconnectionFailed(p0: Exception?) {
                Log.d(TAG, "Connexión Fallida")
            }

        }
       /*chatConnectionListener = object : VerboseQbChatConnectionListener(binding.root) {
            override fun reconnectionSuccessful() {
                super.reconnectionSuccessful()
                loadDialogFromQb(false, clearDialogHolder = true)
            }
        }*/
        //temportal
        QBChatService.getInstance().addConnectionListener(chatConnectionListener)
        registerQbChatListeners()
    }

    private fun registerQbChatListeners() {
        ChatHelper.addConnectionListener(chatConnectionListener)
        try {
            systemMessagesManager = QBChatService.getInstance().systemMessagesManager
            incomingMessagesManager = QBChatService.getInstance().incomingMessagesManager
        } catch (e: Exception) {
            Log.d(TAG, "Can not get SystemMessagesManager. Need relogin. " + e.message)
            //reloginToChat()
            return
        }
    }

    private fun loadDialogFromQb(udpate: Boolean, clearDialogHolder: Boolean){
        val requestBuilder = QBRequestGetBuilder().apply{
            limit = DIALOGS_PER_PAGE
            skip  = if(clearDialogHolder) 0 else QbDialogHolder.dialogsMap.size
        }
        ChatHelper.getDialogs(requestBuilder, object : QBEntityCallback<ArrayList<QBChatDialog>>{
            override fun onSuccess(dialogs: ArrayList<QBChatDialog>?, p1: Bundle?) {
                QbDialogHolder.addDialogs(dialogs!!.toList())
                Log.d("dialogs", dialogs.toString())
                ChatHelper.join(dialogs)

            }

            override fun onError(p0: QBResponseException?) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun enableSearchMode(){
        binding.tvHeaderSubtitleMessage.invisible()
        binding.tvHeaderTitleMessage.invisible()
        binding.ivBackMessage.invisible()
        binding.ivSearchMessage.invisible()
        binding.svMessage.visible()
    }
    private fun disableSearchMode(){
        binding.tvHeaderSubtitleMessage.visible()
        binding.tvHeaderTitleMessage.visible()
        binding.ivBackMessage.visible()
        binding.ivSearchMessage.visible()
        binding.svMessage.invisible()
    }

    private fun initViews(){
        ResourcesCompat.getDrawable(resources,R.drawable.ic_search_user,null)?.let { drawable ->
            val wrapped =  DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrapped, Color.WHITE)
            DrawableCompat.setTintMode(wrapped, PorterDuff.Mode.SRC_IN)
            binding.svMessage.setCompoundDrawablesWithIntrinsicBounds(wrapped,null,null,null)
        }
        binding.ivSearchMessage.setColorFilter(Color.WHITE)
        binding.ivSearchMessage.setOnClickListener { enableSearchMode() }
        binding.lyContHeaderMessage.setOnClickListener { disableSearchMode() }
/*
        listUsers.add("Luis Bruno")
        listUsers.add("Diana Ninamango")
        listUsers.add("Rodrigo Quinchu")
        listUsers.add("Edgar Espinoza")
        listUsers.add("Cristina Rosenvinge")
        listUsers.add("Enqiue Bunbury")
        listUsers.add("David Gilmour")
        listUsers.add("Guillermo Puertas")*/

        listUsers.add(QuickBloxUser("131311071","Luis Bruno"))
        listUsers.add(QuickBloxUser("131311080","Diana Nina"))
        listUsers.add(QuickBloxUser("131333105","Cristina Rosenvinge"))

        backUpListUsers.addAll(listUsers)
        contactUserAdapter.setData(listUsers)
        contactUserAdapter.clickListener = {
            if(!listSelectedUsers.contains(it)){
                listSelectedUsers.add(it)
                binding.rvContactsSelected.visible()
                binding.vBaseline.visible()
                binding.fbConfirmNewGroup.visible()
                selectedUserAdapter.setData(listSelectedUsers)
            }else{
                notify(requireActivity(),R.string.already_added)
            }
        }
        binding.rvContactsNewGroup.adapter = contactUserAdapter
        binding.rvContactsNewGroup.layoutManager = LinearLayoutManager(requireContext())
        binding.svMessage.addTextChangedListener(object :  TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) { filter(s.toString()) }

        })
        selectedUserAdapter.setData(listSelectedUsers)
        selectedUserAdapter.listenerDeleteUser = { s: QuickBloxUser, i: Int ->
            listSelectedUsers.remove(s)
            selectedUserAdapter.setData(listSelectedUsers)
            if(listSelectedUsers.isEmpty()){
                binding.rvContactsSelected.invisible()
                binding.vBaseline.invisible()
                binding.fbConfirmNewGroup.invisible()
            }
        }
        binding.rvContactsSelected.invisible()
        binding.vBaseline.invisible()
        binding.rvContactsSelected.adapter = selectedUserAdapter
        binding.rvContactsSelected.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        binding.fbConfirmNewGroup.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("listSelected", listSelectedUsers)
            if(selectedUserAdapter.selectedUsers.size > 1){
                findNavController().navigate(R.id.action_navigation_messages_to_navigation_confirm_new_group,bundle)
            }
        }

        binding.ivBackMessage.setOnClickListener { activity?.onBackPressed() }
    }
    private fun filter(text: String){
        val listFiltered = ArrayList<QuickBloxUser>()
        backUpListUsers.let{ bkpList ->
            for(user in bkpList){
                if(user.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))){
                    listFiltered.add(user)
                }
            }
        }.also { contactUserAdapter.setData(listFiltered) }
    }

}