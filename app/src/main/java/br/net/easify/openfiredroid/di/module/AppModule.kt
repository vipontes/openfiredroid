package br.net.easify.openfiredroid.di.module

import android.app.Application
import br.net.easify.openfiredroid.util.ServiceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesServiceHelper(application: Application): ServiceHelper {
        return ServiceHelper(application)
    }
}