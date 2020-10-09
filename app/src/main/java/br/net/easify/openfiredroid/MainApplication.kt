package br.net.easify.openfiredroid

import android.app.Application
import br.net.easify.openfiredroid.di.component.AppComponent
import br.net.easify.openfiredroid.di.component.DaggerAppComponent
import br.net.easify.openfiredroid.di.module.AppModule
import br.net.easify.openfiredroid.xmpp.XMPP

class MainApplication : Application() {

    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        this.appComponent = DaggerAppComponent.builder()
            .application(AppModule(this))
            .build()
    }

    fun getAppComponent(): AppComponent? {
        return appComponent
    }

    override fun onTerminate() {
        super.onTerminate()
        XMPP.getXmpp(this)?.close()
    }
}