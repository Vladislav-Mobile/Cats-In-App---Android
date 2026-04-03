package com.example.catsinapp.ui.store

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

private const val TARGET_URL = "https://en.wikipedia.org/wiki/Cat"

// Безопасная проверка — ловим SecurityException если пермишн не выдан
private fun isConnected(context: Context): Boolean {
    return try {
        val cm      = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } catch (e: SecurityException) {
        // Пермишн не объявлен в манифесте — считаем что интернет есть
        // и даём WebView попробовать загрузить страницу
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
fun StoreScreen() {
    val context     = LocalContext.current
    var hasInternet by remember { mutableStateOf(true) }   // оптимистично — сначала true
    var isLoading   by remember { mutableStateOf(true) }
    var hasError    by remember { mutableStateOf(false) }
    var webViewRef  by remember { mutableStateOf<WebView?>(null) }

    // Проверяем после первого рендера
    LaunchedEffect(Unit) {
        hasInternet = isConnected(context)
    }

    fun retry() {
        hasError    = false
        isLoading   = true
        hasInternet = isConnected(context)
        webViewRef?.reload()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        when {
            !hasInternet -> NoInternetScreen(onRetry = { retry() })
            hasError     -> NoInternetScreen(
                title      = "Page not available",
                message    = "Failed to load the page. Check your connection and try again.",
                emoji      = "⚠️",
                buttonText = "Reload",
                onRetry    = { retry() }
            )
            else -> {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled    = true
                            settings.domStorageEnabled    = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort      = true
                            settings.builtInZoomControls  = true
                            settings.displayZoomControls  = false

                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isLoading = false
                                }
                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        isLoading   = false
                                        hasError    = true
                                        hasInternet = false
                                    }
                                }
                            }
                            webChromeClient = WebChromeClient()
                            loadUrl(TARGET_URL)
                            webViewRef = this
                        }
                    },
                    update   = { wv -> webViewRef = wv },
                    modifier = Modifier.fillMaxSize()
                )

                // Прелоадер
                if (isLoading && !hasError) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = GreenDark, strokeWidth = 3.dp)
                            Text("Loading article...", fontSize = 14.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoInternetScreen(
    title: String      = "No Internet Connection",
    message: String    = "Please check your Wi-Fi or mobile data and try again.",
    emoji: String      = "📡",
    buttonText: String = "Check Connection",
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 72.sp)
        Spacer(Modifier.height(24.dp))
        Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold,
            color = TextPrimary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(message, fontSize = 15.sp, color = TextSecondary,
            textAlign = TextAlign.Center, lineHeight = 22.sp)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick  = onRetry,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(26.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = GreenDark)
        ) {
            Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "The app will work offline. Go to another section.",
            fontSize = 12.sp, color = Color(0xFFBBBBBB), textAlign = TextAlign.Center
        )
    }
}