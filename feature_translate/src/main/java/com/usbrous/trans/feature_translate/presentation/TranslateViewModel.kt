package com.usbrous.trans.feature_translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usbrous.trans.core.common.Resource
import com.usbrous.trans.feature_translate.domain.model.Language
import com.usbrous.trans.feature_translate.domain.usecase.TranslateTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateTextUseCase: TranslateTextUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    private var translationJob: Job? = null

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.InputTextChanged -> {
                _uiState.update {
                    it.copy(inputText = event.text, error = null, translationError = null)
                }
            }

            is TranslateEvent.ClearInput -> {
                translationJob?.cancel()
                _uiState.update {
                    it.copy(
                        inputText = "",
                        translatedText = "",
                        isTranslating = false,
                        error = null,
                        translationError = null
                    )
                }
            }

            is TranslateEvent.SwapLanguages -> {
                swapLanguages()
            }

            is TranslateEvent.SelectSourceLanguage -> {
                selectSourceLanguage(event.language)
            }

            is TranslateEvent.SelectTargetLanguage -> {
                selectTargetLanguage(event.language)
            }

            is TranslateEvent.OpenLanguagePicker -> {
                _uiState.update { it.copy(languagePickerTarget = event.target) }
            }

            is TranslateEvent.DismissLanguagePicker -> {
                _uiState.update { it.copy(languagePickerTarget = null) }
            }

            is TranslateEvent.PastedText -> {
                _uiState.update {
                    it.copy(inputText = event.text, error = null, translationError = null)
                }
            }

            is TranslateEvent.Translate -> {
                translate()
            }

            is TranslateEvent.Retry -> {
                _uiState.update { it.copy(translationError = null, error = null) }
                translate()
            }

            is TranslateEvent.CopyTranslation -> Unit

            is TranslateEvent.MicClicked -> Unit

            is TranslateEvent.CameraClicked -> Unit

            is TranslateEvent.OnSpeechResult -> {
                _uiState.update {
                    it.copy(inputText = event.text, translationError = null)
                }
                translate()
            }

            is TranslateEvent.OnCameraResult -> {
                _uiState.update {
                    it.copy(inputText = event.text, translationError = null)
                }
                translate()
            }

            is TranslateEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun swapLanguages() {
        val state = _uiState.value

        if (state.sourceLanguage.isAutoDetect) {
            val concreteSource = Language.supportedLanguages().first()
            _uiState.update {
                it.copy(
                    sourceLanguage = it.targetLanguage,
                    targetLanguage = concreteSource,
                    inputText = it.translatedText,
                    translatedText = it.inputText,
                    translationError = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    sourceLanguage = it.targetLanguage,
                    targetLanguage = it.sourceLanguage,
                    inputText = it.translatedText,
                    translatedText = it.inputText,
                    translationError = null
                )
            }
        }
    }

    private fun selectSourceLanguage(language: Language) {
        _uiState.update { state ->
            val newTarget = if (!language.isAutoDetect && language == state.targetLanguage) {
                state.sourceLanguage.takeUnless { it.isAutoDetect }
                    ?: Language.supportedLanguages().first { it != language }
            } else {
                state.targetLanguage
            }

            state.copy(
                sourceLanguage = language,
                targetLanguage = newTarget,
                languagePickerTarget = null,
                recentSourceLanguages = addToRecent(language, state.recentSourceLanguages),
                translationError = null
            )
        }
        retranslateIfNeeded()
    }

    private fun selectTargetLanguage(language: Language) {
        _uiState.update { state ->
            val newSource = if (!state.sourceLanguage.isAutoDetect && language == state.sourceLanguage) {
                state.targetLanguage
            } else {
                state.sourceLanguage
            }

            state.copy(
                sourceLanguage = newSource,
                targetLanguage = language,
                languagePickerTarget = null,
                recentTargetLanguages = addToRecent(language, state.recentTargetLanguages),
                translationError = null
            )
        }
        retranslateIfNeeded()
    }

    private fun retranslateIfNeeded() {
        if (_uiState.value.inputText.isNotBlank()) {
            translate()
        }
    }

    private fun addToRecent(language: Language, current: List<Language>): List<Language> {
        if (language.isAutoDetect) return current
        val updated = (listOf(language) + current.filter { it != language })
        return updated.take(MAX_RECENT_LANGUAGES)
    }

    private fun translate() {
        val state = _uiState.value
        if (state.inputText.isBlank()) {
            _uiState.update {
                it.copy(translatedText = "", isTranslating = false, translationError = null)
            }
            return
        }

        translationJob?.cancel()
        translationJob = viewModelScope.launch {
            _uiState.update {
                it.copy(isTranslating = true, error = null, translationError = null)
            }

            when (val result = translateTextUseCase(
                text = state.inputText,
                sourceLanguage = state.sourceLanguage,
                targetLanguage = state.targetLanguage
            )) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            translatedText = result.data.translatedText,
                            isTranslating = false,
                            translationError = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            translationError = result.message ?: "Translation failed. Please try again.",
                            isTranslating = false
                        )
                    }
                }

                is Resource.Loading -> Unit
            }
        }
    }

    companion object {
        private const val MAX_RECENT_LANGUAGES = 3
    }
}
