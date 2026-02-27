package com.example.vast_debug_player.ads

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.example.vast_debug_player.logs.AdEventLogger
import androidx.core.net.toUri

@UnstableApi
class ImaManager(
    private val context: Context,
    private val player: ExoPlayer,
    private val logger: AdEventLogger
) {

    private var adsLoader: ImaAdsLoader? = null
    private val dataSourceFactory = DefaultDataSource.Factory(context)

    fun playWithAds(contentUrl: String, vastUrl: String, playerView: PlayerView) {

        release()

        player.stop()
        player.clearMediaItems()

        adsLoader = ImaAdsLoader.Builder(context)
            .setAdEventListener { event ->
                if (event.type.name != "AD_PROGRESS") {
                    logger.log("AD EVENT: ${event.type}")
                }
            }
            .setAdErrorListener { error ->
                logger.log("AD ERROR: ${error.error.message}")
            }
            .build()

        adsLoader?.setPlayer(player)

        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setLocalAdInsertionComponents(
                { adsLoader },
                playerView
            )

        val uniqueAdsId = System.currentTimeMillis().toString()

        val mediaItem = MediaItem.Builder()
            .setUri(contentUrl.toUri())
            .setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(vastUrl.toUri())
                    .setAdsId(uniqueAdsId)
                    .build()
            )
            .build()

        player.setMediaSource(mediaSourceFactory.createMediaSource(mediaItem))
        player.prepare()
        player.playWhenReady = true
    }

    fun release() {
        adsLoader?.setPlayer(null)
        adsLoader?.release()
        adsLoader = null
    }
}