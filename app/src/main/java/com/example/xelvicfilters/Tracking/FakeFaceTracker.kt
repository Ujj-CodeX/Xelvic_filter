package com.example.xelvicfilters.tracking

import com.example.xelvicfilters.core.FaceFrame
import com.example.xelvicfilters.core.TrackerConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFaceTracker : FaceTracker {
    private val _frames = MutableStateFlow<FaceFrame?>(null)
    override val frames: Flow<FaceFrame?> = _frames
    override fun start(config: TrackerConfig) { /* real MediaPipe baad mein */ }
    override fun stop() {}
}