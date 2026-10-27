package com.example.xelvicfilters.ui

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.xelvicfilters.camera.CameraSource

@Composable
fun CameraPreview(
    camera: CameraSource,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        // factory sirf ek baar chalta hai, isliye bind bhi ek baar hota hai
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                camera.bind(lifecycleOwner, surfaceProvider)
            }
        }
    )
}