package com.example.amro.fragments


import com.example.amro.R
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_text_to_speech.*
import java.util.*





class TextToSpeechFragment : BaseFragment(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_text_to_speech, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(baseActivity, R.anim.rotate_forward)
        mCircularImage.startAnimation(anim)

        tts = TextToSpeech(baseActivity, this)
    }

        override fun onInit(status: Int) {

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                speakOut()
            }
        }, 0, 2000)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun speakOut() {
        val text = "Please move away"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            tts.language = Locale.US
            tts.setSpeechRate(0.40f)
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            tts.language = Locale.US
            tts.setSpeechRate(0.40f)
        }


    }

    override fun onPause() {
        tts.stop()
        tts.shutdown()
        super.onPause()
    }

    override fun onStop() {
        tts.stop()
        tts.shutdown()
        super.onStop()
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

}
