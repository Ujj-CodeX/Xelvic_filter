package com.example.xelvicfilters.tracking

import com.example.xelvicfilters.core.FaceFrame
import com.example.xelvicfilters.core.TrackerConfig
import kotlinx.coroutines.flow.Flow

interface FaceTracker {
    val frames: Flow<FaceFrame?>   // null = chehra nahi mila (tracking loss)
    fun start(config: TrackerConfig)
    fun stop()
}