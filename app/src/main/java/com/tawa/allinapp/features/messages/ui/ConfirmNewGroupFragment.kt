package com.tawa.allinapp.features.messages.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.QBSystemMessagesManager
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.model.QBChatMessage
import com.quickblox.chat.model.QBDialogType
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.model.QBUser
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentConfirmNewGroupBinding
import com.tawa.allinapp.models.QuickBloxUser
import com.tawa.allinapp.tools.QbDialogHolder
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smackx.muc.DiscussionHistory
import java.io.File
import javax.inject.Inject


class ConfirmNewGroupFragment
@Inject constructor(): BaseFragment() {

    private lateinit var binding: FragmentConfirmNewGroupBinding

    @Inject lateinit var selectedUserAdapter : SelectedUserAdapter

    companion object{
        const val REQUEST_CODE = 200
        const val PROPERTY_OCCUPANTS_IDS = "current_occupant_ids"
        const val PROPERTY_DIALOG_TYPE = "type"
        const val PROPERTY_DIALOG_NAME = "room_name"
        const val PROPERTY_NOTIFICATION_TYPE = "notification_type"
        const val CREATING_DIALOG = "1"
        const val OCCUPANTS_ADDED = "2"
        const val OCCUPANT_LEFT = "3"
        const val PROPERTY_NEW_OCCUPANTS_IDS = "new_occupants_ids"
    }

    private lateinit var systemMessagesManager: QBSystemMessagesManager

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        binding.ibSetImageGroup.setImageURI(uri)
        Log.d("asd", Uri.parse(uri.toString()).toString())
        binding.ibSetImageGroup.setBackgroundResource(0)
        /*Glide.with(requireActivity())
            .load(Uri.parse(uri.toString()))
            .circleCrop()
            .into(binding.ibSetImageGroup)*/
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentConfirmNewGroupBinding.inflate(inflater)
        changeViewsFragment()

        return binding.root
    }
    override fun changeViewsFragment() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    @SuppressLint("SetTextI18n")
    private fun initViews() {
        val selectedUsers = arguments?.getParcelableArrayList<QuickBloxUser>("listSelected")
        Log.d("asd", selectedUsers.toString())
        selectedUserAdapter.setData(selectedUsers!!.toList())
        binding.rvSelectedUsersResume.adapter = selectedUserAdapter
        binding.rvSelectedUsersResume.layoutManager = GridLayoutManager(requireContext(), 4)

        val title = binding.tvTitleParticipant.text.toString() + " (" + selectedUsers.size + ")"
        binding.tvTitleParticipant.text = title

        binding.ibSetImageGroup.setOnClickListener {
            try {
                responseLauncher.launch("image/*")
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace();
                Log.e("tag", "error")
            }
        }
        binding.ivBackMessage.setOnClickListener { activity?.onBackPressed() }
        binding.btnCreateNewGroup.setOnClickListener {
            if(binding.edIssue.text!!.isNotBlank()){
                val dialog = QBChatDialog().apply {
                    name = binding.edIssue.text.toString()
                    type  = QBDialogType.GROUP
                    setOccupantsIds(selectedUserAdapter.selectedUsers.map { it.id.toInt() })
                }
                QBRestChatService.createChatDialog(dialog).performAsync(object : QBEntityCallback<QBChatDialog>{
                    override fun onSuccess(p0: QBChatDialog?, p1: Bundle?) {
                        val dialogs = ArrayList<QBChatDialog>()
                        dialogs.add(p0!!)
                        QbDialogHolder.addDialogs(dialogs)
                        sendSystemMessageAboutCreatingDialog(QBChatService.getInstance().systemMessagesManager, p0)
                        Log.d("IsLoggin", QBChatService.getInstance().isLoggedIn.toString())
                        Log.d("Reconenction", QBChatService.getInstance().isReconnectionAllowed.toString())
                        for(chatDialog in dialogs){
                            try {
                                val history = DiscussionHistory()
                                history.maxStanzas = 0
                                chatDialog.join(history)
                                findNavController().navigate(ConfirmNewGroupFragmentDirections.actionNavigationConfirmNewGroupToNavigationChat(dialog.dialogId, true))
                            } catch (e: XMPPException) {
                                Log.d("error", e.toString())
                            } catch (e: SmackException) {
                                Log.d("error", e.toString())
                            }
                        }
                    }

                    override fun onError(p0: QBResponseException?) { Log.d("failure",p0.toString()) }
                })
            }else{
                notify(requireActivity(), "Se tiene que agregar un asunto")
            }
        }
    }

    private fun sendSystemMessageAboutCreatingDialog(systemMessagesManager: QBSystemMessagesManager, dialog : QBChatDialog){
        val message = buildMessageCreatedGroupDialog(dialog)
        prepareSystemMessage(systemMessagesManager, message, dialog.occupants)
    }
    private fun buildMessageCreatedGroupDialog(dialog: QBChatDialog): QBChatMessage {
        val qbChatMessage = QBChatMessage()
        qbChatMessage.dialogId = dialog.dialogId
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, getOccupantsIdsStringFromList(dialog.occupants))
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, dialog.type.code.toString())
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, dialog.name.toString())
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG)
        qbChatMessage.dateSent = System.currentTimeMillis() / 1000
        qbChatMessage.body = "Nuevo dialogo encontrado "//AndroidApplication.getInstance().getString("asd", getCurrentUserName(), dialog.name)
        qbChatMessage.setSaveToHistory(true)
        qbChatMessage.isMarkable = true
        return qbChatMessage
    }
    private fun getOccupantsIdsStringFromList(occupantIdsList: Collection<Int>): String {
        return TextUtils.join(",", occupantIdsList)
    }
    private fun getCurrentUserName(): String {
        val currentUser = QBChatService.getInstance().user
        return if (TextUtils.isEmpty(currentUser.fullName)) currentUser.login else currentUser.fullName
    }
    private fun prepareSystemMessage(systemMessagesManager: QBSystemMessagesManager, message: QBChatMessage, occupants: List<Int>) {
        message.setSaveToHistory(false)
        message.isMarkable = false

        try {
            for (opponentID in occupants) {
                if (opponentID != QBChatService.getInstance().user.id) {
                    message.recipientId = opponentID
                    systemMessagesManager.sendSystemMessage(message)
                }
            }
        } catch (e: SmackException.NotConnectedException) {
            Log.d("error", "Sending System Message Error: " + e.message)
            e.printStackTrace()
        }
    }
}