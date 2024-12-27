package com.pc.mediaplayer.repository


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.pc.mediaplayer.model.Mp3FileData

class Mp3RepositoryImpl(private val context: Context) : Mp3Repository {

    override fun getMp3Files(): List<Mp3FileData> {
        val mp3Files = mutableListOf<Mp3FileData>()
        try {
            val assetManager = context.assets
            val fileNames = assetManager.list("") ?: arrayOf()

            var index = 0
            for (fileName in fileNames) {
                if (fileName.endsWith(".mp3")) {
                    val albumArt = extractAlbumArtFromAssets(fileName)
                    mp3Files.add(Mp3FileData(index, fileName, albumArt))
                    index++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mp3Files
    }

    override fun extractAlbumArtFromAssets(fileName: String): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            val assetFileDescriptor = context.assets.openFd(fileName)
            retriever.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            val art = retriever.embeddedPicture
            retriever.release()

            art?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}