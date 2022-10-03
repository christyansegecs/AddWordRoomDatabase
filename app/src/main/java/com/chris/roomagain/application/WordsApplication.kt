package com.chris.roomagain.application

import android.app.Application
import com.chris.roomagain.database.WordRoomDatabase
import com.chris.roomagain.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { WordRoomDatabase.getDatabase(applicationScope,this) }
    val repository by lazy { WordRepository(database.wordDao())}
}