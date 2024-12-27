package com.pc.mediaplayer.repository

import android.graphics.Bitmap
import com.pc.mediaplayer.model.Mp3FileData

interface Mp3Repository {
    fun getMp3Files(): List<Mp3FileData>
    fun extractAlbumArtFromAssets(fileName: String): Bitmap?
}