package com.mohi.examenma.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.*
import android.util.Log
import androidx.lifecycle.MutableLiveData

object InternetStatusReceiver: BroadcastReceiver() {
    private var internetCallback: InternetCallback? = null
    override fun onReceive(context: Context?, intent: Intent?){
        val noConnection = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        var stat = InternetStatus.ONLINE
        if(noConnection)
            stat = InternetStatus.OFFLINE
        if(stat!= InternetStatusLive.get()) {
            InternetStatusLive.status.postValue(stat)
            Log.d("Mohi", "receiver: internet status -> $stat")
            internetCallback?.onStatusChanged(stat)
        }
    }

    fun setOnCallbackReceivedListener(internetCallback: InternetCallback) {
        InternetStatusReceiver.internetCallback = internetCallback
    }
}

object InternetStatusLive {
    var status = MutableLiveData<InternetStatus>()
    fun get(): InternetStatus? {
        return status.value
    }
}

enum class InternetStatus {
    ONLINE, OFFLINE
}

interface InternetCallback {
    fun onStatusChanged(status: InternetStatus)
}