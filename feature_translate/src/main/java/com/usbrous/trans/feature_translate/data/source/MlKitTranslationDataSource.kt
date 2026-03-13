package com.usbrous.trans.feature_translate.data.source

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ConcurrentHashMap
import com.google.mlkit.nl.translate.Translator as MlKitTranslator

class MlKitTranslationDataSource : TranslationDataSource {

    private val translatorCache = ConcurrentHashMap<String, MlKitTranslator>()

    override suspend fun translate(
        text: String,
        sourceLanguageCode: String,
        targetLanguageCode: String
    ): String {
        val resolvedSourceCode = if (sourceLanguageCode == "auto") {
            identifyLanguage(text)
        } else {
            sourceLanguageCode
        }

        val mlSourceCode = MlKitLanguageMapper.toMlKitCode(resolvedSourceCode)
        val mlTargetCode = MlKitLanguageMapper.toMlKitCode(targetLanguageCode)

        if (mlSourceCode == mlTargetCode) {
            return text
        }

        val translator = getOrCreateTranslator(mlSourceCode, mlTargetCode)

        translator.downloadModelIfNeeded().await()

        return translator.translate(text).await()
    }

    private suspend fun identifyLanguage(text: String): String {
        val languageIdentifier = LanguageIdentification.getClient()
        return try {
            val identifiedTag = languageIdentifier.identifyLanguage(text).await()
            if (identifiedTag == "und") {
                "en"
            } else {
                MlKitLanguageMapper.fromIdentifiedTag(identifiedTag)
            }
        } finally {
            languageIdentifier.close()
        }
    }

    private fun getOrCreateTranslator(
        sourceCode: String,
        targetCode: String
    ): MlKitTranslator {
        val key = "${sourceCode}_$targetCode"
        return translatorCache.getOrPut(key) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceCode)
                .setTargetLanguage(targetCode)
                .build()
            Translation.getClient(options)
        }
    }

    fun close() {
        translatorCache.values.forEach { it.close() }
        translatorCache.clear()
    }
}
