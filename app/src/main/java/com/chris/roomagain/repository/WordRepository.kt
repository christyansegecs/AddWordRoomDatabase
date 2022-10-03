package com.chris.roomagain.repository

import androidx.annotation.WorkerThread
import com.chris.roomagain.database.daos.WordDao
import com.chris.roomagain.database.models.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}