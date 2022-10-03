package com.chris.roomagain

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chris.roomagain.application.WordsApplication
import com.chris.roomagain.database.models.Word
import com.chris.roomagain.databinding.ActivityMainBinding
import com.chris.roomagain.ui.adapters.WordListAdapter
import com.chris.roomagain.ui.viewmodels.WordViewModel
import com.chris.roomagain.ui.viewmodels.WordViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordListAdapter
    private lateinit var binding: ActivityMainBinding
    private val wordViewModel : WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.recyclerView = this.binding.recyclerview
        this.adapter = WordListAdapter()
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let{
                    it.getStringExtra("REPLY")?.let { it1 -> Word(it1) }?.let { it2 ->
                        wordViewModel.insert(
                            it2
                        )
                    }
                }
            }
        }
        this.binding.fab.setOnClickListener {

            resultLauncher.launch(Intent(this, NewWordActivity::class.java))

        }
    }

    override fun onStart() {
        super.onStart()

        wordViewModel.allWords.observe(this) { words ->
            words?.let { adapter.submitList(it) }
        }
    }
}