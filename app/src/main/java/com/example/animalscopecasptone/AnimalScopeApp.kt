package com.example.animalscopecasptone

import android.app.Application
import android.content.Context
import com.example.animalscopecasptone.repository.AuthRepository
import com.example.animalscopecasptone.retorfit.ApiConfig
import com.example.animalscopecasptone.retorfit.ApiService
import com.example.animalscopecasptone.viewModel.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AnimalScopeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        startKoin {
            androidLogger()
            androidContext(this@AnimalScopeApp)
            modules(vmModule, repositoryModule)
        }
    }

    private val vmModule = module {
        viewModel { AuthViewModel(get()) }
    }

    private val repositoryModule = module {
        single { ApiConfig.createService<ApiService>() }
        single { AuthRepository(get()) }
    }

    companion object {
        lateinit var context: Context
    }
}