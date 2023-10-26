package com.justdevelopers.quizzy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justdevelopers.quizzy.Sql.DBHelper

class Signup : AppCompatActivity() {
    var name: EditText? = null
    var number: EditText? = null
    var email: EditText? = null
    var pass: EditText? = null
    var login: TextView? = null
    var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        name = findViewById<EditText>(R.id.textName)
        email = findViewById<EditText>(R.id.textEmail)
        pass = findViewById<EditText>(R.id.textPass)
        val signUpAcc = findViewById<Button>(R.id.btnSignUpAcc)
        dbHelper = DBHelper(this)
        signUpAcc.setOnClickListener {
            val name1 = name?.text.toString()
            val number1 = number?.text.toString()
            val email1 = email?.text.toString()
            val pass1 = pass?.text.toString()
            val b: Boolean = dbHelper!!.insetUserData(name1, number1, email1, pass1)
            if (b) {
                Toast.makeText(this@Signup, "Data inserted", Toast.LENGTH_SHORT).show()
                val i = Intent(this@Signup, Login::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@Signup, "Failed To insert Data", Toast.LENGTH_SHORT).show()
            }
        }
        login = findViewById(R.id.loginAcc)
        login?.setOnClickListener {
            val i = Intent(this@Signup, Login::class.java)
            startActivity(i)
            finish()
        }
    }
}