package hoge.hogehoge.myapplication.di

import dagger.Module
import dagger.Provides
import hoge.hogehoge.myapplication.infra.api.qiita.QiitaService
import hoge.hogehoge.myapplication.infra.database.dao.ArticleDao
import hoge.hogehoge.myapplication.infra.repository.QiitaRepository
import hoge.hogehoge.myapplication.infra.repository.QiitaRepositoryImpl
import hoge.hogehoge.myapplication.infra.store.QiitaLocalStore
import hoge.hogehoge.myapplication.infra.store.QiitaLocalStoreImpl
import hoge.hogehoge.myapplication.infra.store.QiitaRemoteStore
import hoge.hogehoge.myapplication.infra.store.QiitaRemoteStoreImpl

@Module
open class InfraModule {
    companion object {
        val instance: InfraModule = InfraModule()
    }

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
