package com.example.todo.DI

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.todo.Data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestAppModule {

    @Provides
    fun provideInMemoryDB(@ApplicationContext context: Context): TodoDatabase {
        return Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    TodoDatabase::class.java
                )
                .allowMainThreadQueries()
                .build()
    }
}