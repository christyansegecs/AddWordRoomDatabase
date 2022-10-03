package com.chris.roomagain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chris.roomagain.database.daos.WordDao
import com.chris.roomagain.database.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao() : WordDao

    companion object {

        private const val DATABASE_NAME: String = "nome-do-banco-de-dados"

        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(scope: CoroutineScope, context: Context): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java, DATABASE_NAME
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {

            var word = Word("App")
            wordDao.insert(word)
            word = Word("Room Database")
            wordDao.insert(word)
            word = Word("Go add")
            wordDao.insert(word)
        }
    }
}