package com.tawa.allinapp.models

import android.widget.*


data class RecordAudioViews(
    var idQuestion: String,
    var idAnswer:String,
    var nameQuestion: String,
    var layoutContainer: LinearLayout?,
    //AudioSelected
    var layoutAudioSelectedButton : LinearLayout?,
    var textViewAudioSelected: TextView?,
    var chrometerSeleceted: Chronometer?,
    var layoutShowSelectedAudio: LinearLayout?,
    var textViewShowSelecetedAudio: TextView?,
    var imageViewClipCloseSelected: ImageView?,

    //RecordButton
    var layoutRecordButton: LinearLayout?,
    var textViewRecording: TextView?,
    var chrometerRecording: Chronometer?,
    var layoutShowRecord: LinearLayout?,
    var textViewShowRecord: TextView?,
    var imageViewClipClose: ImageView?,
    //Records
    var answerSelected:String?,
    var answerRecorded: String?,
    var path: String
    )

