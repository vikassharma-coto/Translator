package com.usbrous.trans.feature_translate.domain.repository

import com.usbrous.trans.feature_translate.domain.model.Language
import com.usbrous.trans.feature_translate.domain.model.Translation

interface TranslationRepository {
    suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): Translation
}
