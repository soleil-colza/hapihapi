package com.example.hapihapi

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.hapihapi.ui.theme.*
import java.util.*
import androidx.compose.material3.Text
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HapiHapiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        text = "Hapi Hapi Sensor",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) {
                        ProximitySensorScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ProximitySensorScreen() {

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    var sensorStatus by remember { mutableStateOf("") }

    val videoPlayer = remember  {
        SimpleExoPlayer.Builder(context).build().apply {

            setMediaItem(
                MediaItem.Builder()
                    .setUri("android.resource://com.example.hapihapi/${R.raw.hapihapi}")
                    .build()
            )

            prepare()
        }
    }

    val chipichapaUri = "android.resource://com.example.hapihapi/${R.raw.chipichapa}"

    val proximitySensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    sensorStatus = "Near"

                    videoPlayer.setMediaItem(
                        MediaItem.Builder()
                            .setUri(chipichapaUri)
                            .build()
                    )

                    videoPlayer.prepare()
                    videoPlayer.play()
                } else {
                    sensorStatus = "Away"

                    videoPlayer.setMediaItem(
                        MediaItem.Builder()
                            .setUri("android.resource://${context.packageName}/${R.raw.hapihapi}")
                            .build()
                    )

                    videoPlayer.prepare()
                    videoPlayer.play()
                }
            }
        }
    }

    sensorManager.registerListener(
        proximitySensorEventListener,
        proximitySensor,
        SensorManager.SENSOR_DELAY_NORMAL
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Object is",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )

        Text(
            text = sensorStatus,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = videoPlayer
                }
            },
            modifier = Modifier.size(300.dp)
        )
    }
}