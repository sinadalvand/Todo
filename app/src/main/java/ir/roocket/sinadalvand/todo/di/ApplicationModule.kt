package ir.roocket.sinadalvand.todo.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import ir.roocket.sinadalvand.todo.data.local.Valutor

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideValutor(@ApplicationContext context: Context): Valutor =  PrefValuetor(context)

}