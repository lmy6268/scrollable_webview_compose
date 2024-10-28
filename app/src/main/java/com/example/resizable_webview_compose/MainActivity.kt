package com.example.resizable_webview_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.resizable_webview_compose.ui.theme.ResizableWebViewComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResizableWebViewComposeTheme {
                ResizableWebViewScreen(Modifier.fillMaxSize(), url = "https://www.naver.com")
            }
        }
    }
}


