package com.justdevelopers.quizzy

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class QuizQuestionActivity : AppCompatActivity(),View.OnClickListener {
    private var mCorrectAns: Int = 0
    private var mUserName: String? = null
    private var mCurrenPosition: Int = 1
    private lateinit var mQuestionsList: List<Result>
    private var mSelectedOption: String = ""
    private var svMain:ScrollView? = null
    private var progressBar: ProgressBar? = null
    private var tvProgressBar: TextView? = null
    private var tvQuestion: TextView? = null
    private var tvTimer: TextView? = null
    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null

    private var player: MediaPlayer? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null
    private var btnSubmit: Button? = null
    private var ansToOptions: MutableMap<String,Int>? = null
    private var mTimer:CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        svMain=findViewById(R.id.svMain)
        progressBar = findViewById(R.id.progressBar)
        tvProgressBar = findViewById(R.id.tvProgress)
        tvQuestion = findViewById(R.id.tvQuestion)
        tvOptionOne = findViewById(R.id.tvOptionOne)
        tvOptionTwo = findViewById(R.id.tvOptionTwo)
        tvOptionThree = findViewById(R.id.tvOptionThree)
        tvOptionFour = findViewById(R.id.tvOptionFour)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvTimer = findViewById(R.id.tvTimer)
        ansToOptions = mutableMapOf()
        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)
        val loader = Dialog(this)
        loader.setCanceledOnTouchOutside(false)
        loader.setContentView(R.layout.loader)
        loader.show()
        try{
            val soundUri =
                Uri.parse("android.resource://com.justdevelopers.quizzy/" + R.raw.tick
                )
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
        }catch (e:Exception){
            e.printStackTrace()
        }
        val stringRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Method.GET,
            intent.getStringExtra(Constants.URL),
            Response.Listener { response ->

                Log.e("ques",response.toString())
                Log.e("ques","\n")
//                Log.e("ques",responseNew)
                val questionResult = Gson().fromJson(response, QuestionNew::class.java)
                mQuestionsList = questionResult.results
                loader.dismiss()
                setQuestion()
            },
            Response.ErrorListener { error ->
                print("hero")
                loader.dismiss()
                Toast.makeText(this,"Network error try again",Toast.LENGTH_LONG).show()
                Log.e("ques",error.message.toString())
                finish()
            }){
            override fun getParams(): MutableMap<String, String> {

                // below line we are creating a map for storing
                // our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.

                // at last we are
                // returning our params.
                return params
            }
        }

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest)
    }
    internal fun setQuestion() {
        mTimer?.cancel()
        defaultOptionsView()
        mTimer=object : CountDownTimer(20000,500){
            override fun onTick(millisUntilFinished: Long) {
                tvTimer?.text =(millisUntilFinished/1000).toString()
                if(millisUntilFinished <= 7500.toLong()){
                    svMain?.setBackgroundColor(Color.parseColor("#E8B8B8"))
                    tvTimer?.setTextColor(Color.parseColor("#FF0000"))
                    player?.start()
                }else{
                    tvTimer?.setTextColor(Color.parseColor("#FF000000"))
                    svMain?.setBackgroundColor(Color.parseColor("#B8E8D1"))
                }

            }
            override fun onFinish() {
                mTimer?.cancel()
                onClick(btnSubmit)
            }

        }.start()
        val question = mQuestionsList[mCurrenPosition - 1]
        progressBar?.progress = mCurrenPosition
        tvProgressBar?.text = "$mCurrenPosition / ${progressBar?.max}"
        tvQuestion?.text = parseHTMLString(question.question)
        var options = listOf(question.incorrect_answers[0],question.incorrect_answers[1],question.incorrect_answers[2],question.correct_answer)
        options = options.shuffled()
        val opt1 = parseHTMLString(options[0])
        val opt2 = parseHTMLString(options[1])
        val opt3 = parseHTMLString(options[2])
        val opt4 = parseHTMLString(options[3])

        tvOptionOne?.text = opt1
        tvOptionTwo?.text = opt2
        tvOptionThree?.text = opt3
        tvOptionFour?.text = opt4
        ansToOptions?.set(opt1, 1)
        ansToOptions?.set(opt2, 2)
        ansToOptions?.set(opt3, 3)
        ansToOptions?.set(opt4, 4)
        if(mCurrenPosition== mQuestionsList.size+1){
            btnSubmit?.text="FINISH"
        }else{
            btnSubmit?.text="SUBMIT"
        }

    }
    private fun defaultOptionsView(){

        var options = ArrayList<TextView>()
        tvOptionOne?.let {
            options.add(0,it)
        }
        tvOptionTwo?.let {
            options.add(1,it)
        }
        tvOptionThree?.let {
            options.add(2,it)
        }
        tvOptionFour?.let {
            options.add(3,it)
        }
        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }
    private fun selectedOptionView(tv:TextView,selectedOption:String){
        defaultOptionsView()
        mSelectedOption = selectedOption
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }
    @SuppressLint("SetTextI18n")
    override fun onClick(view: View?) {
        if(view?.id==R.id.btnSubmit){

            if (mSelectedOption == "") {
                mTimer?.cancel()
                mCurrenPosition++
                when {
                    mCurrenPosition <= mQuestionsList?.size -> {
                        setQuestion()
                    }
                    else->{
                        val intent= Intent(this,finishActivity()::class.java)
                        intent.putExtra(Constants.USER_NAME,mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAns)
                        intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionsList?.size)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                mTimer?.cancel()
                player?.stop()
                val question = mQuestionsList[mCurrenPosition - 1]
//check if answer is correct or not
                if (parseHTMLString(question.correct_answer) != mSelectedOption) {
                    answerView(ansToOptions?.get(mSelectedOption)!!, R.drawable.wrong_option_border_bg)
                }
                else{
                    mCorrectAns++
                }
                answerView(ansToOptions!![parseHTMLString(question.correct_answer)]!!, R.drawable.correct_option_border_bg)
                if (mCurrenPosition == mQuestionsList.size) {
                    btnSubmit?.text = "FINISH"
                } else {
                    if(mCurrenPosition != mQuestionsList.size)
                    btnSubmit?.text = "Go to next Question"
                }
                mSelectedOption = ""
            }
        }
        if(btnSubmit?.text=="SUBMIT"||btnSubmit?.text=="FINISH"){
            when (view?.id) {
                R.id.tvOptionOne -> {
                    tvOptionOne?.let {
                        selectedOptionView(it, tvOptionOne?.text.toString())
                    }
                }
                R.id.tvOptionTwo -> {
                    tvOptionTwo?.let {
                        selectedOptionView(it, tvOptionTwo?.text.toString())
                    }
                }
                R.id.tvOptionThree -> {
                    tvOptionThree?.let {
                        selectedOptionView(it, tvOptionThree?.text.toString())
                    }
                }
                R.id.tvOptionFour -> {
                    tvOptionFour?.let {
                        selectedOptionView(it, tvOptionFour?.text.toString())
                    }
                }
            }
        }
    }
    private fun answerView(answer:Int,drawableView:Int){
        when(answer){
            1->{
                tvOptionOne?.background = ContextCompat.getDrawable(this,drawableView)
            }
            2->{
                tvOptionTwo?.background = ContextCompat.getDrawable(this,drawableView)
            }
            3->{
                tvOptionThree?.background = ContextCompat.getDrawable(this,drawableView)
            }
            4->{
                tvOptionFour?.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }
    private fun parseHTMLString(s:String ):String{
        return s.replace("&uuml;","ü").replace("&nbsp"," ")
            .replace("&amp;","&").replace("&quot;","\"")
            .replace("&ldquo;","“").replace("&rdquo;","”")
            .replace("&micro;","µ").replace("&#039;","'").replace("&eacute","é")
    }


    override fun onDestroy() {
        super.onDestroy()
        if(player!=null){
            player?.stop()
            player?.release()
            player = null
        }
        if(mTimer!=null){
            mTimer=null
        }
    }
}