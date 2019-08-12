package com.example.amro.Arya.utils

import android.app.Activity

class TranslatorFactory private constructor() {

    enum class TRANSLATORS {

        SPEECH_TO_TEXT
    }

    interface IConverter {
        fun initialize(message: String, appContext: Activity): IConverter

        fun getErrorText(errorCode: Int): String
    }


    fun with(TRANSLATORS: TRANSLATORS, conversionCallback: ConversionCallback): IConverter {
        return when (TRANSLATORS) {

            TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT ->

                //Get speech to text translator
                SpeechRecognization(conversionCallback)
        }
    }

    companion object {
        val instance = TranslatorFactory()
    }
}