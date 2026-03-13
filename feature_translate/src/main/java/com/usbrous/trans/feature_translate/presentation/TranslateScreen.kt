package com.usbrous.trans.feature_translate.presentation

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.usbrous.trans.core.utils.ClipboardHelper
import com.usbrous.trans.feature_translate.domain.model.Language
import com.usbrous.trans.feature_translate.presentation.components.LanguagePickerSheet
import com.usbrous.trans.feature_translate.presentation.components.LanguageSelectorBar
import com.usbrous.trans.feature_translate.presentation.components.TranslateInputSection
import com.usbrous.trans.feature_translate.presentation.components.TranslateOutputSection
import com.usbrous.trans.feature_translate.presentation.components.rememberCameraOcr
import com.usbrous.trans.feature_translate.presentation.components.rememberSpeechRecognizer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen(
    uiState: TranslateUiState,
    onEvent: (TranslateEvent) -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val speechRecognizer = rememberSpeechRecognizer(
        onResult = { spokenText ->
            onEvent(TranslateEvent.OnSpeechResult(spokenText))
        },
        onError = { errorMessage ->
            scope.launch { snackbarHostState.showSnackbar(errorMessage) }
        }
    )

    val cameraOcr = rememberCameraOcr(
        onResult = { extractedText ->
            onEvent(TranslateEvent.OnCameraResult(extractedText))
        },
        onError = { errorMessage ->
            scope.launch { snackbarHostState.showSnackbar(errorMessage) }
        }
    )

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(TranslateEvent.DismissError)
        }
    }

    fun performCopyWithHaptic(label: String, text: String, feedbackMessage: String) {
        ClipboardHelper.copyToClipboard(context = context, label = label, text = text)
        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        scope.launch { snackbarHostState.showSnackbar(feedbackMessage) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Translator",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToAbout) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "About",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LanguageSelectorBar(
                sourceLanguage = uiState.sourceLanguage,
                targetLanguage = uiState.targetLanguage,
                onSourceLanguageClick = {
                    onEvent(TranslateEvent.OpenLanguagePicker(LanguagePickerTarget.SOURCE))
                },
                onTargetLanguageClick = {
                    onEvent(TranslateEvent.OpenLanguagePicker(LanguagePickerTarget.TARGET))
                },
                onSwapClick = { onEvent(TranslateEvent.SwapLanguages) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TranslateInputSection(
                inputText = uiState.inputText,
                isTranslating = uiState.isTranslating,
                onInputTextChange = { onEvent(TranslateEvent.InputTextChanged(it)) },
                onTranslateClick = { onEvent(TranslateEvent.Translate) },
                onClearClick = { onEvent(TranslateEvent.ClearInput) },
                onPasteClick = {
                    val clipText = ClipboardHelper.getFromClipboard(context)
                    if (!clipText.isNullOrBlank()) {
                        onEvent(TranslateEvent.PastedText(clipText))
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Clipboard is empty")
                        }
                    }
                },
                onCopyInputClick = {
                    if (uiState.inputText.isNotEmpty()) {
                        performCopyWithHaptic(
                            label = "Source text",
                            text = uiState.inputText,
                            feedbackMessage = "Source text copied"
                        )
                    }
                },
                onMicClick = {
                    if (speechRecognizer.isAvailable) {
                        val langCode = if (uiState.sourceLanguage.isAutoDetect) "en"
                                       else uiState.sourceLanguage.code
                        speechRecognizer.launch(langCode)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Speech recognition is not available on this device")
                        }
                    }
                },
                onCameraClick = {
                    if (cameraOcr.hasCameraHardware) {
                        cameraOcr.launch()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Camera is not available on this device")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TranslateOutputSection(
                translatedText = uiState.translatedText,
                isLoading = uiState.isTranslating,
                errorMessage = uiState.translationError,
                hasInput = uiState.inputText.isNotEmpty(),
                onCopyClick = {
                    performCopyWithHaptic(
                        label = "Translation",
                        text = uiState.translatedText,
                        feedbackMessage = "Translation copied"
                    )
                },
                onRetryClick = { onEvent(TranslateEvent.Retry) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    uiState.languagePickerTarget?.let { target ->
        val isSource = target == LanguagePickerTarget.SOURCE

        LanguagePickerSheet(
            title = if (isSource) "Translate from" else "Translate to",
            languages = if (isSource) Language.sourceLanguages() else Language.targetLanguages(),
            recentLanguages = if (isSource) uiState.recentSourceLanguages
                             else uiState.recentTargetLanguages,
            selectedLanguage = if (isSource) uiState.sourceLanguage else uiState.targetLanguage,
            onLanguageSelected = { language ->
                if (isSource) onEvent(TranslateEvent.SelectSourceLanguage(language))
                else onEvent(TranslateEvent.SelectTargetLanguage(language))
            },
            onDismiss = { onEvent(TranslateEvent.DismissLanguagePicker) }
        )
    }
}
