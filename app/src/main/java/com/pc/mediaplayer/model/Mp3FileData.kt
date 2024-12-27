package com.pc.mediaplayer.model

import android.graphics.Bitmap

data class Mp3FileData(
    val index: Int,
    val fileName: String,
    val albumArt: Bitmap?,
)