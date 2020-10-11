package br.net.easify.openfiredroid.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import br.net.easify.openfiredroid.xmpp.XMPP

class MessageService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun start() {

    }

    private fun stop() {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return START_STICKY
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }
}