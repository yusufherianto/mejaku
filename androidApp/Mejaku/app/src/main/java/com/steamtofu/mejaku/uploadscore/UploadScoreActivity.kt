package com.steamtofu.mejaku.uploadscore

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.steamtofu.mejaku.MainActivity
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.classes.CreateUpdateClassActivity
import com.steamtofu.mejaku.databinding.ActivityUploadScoreBinding

class UploadScoreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadScoreBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnChooseFile.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val dialogTitle = "Cancel"
        val dialogMessage = "Are you sure want to leave?"

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    // using uri path to do something here
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.tvFilePath.text = uri?.path
            Log.d("uri", "onContentTake: ${uri?.path}")
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_choose_file -> {
                getContent.launch("*/*")
            }
            R.id.btn_upload -> {
                Log.d("UPLOAD FILE", "onClick: UPLOADING FILE...")
            }
        }
    }
}