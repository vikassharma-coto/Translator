package com.usbrous.trans.feature_translate.data.source

interface TranslationDataSource {
    suspend fun translate(
        text: String,
        sourceLanguageCode: String,
        targetLanguageCode: String
    ): String
}
