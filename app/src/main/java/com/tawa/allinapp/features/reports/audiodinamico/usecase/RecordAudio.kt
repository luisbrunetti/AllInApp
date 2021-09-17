package com.tawa.allinapp.features.reports.audiodinamico.usecase

import android.widget.*

data class RecordAudio(
    val tagLayout:String,
    val tagTextView: String,
    val tagChrometer: String,
    val tagLayoutShowRecord:String,
    val tagTextViewShowRecord:String,
    val tagImageViewClipClose:String
)

data class RecordAudioViews(
    val layoutContainer: LinearLayout,
    //AudioSelected
    val layoutSelectAudio : Button,
    //RecordButton
    val layoutRecordButton: LinearLayout,
    val textViewRecording: TextView,
    val chrometerRecording: Chronometer,
    val layoutShowRecord: LinearLayout,
    val textViewShowRecord: TextView,
    val imageViewClipClose: ImageView)

