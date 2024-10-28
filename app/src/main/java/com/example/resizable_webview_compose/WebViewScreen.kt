package com.example.resizable_webview_compose

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.View.OnScrollChangeListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ResizableWebViewScreen(
    modifier: Modifier = Modifier,
    url: String,
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val mH = maxHeight
        val mW = maxWidth

        var webViewHeight by remember { mutableStateOf(mH * 0.3f) }
        val animateHeight by animateDpAsState(targetValue = webViewHeight)

        var showFilterBar by remember { mutableStateOf(true) }
        Column(
            Modifier
                .fillMaxWidth()
                .height(animateHeight)
                .align(Alignment.BottomCenter)
        ) {
            DynamicWebView(
                url = url,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(7f),
            ) {
                webViewHeight = if (it) mH * 0.7f
                else mH * 0.5f
                showFilterBar = !it
            }
            AnimatedVisibility(
                visible = showFilterBar, modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Blue)
                )
            }
        }
        Box(
            Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .height(mH - webViewHeight)
                .align(Alignment.TopCenter),
        )
    }
}


@Composable
fun DynamicWebView(
    url: String,
    modifier: Modifier = Modifier,
    onChangeMoveWay: (Boolean) -> Unit,
) {
    val isThrottling = remember { AtomicBoolean(false) }
    var isMoveDown by remember { mutableStateOf(false) }
    val threshold = remember { 3 }
    val scope = rememberCoroutineScope()
    val listener = OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (!isThrottling.getAndSet(true)) {
            scope.launch {
                val newIsMoveDown = scrollY > oldScrollY
                val diff = abs(scrollY - oldScrollY)
                if (diff >= threshold && newIsMoveDown != isMoveDown) {
                    isMoveDown = newIsMoveDown
                }
                delay(300) // 1초 동안 추가 스크롤 이벤트 무시
                isThrottling.set(false)
                onChangeMoveWay(isMoveDown)
            }
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
                setOnScrollChangeListener(listener)
            }
        },
        modifier = modifier,
    )
}