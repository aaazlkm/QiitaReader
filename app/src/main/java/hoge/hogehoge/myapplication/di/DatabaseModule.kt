package hoge.hogehoge.myapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import hoge.hogehoge.infra.database.QiitaDatabase
import javax.inject.Singleton

@Module
open class DatabaseModule {
    @Singleton
    @Provides
    fun provideQiitaDatabase(context: Context) = QiitaDatabase.create(context)

    @Provides
    fun provideQiitaArticleDao(qiitaDatabase: QiitaDatabase) = qiitaDatabase.articleDao()
}
