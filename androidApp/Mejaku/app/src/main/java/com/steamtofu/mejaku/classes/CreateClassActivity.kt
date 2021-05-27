package com.steamtofu.mejaku.classes

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ActivityCreateClassBinding


class CreateClassActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreateClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseFile.setOnClickListener(this)
        binding.btnCreateClass.setOnClickListener(this)

    }

    // using uri path to do something here
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        binding.tvFilePath.text = uri?.path
        Log.d("uri", "onContentTake: ${uri?.path}")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_choose_file -> {
                getContent.launch("*/*")
            }
            R.id.btn_create_class -> {
            }
        }
    }
}