package com.example.vast_debug_player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerManager(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    fun attach(playerView: PlayerView) {
        playerView.player = player
    }

    fun playContent(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun release() {
        player.release()
    }
}