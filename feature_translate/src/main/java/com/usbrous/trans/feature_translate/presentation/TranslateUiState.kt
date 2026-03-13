package com.usbrous.trans.feature_translate.presentation

import com.usbrous.trans.feature_translate.domain.model.Language

data class TranslateUiState(
    val inputText: String = "",
    val translatedText: String = "",
    val sourceLanguage: Language = Language.DEFAULT_SOURCE,
    val targetLanguage: Language = Language.DEFAULT_TARGET,
    val isTranslating: Boolean = false,
    val error: String? = null,
    val translationError: String? = null,
    val languagePickerTarget: LanguagePickerTarget? = null,
    val recentSourceLanguages: List<Language> = emptyList(),
    val recentTargetLanguages: List<Language> = emptyList()
)

enum class LanguagePickerTarget {
    SOURCE, TARGET
}
