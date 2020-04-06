package hoge.hogehoge.myapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.ImagesPlugin

@Module
open class MarkdownModule {
    @Provides
    fun provideMarkdown(context: Context) = Markwon.builder(context)
        .usePlugin(CorePlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(ImagesPlugin.create())
        .usePlugin(TaskListPlugin.create(context))
        .build()
}
