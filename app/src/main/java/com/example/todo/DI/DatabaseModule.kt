package com.example.todo.DI

import android.app.Application
import androidx.room.Room
import com.example.todo.Data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app : Application) : TodoDatabase {
        return Room
            .databaseBuilder(
                app,
                TodoDatabase::class.java,
                "todo_db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}