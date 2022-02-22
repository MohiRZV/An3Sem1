package com.mohi.examenma

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.gson.Gson
import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.viewmodel.EntitiesViewModel
import com.mohi.examenma.navigation.AppNavigation
import com.mohi.examenma.ui.theme.ExamenMATheme
import com.mohi.examenma.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity(), MessageListener {
    private var lastState = InternetStatus.OFFLINE
    val viewModel : EntitiesViewModel by viewModels()
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectToSocket()
        bcontext = applicationContext
        setContent {
            ExamenMATheme() {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
        initReceiver()
    }

    private fun connectToSocket() {
        WebSocketManager.init(baseUrl, this)
        WebSocketManager.connect(WebSocketManager.createListener())
    }

    private fun initReceiver() {
        InternetStatusReceiver.setOnCallbackReceivedListener(object : InternetCallback {
            override fun onStatusChanged(status: InternetStatus) {
                if(status!=lastState) {
                    when (status) {
                        InternetStatus.ONLINE -> {
                            Log.d("Mohi", "Back online!")
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.backOnline()
                            }
                        }
                        InternetStatus.OFFLINE -> {
                            Log.d("Mohi", "Went offline!")
                        }
                    }
                    lastState=status
                }
            }
        })
        registerReceiver(
            InternetStatusReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }
    companion object {
        lateinit var bcontext: Context
        const val baseUrl = "http://172.30.113.195:2028/"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(InternetStatusReceiver)
    }

    override fun onConnectSuccess() {
        Log.d("Mohi","Socket conn success")
    }

    override fun onConnectFailed() {
        Log.d("Mohi","Socket conn failed")
    }

    override fun onClose() {
        Log.d("Mohi","Socket close")
    }

    override fun onMessage(text: String?) {
        Log.d("Mohi","Recv brdcast $text")
        val entity = Gson().fromJson<Entity>(text, Entity::class.java)
        runOnUiThread {
            Toast.makeText(this, "Serverul a creat o noua inregistrare pentru ${entity.nume}, de la etajul ${entity.etaj} si camera ${entity.camera} ", Toast.LENGTH_LONG).show()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun App(){
    AppNavigation()
}

