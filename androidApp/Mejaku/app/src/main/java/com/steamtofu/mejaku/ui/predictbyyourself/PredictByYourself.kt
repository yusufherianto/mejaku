package com.steamtofu.mejaku.ui.predictbyyourself

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ActivityPredictByYourselfBinding
import com.steamtofu.mejaku.helper.MlHelper
import org.tensorflow.lite.Interpreter

class PredictByYourself : AppCompatActivity() {

    private lateinit var _predictByYourselfBinding: ActivityPredictByYourselfBinding
    private lateinit var tflite: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _predictByYourselfBinding = ActivityPredictByYourselfBinding.inflate(layoutInflater)
        setContentView(_predictByYourselfBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        try {
            tflite = Interpreter(MlHelper.loadModeFile(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _predictByYourselfBinding.btnPredictScore.setOnClickListener {
            predictScore()
        }
    }

    private fun predictScore() {
        val quiz = _predictByYourselfBinding.edtQuiz.text.toString().trim().toInt().toFloat()
        val assignment1 =
            _predictByYourselfBinding.edtAssignment1.text.toString().trim().toInt().toFloat()
        val assignment2 =
            _predictByYourselfBinding.edtAssignment2.text.toString().trim().toInt().toFloat()
        val assignment3 =
            _predictByYourselfBinding.edtAssignment3.text.toString().trim().toInt().toFloat()
        val prediction = MlHelper.doInferenceByYourself(
            tflite,
            floatArrayOf(quiz, assignment1, assignment2, assignment3)
        )
        _predictByYourselfBinding.tvPredictedScore.text = "Predict Score : $prediction"

    }
}