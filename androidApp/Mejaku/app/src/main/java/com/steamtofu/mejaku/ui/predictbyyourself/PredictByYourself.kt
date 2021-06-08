package com.steamtofu.mejaku.ui.predictbyyourself

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.steamtofu.mejaku.R

class PredictByYourself : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_by_yourself)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}