package com.example.xelvicfilters.camera

sealed interface CameraState {
    data object Idle : CameraState
    data object Starting : CameraState
    data object Ready : CameraState
    data class Error(val message: String) : CameraState
}