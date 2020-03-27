package hoge.hogehoge.myapplication.di

import dagger.Module

@Module
open class InfraModule {
    companion object {
        val newInstance: InfraModule = InfraModule()
    }
}
