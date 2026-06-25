package com.example.demofacedetect

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point that exists to bootstrap Hilt for the whole offline face-recognition app.
 * It owns no feature logic; keeping it small makes future startup work, such as encrypted database
 * initialization or model warm-up, easy to add without coupling UI and infrastructure layers.
 */
@HiltAndroidApp
class FacePeopleApplication : Application()
