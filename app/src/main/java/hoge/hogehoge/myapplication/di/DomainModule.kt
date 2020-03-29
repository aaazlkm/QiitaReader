package hoge.hogehoge.myapplication.di

import dagger.Module
import dagger.Provides
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCaseImpl
import hoge.hogehoge.myapplication.infra.repository.QiitaRepository

@Module
open class DomainModule {
    companion object {
        val instance: DomainModule = DomainModule()
    }

    @Provides
    fun provideQiitaUseCase(qiitaRepository: QiitaRepository): QiitaUseCase = QiitaUseCaseImpl(qiitaRepository)
}
