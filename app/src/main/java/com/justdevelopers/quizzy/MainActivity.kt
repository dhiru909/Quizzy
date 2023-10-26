package com.justdevelopers.quizzy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val categoryMap= mutableMapOf<String,Int>("General Knowledge" to 9,"Entertainment: Books" to 10
        ,"Entertainment: Film" to 11,"Entertainment: Music" to 12, "Entertainment: Musicals & Theatres" to 13,
        "Entertainment: Television" to 14, "Entertainment: Video Games" to 15,"Entertainment: Board Games" to 16,
            "Science & Nature" to 17,"Science: Computers" to 18, 	"Science: Mathematics" to 19, 	"Mythology" to 20,
            "Sports" to 21,"Geography" to 22,	"History" to 23,"Politics" to 24,"Art" to 25,"Celebrities" to 26,
            "Animals" to 27, 	"Vehicles" to 28,"Entertainment: Comics" to 29,"Science: Gadgets" to 30,
            "Entertainment: Japanese Anime & Manga" to 31, 	"Entertainment: Cartoon & Animations" to 32
        )
        val btnStart:Button = findViewById(R.id.btnStart)
        val etName:EditText=findViewById(R.id.etName)
        etName.setText(intent.getStringExtra("namee"))
        val etCategory:MaterialAutoCompleteTextView = findViewById(R.id.etCategory)
        val etDifficulty:MaterialAutoCompleteTextView = findViewById(R.id.etDifficulty)
        val difficulty = resources.getStringArray(R.array.difficulty)
        val arrayAdapterDifficulty=ArrayAdapter(this,R.layout.dropdown_item,difficulty)
        etDifficulty.setAdapter(arrayAdapterDifficulty)

        val arrayAdapterCategory = ArrayAdapter(this,R.layout.dropdown_item,categoryMap.keys.toList())
        etCategory.setAdapter(arrayAdapterCategory)
        etName.maxWidth=2
        findViewById<Button>(R.id.logoutButton).setOnClickListener{
            val loginPrefs = getSharedPreferences("login", MODE_PRIVATE)
            val editor = loginPrefs.edit()
            editor.putString("name", null)
            editor.apply()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        btnStart.setOnClickListener{
            if (etName.text.isEmpty()) {
                Toast.makeText(this,"please enter your Name",Toast.LENGTH_LONG).show()
            }else{
                val difficultyLower=etDifficulty.text.toString().toLowerCase()
                val intent = Intent(this,QuizQuestionActivity::class.java)
                Log.e("ques","https://opentdb.com/api.php?amount=10&category=${categoryMap[etCategory.text.toString()]}&type=multiple&difficulty=${difficultyLower}")
                intent.putExtra(Constants.URL,"https://opentdb.com/api.php?amount=10&category=${categoryMap[etCategory.text.toString()]}&type=multiple&difficulty=${difficultyLower}")
                intent.putExtra(Constants.USER_NAME,etName.text.toString())
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val difficulty = resources.getStringArray(R.array.difficulty)
        val arrayAdapter=ArrayAdapter(this,R.layout.dropdown_item,difficulty)
//        etDifficulty.setAdapter(arrayAdapter)
    }
}