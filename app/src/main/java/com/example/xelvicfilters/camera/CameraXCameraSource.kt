package com.example.xelvicfilters.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraXCameraSource(private val context: Context) : CameraSource {

    private val _front = MutableStateFlow(true)
    override val isFrontCamera: StateFlow<Boolean> = _front.asStateFlow()

    private val _state = MutableStateFlow<CameraState>(CameraState.Idle)
    override val state: StateFlow<CameraState> = _state.asStateFlow()

    private var provider: ProcessCameraProvider? = null
    private var owner: LifecycleOwner? = null
    private var surfaceProvider: Preview.SurfaceProvider? = null

    override fun bind(lifecycleOwner: LifecycleOwner, surfaceProvider: Preview.SurfaceProvider) {
        owner = lifecycleOwner
        this.surfaceProvider = surfaceProvider
        _state.value = CameraState.Starting
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener({
            try {
                provider = future.get()
                rebind()
            } catch (e: Exception) {
                _state.value = CameraState.Error(e.message ?: "Camera start failed")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun unbind() {
        provider?.unbindAll()
        _state.value = CameraState.Idle
    }

    override fun switchCamera() {
        _front.value = !_front.value
        rebind()
    }

    private fun rebind() {
        val p = provider ?: return
        val o = owner ?: return
        val sp = surfaceProvider ?: return
        try {
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(sp)
            val selector = if (_front.value) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            p.unbindAll()
            p.bindToLifecycle(o, selector, preview)
            _state.value = CameraState.Ready
        } catch (e: Exception) {
            _state.value = CameraState.Error(e.message ?: "Camera bind failed")
        }
    }
}