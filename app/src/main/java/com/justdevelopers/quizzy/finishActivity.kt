package com.justdevelopers.quizzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class finishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        val tvName= findViewById<TextView>(R.id.tvName)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        tvName.text=intent.getStringExtra(Constants.USER_NAME)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS,0)
        val correctAns=intent.getIntExtra(Constants.CORRECT_ANSWERS,0)
        tvScore.text="Score is $correctAns out of $totalQuestions"

        findViewById<Button>(R.id.btnFinish).setOnClickListener {
            finishAfterTransition()
        }
    }
}