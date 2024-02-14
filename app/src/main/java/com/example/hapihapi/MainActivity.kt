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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.hapihapi.ui.theme.*
import java.util.*
import androidx.compose.material3.Text
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
                                        text = "Proximity Sensor",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) {
                        ProximitySensor()
                    }
                }
            }
        }
    }
}

@Composable
fun ProximitySensor() {

    val ctx = LocalContext.current

    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    val sensorStatus = remember {
        mutableStateOf("")
    }

    val proximitySensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {

                if (event.values[0] == 0f) {

                    sensorStatus.value = "Near"
                } else {
                    sensorStatus.value = "Away"
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

        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()

            .padding(5.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Object is",
            color = Color.Black,

            fontWeight = FontWeight.Bold,

            fontFamily = FontFamily.Default,

            fontSize = 40.sp, modifier = Modifier.padding(5.dp)
        )

        Text(
            text = sensorStatus.value,
            color = Color.Black,

            fontWeight = FontWeight.Bold,

            fontFamily = FontFamily.Default,

            fontSize = 40.sp, modifier = Modifier.padding(5.dp)
        )
        Text(
            text = "Sensor",
            color = Color.Black,

            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,

            fontSize = 40.sp, modifier = Modifier.padding(5.dp)
        )
    }
}
