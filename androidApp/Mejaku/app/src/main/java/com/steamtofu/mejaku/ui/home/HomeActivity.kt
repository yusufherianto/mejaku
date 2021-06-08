package com.steamtofu.mejaku.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.steamtofu.mejaku.databinding.ActivityHomeBinding
import com.steamtofu.mejaku.ui.createclass.MainClass
import com.steamtofu.mejaku.ui.predictbyyourself.PredictByYourself

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPredictByYourself.setOnClickListener {
            val intent = Intent(this, PredictByYourself::class.java)
            startActivity(intent)
        }

        binding.btnPredictPerClass.setOnClickListener{
            val intent = Intent(this, MainClass::class.java)
            startActivity(intent)
        }
    }
}