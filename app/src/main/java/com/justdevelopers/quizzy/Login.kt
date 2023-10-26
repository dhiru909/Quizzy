package com.justdevelopers.quizzy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.justdevelopers.quizzy.Sql.DBHelper

class Login : AppCompatActivity() {
    var email: EditText? = null
    var password: EditText? = null
    var btnSubmit: Button? = null
    var createAcc: TextView? = null
    var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val e = false
        val p = false
        setContentView(R.layout.activity_login)
        val namee = getSharedPreferences("login", MODE_PRIVATE).getString("name",null)
        if(namee!=null){
            val intent = Intent(this@Login, MainActivity::class.java)
            intent.putExtra("namee", namee)
            startActivity(intent)
            finish()
        }
        email = findViewById<EditText>(R.id.text_email)
        password = findViewById<EditText>(R.id.text_pass)
        btnSubmit = findViewById<Button>(R.id.btnSubmit_login)
        dbHelper = DBHelper(this)
        btnSubmit?.setOnClickListener {
            val emailCheck = email?.text.toString()
            val passCheck = password?.text.toString()
            val names: ArrayList<String> = dbHelper!!.getData(emailCheck, passCheck)
            Log.e("column", String.format("%s", names.toString()))
            if (names.size > 0) {
                val intent = Intent(this@Login, MainActivity::class.java)
                val loginPrefs = getSharedPreferences("login", MODE_PRIVATE)
                val editor = loginPrefs.edit()
                editor.putString("name", names[0])
                editor.apply()
                intent.putExtra("name", names[0])
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(this@Login)
                builder.setCancelable(true)
                builder.setTitle("Wrong Credential")
                builder.setMessage("Wrong Credential")
                builder.show()
            }
            dbHelper!!.close()
        }
        createAcc = findViewById<TextView>(R.id.createAcc)
        createAcc?.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
    } //    public static boolean loginCheck(Cursor cursor,String emailCheck,String passCheck) {
    //        while (cursor.moveToNext()){
    //            if (cursor.getString(0).equals(emailCheck)) {
    //                if (cursor.getString(2).equals(passCheck)) {
    //                    return true;
    //                }
    //                return false;
    //            }
    //        }
    //        return false;
    //    }
}