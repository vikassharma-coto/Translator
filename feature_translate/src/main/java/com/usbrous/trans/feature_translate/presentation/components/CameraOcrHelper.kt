package com.usbrous.trans.feature_translate.presentation.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class CameraOcrState(
    private val launchCamera: () -> Unit,
    private val requestPermission: () -> Unit,
    private val hasPermission: () -> Boolean,
    val hasCameraHardware: Boolean
) {
    fun launch() {
        if (hasPermission()) {
            launchCamera()
        } else {
            requestPermission()
        }
    }
}

@Composable
fun rememberCameraOcr(
    onResult: (String) -> Unit,
    onError: (String) -> Unit
): CameraOcrState {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val hasCameraHardware = remember {
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            scope.launch {
                try {
                    val extractedText = recognizeText(context, photoUri!!)
                    if (extractedText.isNotBlank()) {
                        onResult(extractedText)
                    } else {
                        onError("No text found in image")
                    }
                } catch (e: Exception) {
                    onError("Failed to recognize text: ${e.localizedMessage}")
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            photoUri = uri
            cameraLauncher.launch(uri)
        } else {
            onError("Camera permission is required to capture text")
        }
    }

    return remember(hasCameraHardware) {
        CameraOcrState(
            launchCamera = {
                val uri = createImageUri(context)
                photoUri = uri
                cameraLauncher.launch(uri)
            },
            requestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            hasPermission = {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            },
            hasCameraHardware = hasCameraHardware
        )
    }
}

private fun createImageUri(context: Context): Uri {
    val imageDir = File(context.cacheDir, "camera_images").apply { mkdirs() }
    val imageFile = File(imageDir, "ocr_capture_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

private suspend fun recognizeText(context: Context, imageUri: Uri): String {
    val image = InputImage.fromFilePath(context, imageUri)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    return try {
        val result = recognizer.process(image).await()
        result.text
    } finally {
        recognizer.close()
    }
}
