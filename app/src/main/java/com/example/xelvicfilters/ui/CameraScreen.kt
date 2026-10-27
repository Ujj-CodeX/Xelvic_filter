package com.example.xelvicfilters.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.xelvicfilters.camera.CameraSource
import com.example.xelvicfilters.camera.CameraState
import com.example.xelvicfilters.engine.FilterEngine

@Composable
fun CameraScreen(
    engine: FilterEngine,
    camera: CameraSource,
    lifecycleOwner: LifecycleOwner
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    val cameraState by camera.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            CameraPreview(camera, lifecycleOwner, Modifier.fillMaxSize())
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera permission chahiye filters dikhane ke liye.")
                Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                    Text("Allow camera")
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart).systemBarsPadding().padding(12.dp)
        ) {
            when (val s = cameraState) {
                is CameraState.Starting -> Text("Starting camera...")
                is CameraState.Error -> Text("Camera error: ${s.message}", color = MaterialTheme.colorScheme.error)
                else -> {}
            }
        }

        OutlinedButton(
            onClick = { camera.switchCamera() },
            modifier = Modifier.align(Alignment.TopEnd).systemBarsPadding().padding(12.dp)
        ) { Text("Flip") }

        FilterPanel(engine, Modifier.align(Alignment.BottomCenter))
    }
}