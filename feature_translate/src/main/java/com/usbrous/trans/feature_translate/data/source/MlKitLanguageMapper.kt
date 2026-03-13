package com.usbrous.trans.feature_translate.data.source

import com.google.mlkit.nl.translate.TranslateLanguage

object MlKitLanguageMapper {

    private val codeToMlKit: Map<String, String> = mapOf(
        "af" to TranslateLanguage.AFRIKAANS,
        "ar" to TranslateLanguage.ARABIC,
        "be" to TranslateLanguage.BELARUSIAN,
        "bg" to TranslateLanguage.BULGARIAN,
        "bn" to TranslateLanguage.BENGALI,
        "ca" to TranslateLanguage.CATALAN,
        "cs" to TranslateLanguage.CZECH,
        "cy" to TranslateLanguage.WELSH,
        "da" to TranslateLanguage.DANISH,
        "de" to TranslateLanguage.GERMAN,
        "el" to TranslateLanguage.GREEK,
        "en" to TranslateLanguage.ENGLISH,
        "eo" to TranslateLanguage.ESPERANTO,
        "es" to TranslateLanguage.SPANISH,
        "et" to TranslateLanguage.ESTONIAN,
        "fa" to TranslateLanguage.PERSIAN,
        "fi" to TranslateLanguage.FINNISH,
        "fr" to TranslateLanguage.FRENCH,
        "ga" to TranslateLanguage.IRISH,
        "gl" to TranslateLanguage.GALICIAN,
        "gu" to TranslateLanguage.GUJARATI,
        "he" to TranslateLanguage.HEBREW,
        "hi" to TranslateLanguage.HINDI,
        "hr" to TranslateLanguage.CROATIAN,
        "ht" to TranslateLanguage.HAITIAN_CREOLE,
        "hu" to TranslateLanguage.HUNGARIAN,
        "id" to TranslateLanguage.INDONESIAN,
        "is" to TranslateLanguage.ICELANDIC,
        "it" to TranslateLanguage.ITALIAN,
        "ja" to TranslateLanguage.JAPANESE,
        "ka" to TranslateLanguage.GEORGIAN,
        "kn" to TranslateLanguage.KANNADA,
        "ko" to TranslateLanguage.KOREAN,
        "lt" to TranslateLanguage.LITHUANIAN,
        "lv" to TranslateLanguage.LATVIAN,
        "mk" to TranslateLanguage.MACEDONIAN,
        "mr" to TranslateLanguage.MARATHI,
        "ms" to TranslateLanguage.MALAY,
        "mt" to TranslateLanguage.MALTESE,
        "nl" to TranslateLanguage.DUTCH,
        "no" to TranslateLanguage.NORWEGIAN,
        "pl" to TranslateLanguage.POLISH,
        "pt" to TranslateLanguage.PORTUGUESE,
        "ro" to TranslateLanguage.ROMANIAN,
        "ru" to TranslateLanguage.RUSSIAN,
        "sk" to TranslateLanguage.SLOVAK,
        "sl" to TranslateLanguage.SLOVENIAN,
        "sq" to TranslateLanguage.ALBANIAN,
        "sv" to TranslateLanguage.SWEDISH,
        "sw" to TranslateLanguage.SWAHILI,
        "ta" to TranslateLanguage.TAMIL,
        "te" to TranslateLanguage.TELUGU,
        "th" to TranslateLanguage.THAI,
        "tl" to TranslateLanguage.TAGALOG,
        "tr" to TranslateLanguage.TURKISH,
        "uk" to TranslateLanguage.UKRAINIAN,
        "ur" to TranslateLanguage.URDU,
        "vi" to TranslateLanguage.VIETNAMESE,
        "zh" to TranslateLanguage.CHINESE,
    )

    fun toMlKitCode(languageCode: String): String {
        return codeToMlKit[languageCode]
            ?: throw IllegalArgumentException("Unsupported language code: $languageCode")
    }

    fun fromIdentifiedTag(bcp47Tag: String): String {
        return codeToMlKit.entries
            .firstOrNull { it.value == bcp47Tag }
            ?.key
            ?: bcp47Tag.take(2)
    }
}
