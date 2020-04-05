package hoge.hogehoge.myapplication.di

import dagger.Module
import dagger.Provides
import hoge.hogehoge.domain.usecase.QiitaUseCase
import hoge.hogehoge.domain.usecase.QiitaUseCaseImpl
import hoge.hogehoge.infra.repository.QiitaRepository

@Module
open class DomainModule {
    companion object {
        val instance: DomainModule = DomainModule()
    }

    @Provides
    fun provideQiitaUseCase(qiitaRepository: hoge.hogehoge.infra.repository.QiitaRepository): hoge.hogehoge.domain.usecase.QiitaUseCase = hoge.hogehoge.domain.usecase.QiitaUseCaseImpl(qiitaRepository)
}
