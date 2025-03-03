package com.example.ledcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ledcontrol.ui.theme.LEDControlTheme

class MainActivity : ComponentActivity() {
    private val connectionManager = ConnectionManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LEDControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LEDControlApp(connectionManager)
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        connectionManager.close()
    }
}

class ConnectionManager {
    private val client = OkHttpClient()
    private var isConnected = false
    
    fun connect(ip: String): Boolean {
        // TODO: Implement connection verification
        isConnected = true
        return true
    }
    
    fun sendCommand(params: Map<String, Int>): Boolean {
        return try {
            val url = "http://$ip/control"
            val paramString = params.map { (led, value) ->
                "$led=$value"
            }.joinToString("&")
            
            val request = Request.Builder()
                .url("$url?$paramString")
                .build()
            
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            false
        }
    }
    
    fun close() {
        client.dispatcher.executorService.shutdown()
    }
    
    fun isConnected(): Boolean = isConnected
}

@Composable
fun LEDControlApp() {
    var connectionStatus by remember { mutableStateOf("Disconnected") }
    
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Status: $connectionStatus",
            style = MaterialTheme.typography.headlineSmall
        )
        
        // TODO: Add manual and auto mode UI
    }
}
