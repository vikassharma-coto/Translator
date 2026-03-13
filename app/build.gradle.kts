import java.util.Properties
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

// ── Security helpers ──────────────────────────────────────────────────────────
// Rotating XOR salt — embedded in source, not in any config file.
// DO NOT change after keys have been generated; changing it invalidates all existing keys.
val KEY_XOR_SALT = byteArrayOf(0x54, 0x52, 0x41, 0x4E, 0x53, 0x5F, 0x4B, 0x45) // "TRANS_KE"

fun sha256hex(bytes: ByteArray): String =
    MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }

fun xorWithSalt(input: ByteArray): ByteArray =
    ByteArray(input.size) { i -> (input[i].toInt() xor KEY_XOR_SALT[i % KEY_XOR_SALT.size].toInt()).toByte() }

fun computeKeyProof(keyBase64: String): Pair<String, Int>? = runCatching {
    val decoded = Base64.getDecoder().decode(keyBase64)
    require(decoded.size == 24) { "Key must decode to exactly 24 bytes" }
    val xored = xorWithSalt(decoded)
    val hash = sha256hex(xored)
    val checksum = xored.fold(0) { acc, b -> (acc + (b.toInt() and 0xFF)) % 251 }
    Pair(hash, checksum)
}.getOrNull()


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
}

// ── Security gate (skipped only when running generateSecurityKey task) ────────
val isGeneratingKey = gradle.startParameter.taskNames
    .any { it.contains("generateSecurityKey", ignoreCase = true) }

var validatedKeyHash = ""

if (!isGeneratingKey) {
    val accessKey = localProperties.getProperty("APP_ACCESS_KEY")
        ?: throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_ACCESS_KEY' is missing from local.properties.\n" +
            "Run './gradlew generateSecurityKey' to generate a valid key pair.\n"
        )

    val expectedHash = (project.findProperty("APP_KEY_HASH") as? String)
        ?.takeIf { it.isNotBlank() }
        ?: throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_KEY_HASH' is missing from gradle.properties.\n" +
            "Run './gradlew generateSecurityKey' to generate a valid key pair.\n"
        )

    val expectedChecksum = (project.findProperty("APP_KEY_CHECKSUM") as? String)
        ?.toIntOrNull()
        ?: throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_KEY_CHECKSUM' is missing or invalid in gradle.properties.\n" +
            "Run './gradlew generateSecurityKey' to generate a valid key pair.\n"
        )

    val (computedHash, computedChecksum) = computeKeyProof(accessKey)
        ?: throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_ACCESS_KEY' is not a valid key.\n" +
            "Keys must be Base64-encoded 24-byte values. Run './gradlew generateSecurityKey'.\n"
        )

    if (computedHash != expectedHash) {
        throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_ACCESS_KEY' failed hash validation.\n" +
            "The key does not match the expected signature in gradle.properties.\n"
        )
    }

    if (computedChecksum != expectedChecksum) {
        throw GradleException(
            "\n\n[SECURITY] Build blocked: 'APP_ACCESS_KEY' failed checksum validation.\n" +
            "The key integrity check did not pass.\n"
        )
    }

    validatedKeyHash = computedHash
}
// ─────────────────────────────────────────────────────────────────────────────

android {
    namespace = "com.usbrous.trans"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.usbrous.trans"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Only hashes are injected — the actual key never touches the APK
        buildConfigField("String", "KEY_PROOF", "\"$validatedKeyHash\"")
        buildConfigField("String", "EXPECTED_KEY_PROOF", "\"${project.findProperty("APP_KEY_HASH") as? String ?: ""}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature_translate"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// ── Key generation task ───────────────────────────────────────────────────────
tasks.register("generateSecurityKey") {
    group = "security"
    description = "Generates a new APP_ACCESS_KEY for local.properties and APP_KEY_HASH/APP_KEY_CHECKSUM for gradle.properties"
    doLast {
        val rawBytes = ByteArray(24).also { SecureRandom().nextBytes(it) }
        val keyBase64 = Base64.getEncoder().encodeToString(rawBytes)
        val xored = xorWithSalt(rawBytes)
        val hash = sha256hex(xored)
        val checksum = xored.fold(0) { acc, b -> (acc + (b.toInt() and 0xFF)) % 251 }

        println("""
╔══════════════════════════════════════════════════════════════════╗
║               SECURITY KEY GENERATED SUCCESSFULLY               ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  1. Add to local.properties  (DO NOT commit — already gitignored)║
║                                                                  ║
║     APP_ACCESS_KEY=$keyBase64
║                                                                  ║
║  2. Add/update in gradle.properties  (safe to commit)            ║
║                                                                  ║
║     APP_KEY_HASH=$hash
║     APP_KEY_CHECKSUM=$checksum
║                                                                  ║
║  Share the APP_ACCESS_KEY value out-of-band with authorized      ║
║  developers only. Never put it in version control.               ║
╚══════════════════════════════════════════════════════════════════╝
        """.trimIndent())
    }
}
