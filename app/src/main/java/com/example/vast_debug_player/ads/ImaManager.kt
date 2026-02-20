package com.example.vast_debug_player.ads

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.example.vast_debug_player.logs.AdEventLogger

class ImaManager(
    private val context: Context,
    private val player: ExoPlayer,
    private val logger: AdEventLogger
) {

    private val dataSourceFactory = DefaultDataSource.Factory(context)
    private val adsLoader = ImaAdsLoader.Builder(context)
        .setAdEventListener { event ->
            logger.log("AD EVENT: ${event.type}")
        }
        .setAdErrorListener { error ->
            logger.log("AD ERROR: ${error.error.message}")
        }
        .build()

    fun playWithAds(contentUrl: String, vastUrl: String, playerView: PlayerView) {

        playerView.player = player
        adsLoader.setPlayer(player)

        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider { adsLoader }
            .setAdViewProvider(playerView)

        val mediaItem = MediaItem.Builder()
            .setUri(contentUrl)
            .setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(Uri.parse(vastUrl)).build()
            )
            .build()

        player.setMediaSource(mediaSourceFactory.createMediaSource(mediaItem))
        player.prepare()
        player.playWhenReady = true
    }

    fun release() {
        adsLoader.setPlayer(null)
        adsLoader.release()
    }
}