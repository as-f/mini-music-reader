package com.albertford.autoflip.viewsheetactivity

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.*
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.LinearInterpolator
import com.albertford.autoflip.*
import com.albertford.autoflip.deprecated.PdfSheetRenderer
import com.albertford.autoflip.deprecated.SheetRenderer
import com.albertford.autoflip.room.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_view_sheet.*
import kotlinx.android.synthetic.main.view_sheet_images.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//private const val MANAGE_DOCUMENTS_REQUEST = 1
//private const val READ_EXTERNAL_STORAGE_REQUEST = 2
//private const val MANAGE_DOCUMENTS_AND_READ_EXTERNAL_STORAGE_REQUEST = 3

class ViewSheetActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val SHEET_KEY = "SHEET_KEY"
        private const val LOGIC_KEY = "LOGIC_KEY"
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var logic: ViewSheetLogic? = null

//    private var sheet: Sheet? = null
//    private var bars: List<Bar>? = null
//    private var sheetRenderer: SheetRenderer? = null
    private var scale = 1f

//    private var handler = Handler()

//    private var countdownBeat = 1
//    private var countdownDelay = 0L
//    private var countdownRunnable = object : Runnable {
//        override fun run() {
//            val bars = bars ?: return
//            beat_text.text = countdownBeat.toString()
////            progress_bar.progress = 0
////            val animation = ObjectAnimator.ofInt(progress_bar, "progress", 0, 600)
////            animation.duration = countdownDelay
////            animation.interpolator = LinearInterpolator()
////            animation.setAutoCancel(true)
////            animation.start()
//            if (countdownBeat < bars[0].beatsPerMeasure) {
//                countdownBeat++
//                handler.postDelayed(this, countdownDelay)
//            } else {
//                handler.postDelayed(barRunnable, countdownDelay)
//            }
//        }
//    }

//    private var barIndex = 0
//    private var barRunnable = object : Runnable {
//        override fun run() {
//            val bars = bars
//            val renderer = sheetRenderer
//            bars ?: return
//            renderer ?: return
//            if (barIndex == 0) {
//                beat_text.visibility = View.GONE
//                play_button.visibility = View.GONE
//                progress_bar.visibility = View.GONE
//            } else {
//                renderTwoBars()
//            }
//            barIndex++
//            if (barIndex < bars.size) {
//                val bar = bars[barIndex]
//                handler.postDelayed(this, (60000 * bar.beatsPerMeasure / bar.beatsPerMinute).toLong())
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_view_sheet)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

//        logic = savedInstanceState?.getParcelable(LOGIC_KEY)
//        progress_bar.rotation = 270f

        val canManageDocuments = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) == PackageManager.PERMISSION_GRANTED
        val canReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        /*when {
            canManageDocuments && canReadExternalStorage -> {
                loadSheet()
                play_button.setOnClickListener(playButtonListener)
            }
            canManageDocuments -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_DOCUMENTS), MANAGE_DOCUMENTS_REQUEST)
            }
            canReadExternalStorage -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST)
            }
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_DOCUMENTS), MANAGE_DOCUMENTS_AND_READ_EXTERNAL_STORAGE_REQUEST)
            }
        }*/
        loadSheet()
//        play_button.setOnClickListener(playButtonListener)
    }

    /** Dispose of asynchronous tasks */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        outState?.putParcelable("STATE", state)
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
//            grantResults: IntArray) {
//        when (requestCode) {
//            MANAGE_DOCUMENTS_REQUEST, READ_EXTERNAL_STORAGE_REQUEST -> {
//                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
//                    loadSheet()
//                    play_button.setOnClickListener(playButtonListener)
//                }
//            }
//            MANAGE_DOCUMENTS_AND_READ_EXTERNAL_STORAGE_REQUEST -> {
//                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED &&
//                        grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED) {
//                    loadSheet()
//                    play_button.setOnClickListener(playButtonListener)
//                }
//            }
//        }
//    }

//    private fun renderTwoBars() {
//        val bars = bars
//        bars ?: return
//        val primaryBitmap = sheetRenderer?.renderBar(bars, barIndex, scale)
//        val secondaryBitmap = sheetRenderer?.renderBar(bars, barIndex + 1, scale)
//        primary_image.setImageBitmap(primaryBitmap)
//        secondary_image.setImageBitmap(secondaryBitmap)
//    }

    // One continuous progress bar for the duration of one bar
    // And a discontinuous secondary progress that updates for each beat
//    private val playButtonListener = View.OnClickListener {
//        sheetRenderer ?: return@OnClickListener
//        val firstBar = bars?.getOrNull(0) ?: return@OnClickListener
//        play_button.setImageBitmap(null)
//        val animation = ObjectAnimator.ofInt(progress_bar, "progress", 0, 600)
//        animation.duration = (60000 * firstBar.beatsPerMeasure / firstBar.beatsPerMinute).toLong()
//        animation.interpolator = LinearInterpolator()
//        animation.start()
////        progress_bar.progress = 0
//        countdownBeat = 1
//        handler.post(countdownRunnable)
//    }

    private fun loadSheet() {
//        val disposable = database?.sheetDao()?.selectSheetById(state.sheetId)
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe { result ->
//                    sheet = result.sheet
//                    bars = result.bars
//                    val firstBar = bars?.get(0)
//                    if (firstBar != null) {
//                        countdownDelay = (60000 / firstBar.beatsPerMinute).toLong()
//                    }
//                    loadRenderer()
//                }
//        if (disposable != null) {
//            compositeDisposable.add(disposable)
//        }
    }
}

interface ViewActivityObserver {
    fun play()
    fun pause()
    fun restart()
}
