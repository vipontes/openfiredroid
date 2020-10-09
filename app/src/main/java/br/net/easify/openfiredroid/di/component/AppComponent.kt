package br.net.easify.openfiredroid.di.component

import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.di.module.AppModule
import br.net.easify.openfiredroid.di.module.DatabaseModule
import br.net.easify.openfiredroid.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun application(app: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: MainApplication)

    fun inject(viewModel: MainViewModel)

    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: ContactsViewModel)
    fun inject(viewModel: MessageViewModel)
    fun inject(viewModel: ProfileViewModel)
}