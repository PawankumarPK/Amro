package com.example.amro.Arya


import com.example.amro.Arya.utils.ConversionCallback
import com.example.amro.Arya.utils.TranslatorFactory
import com.example.amro.R
import com.example.amro.fragments.BaseFragment
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_speech_to_text.*


class SpeechToTextFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_speech_to_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                speak(view)
                handler.postDelayed(this, 5000)
            }
        }

//Start
        handler.postDelayed(runnable, 1000)
    }

    private fun speak(view: View) {

        Snackbar.make(view, "Speak now, App is listening", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        TranslatorFactory.instance.with(TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT,
                        object : ConversionCallback {
                            override fun onSuccess(result: String) {
                                txvResult.text = result
                            }

                            override fun onCompletion() {
                            }

                            override fun onErrorOccurred(errorMessage: String) {
                                txvError.text = "Speech2Text Error: $errorMessage"
                            }

                        }).initialize("Speak Now !!", baseActivity)

    }

    override fun onDestroy() {

        super.onDestroy()
    }

}
