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
    //region repository

    @Provides
    fun provideQiitaRepository(
        qiitaLocalStore: QiitaLocalStore,
        qiitaRemoteStore: QiitaRemoteStore
    ): QiitaRepository = QiitaRepositoryImpl(qiitaLocalStore, qiitaRemoteStore)

    //endregion

    //region store

    @Provides
    fun provideQiitaRemoteStore(
        qiitaService: QiitaService
    ): QiitaRemoteStore = QiitaRemoteStoreImpl(qiitaService)

    @Provides
    fun provideQiitaLocalStore(
        qiitaArticleDao: ArticleDao
    ): QiitaLocalStore = QiitaLocalStoreImpl(qiitaArticleDao)

    //endregion
}
