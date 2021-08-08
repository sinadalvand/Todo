package ir.roocket.sinadalvand.todo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.roocket.sinadalvand.todo.data.persistence.TodoDatabase
import ir.roocket.sinadalvand.todo.data.persistence.dao.TaskDao
import ir.roocket.sinadalvand.todo.data.persistence.dao.UpdateDao
import ir.roocket.sinadalvand.todo.data.persistence.dao.UserDao

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase =
        TodoDatabase.create(context)

    @Provides
    fun provideTaskDao(db: TodoDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: TodoDatabase): UserDao = db.userDao()

    @Provides
    fun provideUpdateDao(db: TodoDatabase): UpdateDao = db.updateDao()






}