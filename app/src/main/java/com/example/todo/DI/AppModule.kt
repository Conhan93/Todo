package com.example.todo.DI

import android.app.Application
import androidx.room.Room
import com.example.todo.Data.TodoDatabase
import com.example.todo.Data.TodoRepository
import com.example.todo.Data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app : Application) : TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(database : TodoDatabase) : TodoRepository {
        return TodoRepositoryImpl(database.dao)
    }
}