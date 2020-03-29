package hoge.hogehoge.myapplication.di

import dagger.Module

@Module
open class DomainModule {
    companion object {
        val instance: DomainModule = DomainModule()
    }
}
