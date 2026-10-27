package com.example.xelvicfilters.core

data class FilterInfo(
    val id: String,
    val name: String,
    val version: String,
    val installed: Boolean = false
)

enum class FaceAnchor {
    FOREHEAD_TOP, EYES_CENTER, NOSE_TIP, MOUTH_CENTER, CHIN, FACE_CENTER
}

data class Landmark(val x: Float, val y: Float, val z: Float)

data class FaceFrame(val landmarks: List<Landmark>, val timestampMs: Long)

// Low-end devices ke liye tunable. Real values baad mein device testing se aayengi.
data class TrackerConfig(
    val analysisWidth: Int = 640,
    val analysisHeight: Int = 480,
    val trackEveryNthFrame: Int = 1,
    val useGpu: Boolean = false
)

sealed interface FilterError {
    data class InstallFailed(val filterId: String, val reason: String) : FilterError
    data class Unsupported(val filterId: String, val reason: String) : FilterError
    data class RenderFailed(val reason: String) : FilterError
}