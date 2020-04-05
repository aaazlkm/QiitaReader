package hoge.hogehoge.myapplication.di

import dagger.Module
import dagger.Provides
import hoge.hogehoge.infra.api.qiita.QiitaService
import hoge.hogehoge.infra.database.dao.ArticleDao
import hoge.hogehoge.infra.repository.QiitaRepository
import hoge.hogehoge.infra.repository.QiitaRepositoryImpl
import hoge.hogehoge.infra.store.QiitaLocalStore
import hoge.hogehoge.infra.store.QiitaLocalStoreImpl
import hoge.hogehoge.infra.store.QiitaRemoteStore
import hoge.hogehoge.infra.store.QiitaRemoteStoreImpl

@Module
open class InfraModule {
    companion object {
        val instance: InfraModule = InfraModule()
    }

    //region repository

    @Provides
    fun provideQiitaRepository(
        qiitaLocalStore: hoge.hogehoge.infra.store.QiitaLocalStore,
        qiitaRemoteStore: hoge.hogehoge.infra.store.QiitaRemoteStore
    ): hoge.hogehoge.infra.repository.QiitaRepository = hoge.hogehoge.infra.repository.QiitaRepositoryImpl(qiitaLocalStore, qiitaRemoteStore)

    //endregion

    //region store

    @Provides
    fun provideQiitaRemoteStore(
        qiitaService: hoge.hogehoge.infra.api.qiita.QiitaService
    ): hoge.hogehoge.infra.store.QiitaRemoteStore = hoge.hogehoge.infra.store.QiitaRemoteStoreImpl(qiitaService)

    @Provides
    fun provideQiitaLocalStore(
        qiitaArticleDao: hoge.hogehoge.infra.database.dao.ArticleDao
    ): hoge.hogehoge.infra.store.QiitaLocalStore = hoge.hogehoge.infra.store.QiitaLocalStoreImpl(qiitaArticleDao)

    //endregion
}
