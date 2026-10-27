package com.example.xelvicfilters.camera

import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.StateFlow

interface CameraSource {
    val isFrontCamera: StateFlow<Boolean>
    val state: StateFlow<CameraState>
    fun bind(lifecycleOwner: LifecycleOwner, surfaceProvider: Preview.SurfaceProvider)
    fun unbind()
    fun switchCamera()
}