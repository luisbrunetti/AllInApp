package com.tawa.allinapp.tools

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.quickblox.auth.session.QBSettings
import com.quickblox.chat.QBChatService
import com.quickblox.chat.QBRestChatService
import com.quickblox.chat.model.QBChatDialog
import com.quickblox.chat.utils.DialogUtils
import com.quickblox.core.LogLevel
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

object ChatTool {
    private const val PRIVATE_CHAT_COUNT_USERS = 2
    private var qbChatService: QBChatService = QBChatService.getInstance()

    init {
        QBSettings.getInstance().logLevel = LogLevel.DEBUG
        QBChatService.setDebugEnabled(true)
    }

    fun login(user: QBUser, callback: QBEntityCallback<QBUser>){
        QBUsers.signIn(user).performAsync( object : QbEntityCallbackTwoTypeWrapper<QBUser,QBUser>(callback){
            override fun onSuccess(t: QBUser, bundle: Bundle?) {
                callback.onSuccess(t,bundle)
            }
        })
    }
    fun createDialogWithSelectedUsers(users:MutableList<QBUser>, chatName: String, callback : QBEntityCallback<QBChatDialog>){
        val dialog = createDialog(users,chatName)
        Log.d("dialog",dialog.occupants.toString())
        QBRestChatService.createChatDialog(dialog).performAsync( object: QBEntityCallback<QBChatDialog>{
            @SuppressLint("LongLogTag")
            override fun onSuccess(p0: QBChatDialog?, p1: Bundle?) {Log.d("createDialogWithSelectedUsers","Sucess")}
            @SuppressLint("LongLogTag")
            override fun onError(p0: QBResponseException?) {Log.d("createDialogWithSelectedUsers","Error")}
        })
    }

    private fun createDialog(users: MutableList<QBUser>, chatName: String): QBChatDialog {
        /*if (isPrivateChat(users)) {
            users.remove(ChatHelper.getCurrentUser())
        }*/
        val dialog = DialogUtils.buildDialog(*users.toTypedArray())
        if (!TextUtils.isEmpty(chatName)) {
            dialog.name = chatName
        }
        return dialog
    }

    private fun isPrivateChat(users: List<QBUser>): Boolean {
        return users.size == PRIVATE_CHAT_COUNT_USERS
    }
}