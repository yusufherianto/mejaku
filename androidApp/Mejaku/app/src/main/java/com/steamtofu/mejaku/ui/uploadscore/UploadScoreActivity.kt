package com.steamtofu.mejaku.ui.uploadscore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ActivityUploadScoreBinding
import com.steamtofu.mejaku.db.FirestoreHelper
import com.steamtofu.mejaku.entity.student.StudentScores
import com.steamtofu.mejaku.helper.CsvHelper
import com.steamtofu.mejaku.helper.MlHelper
import com.steamtofu.mejaku.helper.PathHelper
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream


class UploadScoreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadScoreBinding
    private lateinit var studentScoresList: ArrayList<StudentScores?>
    private var path: String? = null

    private lateinit var tflite: Interpreter
    private lateinit var firebaseDB: FirebaseFirestore

    // use Capstone Firebase Project
    private val options = FirebaseOptions.Builder()
        .setProjectId("mejaku")
        .setApplicationId("1:194229098802:android:31954067d4af205a9ddf2b")
        .setApiKey("AIzaSyCINPcGH28mEKGNeWib2f-c48eRWxfVj-0")
        .build()

    private var getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            path = PathHelper.getPathFromUri(applicationContext, uri)
            binding.tvFilePath.text = path
        }

    private val requestStoragePermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                uploadData()
            } else {
                Log.i("Permission: ", "Denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize secondary FirebaseApp.
        Firebase.initialize(this /* Context */, options, "mejakucapstone")
        // Retrieve secondary FirebaseApp.
        val mejakucapstone = Firebase.app("mejakucapstone")
        // Get the database for the other app.
        firebaseDB = FirebaseFirestore.getInstance(mejakucapstone)

        //tflite object
        try {
            tflite = Interpreter(MlHelper.loadModeFile(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.tvLinkSampleCsv.setOnClickListener(this)
        binding.btnChooseFile.setOnClickListener(this)
        binding.btnUpload.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_choose_file -> {
                getContent.launch("text/*")
            }
            R.id.btn_upload -> {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        this.applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        uploadData()
                    }
                    else -> {
                        requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
            R.id.tv_link_sample_csv -> {
                val uri = Uri.parse("https://drive.google.com/file/d/1ELsqCzSSjLYiKDpZ3puHc-3ynjkGCVH8/view?usp=sharing")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }


    private fun readCsv() {
        try {
            val csvFile = FileInputStream(path)
            studentScoresList = CsvHelper.read(csvFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadData() {
        if (!path.isNullOrEmpty()) {
            readCsv()
            for (student in studentScoresList) {
                if (student != null) {
                    val prediction = MlHelper.doInference(tflite, student)
                    student.scoresPrediction = prediction
                    FirestoreHelper.insertData(student, firebaseDB)
                    showSnackbarMessagge("Student scores successfully uploaded")
                }
            }
        } else {
            showSnackbarMessagge("Please choose csv file that contain tsudent data")
        }
    }


    override fun onBackPressed() {
        showAlertDialog()
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

    private fun showSnackbarMessagge(message: String) {
        Snackbar.make(binding.btnUpload, message, Snackbar.LENGTH_SHORT).show()
    }
}