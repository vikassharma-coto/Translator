package com.usbrous.trans.feature_translate.presentation

import com.usbrous.trans.feature_translate.domain.model.Language

sealed interface TranslateEvent {
    data class InputTextChanged(val text: String) : TranslateEvent
    data class PastedText(val text: String) : TranslateEvent
    data object ClearInput : TranslateEvent
    data object SwapLanguages : TranslateEvent
    data class SelectSourceLanguage(val language: Language) : TranslateEvent
    data class SelectTargetLanguage(val language: Language) : TranslateEvent
    data class OpenLanguagePicker(val target: LanguagePickerTarget) : TranslateEvent
    data object DismissLanguagePicker : TranslateEvent
    data object Translate : TranslateEvent
    data object CopyTranslation : TranslateEvent
    data object MicClicked : TranslateEvent
    data object CameraClicked : TranslateEvent
    data class OnSpeechResult(val text: String) : TranslateEvent
    data class OnCameraResult(val text: String) : TranslateEvent
    data object Retry : TranslateEvent
    data object DismissError : TranslateEvent
}
