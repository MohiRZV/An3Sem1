package com.mohi.maparkingapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.*
import android.util.Log
import androidx.lifecycle.MutableLiveData

class InternetStatusReceiver: BroadcastReceiver() {
    private var internetCallback: InternetCallback? = null
    override fun onReceive(context: Context?, intent: Intent?){
        val noConnection = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        var stat = InternetStatus.ONLINE
        if(noConnection)
            stat = InternetStatus.OFFLINE
        if(stat!=InternetStatusLive.get()) {
            InternetStatusLive.status.postValue(stat)
            Log.d("Mohi", "receiver: internet status -> $stat")
            internetCallback?.onStatusChanged(stat)
        }
    }

    fun setOnCallbackReceivedListener(internetCallback: InternetCallback) {
        this.internetCallback = internetCallback
    }

    companion object {
        fun getInstance() = InternetStatusReceiver()
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