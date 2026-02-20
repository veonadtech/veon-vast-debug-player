package com.example.vast_debug_player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vast_debug_player.ads.ImaManager
import com.example.vast_debug_player.databinding.ActivityMainBinding
import com.example.vast_debug_player.logs.AdEventLogger
import com.example.vast_debug_player.player.PlayerManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerManager: PlayerManager
    private lateinit var imaManager: ImaManager
    private lateinit var logger: AdEventLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerManager = PlayerManager(this)
        logger = AdEventLogger(binding.logRecycler)
        imaManager = ImaManager(this, playerManager.player, logger)

        playerManager.attach(binding.playerView)

        binding.btnPlayDirect.setOnClickListener {
            logger.clear()
            playerManager.playContent(binding.inputUrl.text.toString())
        }

        binding.btnPlayVast.setOnClickListener {
            logger.clear()

            val vastUrl = binding.inputUrl.text.toString()
            if (vastUrl.isBlank()) {
                Toast.makeText(this, "URL is empty. Enter VAST URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val testContent =
                "https://storage.googleapis.com/gvabox/media/samples/stock.mp4"

            imaManager.playWithAds(
                contentUrl = testContent,
                vastUrl = vastUrl,
                playerView = binding.playerView
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
        imaManager.release()
    }
}