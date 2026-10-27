package com.example.xelvicfilters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.xelvicfilters.camera.CameraXCameraSource
import com.example.xelvicfilters.engine.FilterViewModel
import com.example.xelvicfilters.ui.CameraScreen
import com.example.xelvicfilters.ui.theme.XelvicFiltersTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FilterViewModel by viewModels()
    private val camera by lazy { CameraXCameraSource(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XelvicFiltersTheme {
                CameraScreen(
                    engine = viewModel.engine,
                    camera = camera,
                    lifecycleOwner = this
                )
            }
        }
    }
}