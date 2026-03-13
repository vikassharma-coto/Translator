package com.usbrous.trans.feature_translate.presentation.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class SpeechRecognizerState(
    private val launchRecognizer: (String) -> Unit,
    private val requestPermission: () -> Unit,
    private val hasPermission: () -> Boolean,
    val isAvailable: Boolean
) {
    fun launch(languageCode: String) {
        if (hasPermission()) {
            launchRecognizer(languageCode)
        } else {
            requestPermission()
        }
    }
}

@Composable
fun rememberSpeechRecognizer(
    onResult: (String) -> Unit,
    onError: (String) -> Unit
): SpeechRecognizerState {
    val context = LocalContext.current
    val isAvailable = remember {
        SpeechRecognizer.isRecognitionAvailable(context)
    }

    var pendingLanguageCode = remember { "en" }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = matches?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                onResult(spokenText)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchSpeechIntent(speechLauncher = { intent -> speechLauncher.launch(intent) }, pendingLanguageCode)
        } else {
            onError("Microphone permission is required for voice input")
        }
    }

    return remember(isAvailable) {
        SpeechRecognizerState(
            launchRecognizer = { languageCode ->
                pendingLanguageCode = languageCode
                launchSpeechIntent(
                    speechLauncher = { intent -> speechLauncher.launch(intent) },
                    languageCode
                )
            },
            requestPermission = {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            hasPermission = {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            },
            isAvailable = isAvailable
        )
    }
}

private fun launchSpeechIntent(
    speechLauncher: (Intent) -> Unit,
    languageCode: String
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now…")
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
    }
    speechLauncher(intent)
}
