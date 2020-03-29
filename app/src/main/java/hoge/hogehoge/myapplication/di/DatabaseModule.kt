package hoge.hogehoge.myapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import hoge.hogehoge.myapplication.infra.database.QiitaDatabase
import javax.inject.Singleton

@Module
open class DatabaseModule {
    companion object {
        val instance = DatabaseModule()
    }

    @Singleton
    @Provides
    fun provideQiitaDatabase(context: Context) = QiitaDatabase.create(context)

    @Provides
    fun provideQiitaArticleDao(qiitaDatabase: QiitaDatabase) = qiitaDatabase.articleDao()
}
