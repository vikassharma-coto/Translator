package com.usbrous.trans.feature_translate.domain.usecase

import com.usbrous.trans.core.common.Resource
import com.usbrous.trans.feature_translate.domain.model.Language
import com.usbrous.trans.feature_translate.domain.model.Translation
import com.usbrous.trans.feature_translate.domain.repository.TranslationRepository

class TranslateTextUseCase(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): Resource<Translation> {
        if (text.isBlank()) {
            return Resource.Error("Input text cannot be empty")
        }
        return try {
            val result = repository.translate(text, sourceLanguage, targetLanguage)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Translation failed", e)
        }
    }
}
