package com.example.demofacedetect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demofacedetect.presentation.FacePeopleApp
import com.example.demofacedetect.ui.theme.DemoFaceDetectTheme
import dagger.hilt.android.AndroidEntryPoint

/** Hosts the single-activity Compose app and delegates all feature work to navigation/view models. */
@AndroidEntryPoint class MainActivity : ComponentActivity(){ override fun onCreate(savedInstanceState: Bundle?){ super.onCreate(savedInstanceState); enableEdgeToEdge(); setContent{ DemoFaceDetectTheme{ FacePeopleApp() } } } }
