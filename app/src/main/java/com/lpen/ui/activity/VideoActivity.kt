package com.lpen.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lpen.R
import com.lpen.vplayer.AndroidMediaController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.concurrent.TimeUnit

/**
 * @author LPen
 * @date 19-9-13
 */
class VideoActivity : AppCompatActivity(),
    IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener {

    private var mController: AndroidMediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        mController = AndroidMediaController(this)
        mController!!.setSupportActionBar(txtTitle)
        player?.setMediaController(mController)
        player?.setVideoPath("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=66073&editionType=high&source=aliyun")
        player?.setOnErrorListener(this)
        player?.setOnCompletionListener(this)
        player?.start()
    }

    private var mDisposable: Disposable? = null

    private fun loopPlayProgress() {
        mDisposable?.dispose()

        mDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val duration = player?.duration ?: -1
                val progress = player?.currentPosition ?: 0
                if (progress > 0 && duration > 0) {
                    loading?.visibility = View.GONE
                }
            }
    }

    private var isPause: Boolean = false

    override fun onResume() {
        super.onResume()
        loopPlayProgress()
        if (isPause) {
            player?.start()
            isPause = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (player?.isPlaying == true) {
            isPause = true
            player?.pause()
        }
        mDisposable?.dispose()
    }

    override fun onDestroy() {
        mDisposable?.dispose()
        mController?.clearAnimation()
        player?.stopPlayback()
        player?.release(true)
        player?.stopBackgroundPlay()
        super.onDestroy()
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        Toast.makeText(this, "播放出错", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show()
    }

}