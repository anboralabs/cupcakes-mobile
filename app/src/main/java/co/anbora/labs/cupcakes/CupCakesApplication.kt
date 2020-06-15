package co.anbora.labs.cupcakes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CupCakesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}