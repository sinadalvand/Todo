package ir.roocket.sinadalvand.todo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import ir.roocket.sinadalvand.todo.data.local.Valutor

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun provideValutor(prefValuetor: PrefValuetor): Valutor

}