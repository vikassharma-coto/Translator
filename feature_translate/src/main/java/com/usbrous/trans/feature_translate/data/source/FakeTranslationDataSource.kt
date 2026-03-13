package com.usbrous.trans.feature_translate.data.source

import kotlinx.coroutines.delay

class FakeTranslationDataSource : TranslationDataSource {

    override suspend fun translate(
        text: String,
        sourceLanguageCode: String,
        targetLanguageCode: String
    ): String {
        delay(500)
        return "[Translated from $sourceLanguageCode to $targetLanguageCode]: $text"
    }
}
