package com.usbrous.trans.feature_translate.domain.model

data class Translation(
    val sourceText: String,
    val translatedText: String,
    val sourceLanguage: Language,
    val targetLanguage: Language
)
