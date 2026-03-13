package com.usbrous.trans.feature_translate.data.repository

import com.usbrous.trans.feature_translate.data.source.TranslationDataSource
import com.usbrous.trans.feature_translate.domain.model.Language
import com.usbrous.trans.feature_translate.domain.model.Translation
import com.usbrous.trans.feature_translate.domain.repository.TranslationRepository

class TranslationRepositoryImpl(
    private val dataSource: TranslationDataSource
) : TranslationRepository {

    override suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): Translation {
        val translatedText = dataSource.translate(text, sourceLanguage.code, targetLanguage.code)
        return Translation(
            sourceText = text,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
    }
}
