package com.example.pantho

import androidx.core.content.FileProvider

class CameraFileProvider: FileProvider() {
    companion object {
        const val PROVIDER_NAME = "CameraFileProvider"
    }
}