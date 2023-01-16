package mx.com.idel.showanimaldadm

import android.app.Application

/**
 * Creado por De la Cruz Hernández Idelfonso el 14/01/23
 */
class App : Application() {
    override fun onCreate(){
        super.onCreate()
        instance = this
    }
    companion object{
        lateinit var instance: App
            private set
    }
}