package com.steamtofu.mejaku.ui.createclass.createupdate

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ActivityCreateUpdateClassBinding
import com.steamtofu.mejaku.db.DatabaseContract
import com.steamtofu.mejaku.db.helper.ClassHelper
import com.steamtofu.mejaku.entity.classes.ClassData


class CreateUpdateClassActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreateUpdateClassBinding

    private var isEdit = false
    private var classData: ClassData? = null
    private var position: Int = 0
    private lateinit var classHelper: ClassHelper

    companion object{
        const val EXTRA_CLASS = "extra_class"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUpdateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        classHelper = ClassHelper.getInstance(applicationContext)
        classHelper.open()

        classData = intent.getParcelableExtra(EXTRA_CLASS)
        if (classData != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            classData = ClassData()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.update_class)
            btnTitle = getString(R.string.update)

            classData?.let {
                binding.edtClassName.setText(it.name)
            }

        } else {
            actionBarTitle = getString(R.string.create_class)
            btnTitle = getString(R.string.create_class)
        }

        supportActionBar?.title = actionBarTitle
        binding.btnCreateClass.text = btnTitle

        binding.btnCreateClass.setOnClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit)
            menuInflater.inflate(R.menu.class_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose){
            dialogTitle = "Cancel"
            dialogMessage = "Are you sure want to leave?"
        } else {
            dialogTitle = "Delete Class"
            dialogMessage = "Are you sure want to delete this class?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = classHelper.deleteById(classData?.id.toString()).toLong()
                    if (result > 0){
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@CreateUpdateClassActivity, "Failed to delete data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btn_create_class -> {
                val className = binding.edtClassName.text.toString().trim()

                if (className.isEmpty()){
                    binding.edtClassName.error = "Field can not be blank"
                    return
                }

                classData?.name = className

                val intent = Intent()
                intent.putExtra(EXTRA_CLASS, classData)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseContract.ClassColumns.NAME, className)

                if (isEdit) {
                    val result = classHelper.update(classData?.id.toString(), values).toLong()
                    if (result > 0){
                        setResult(RESULT_UPDATE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@CreateUpdateClassActivity, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result = classHelper.insert(values)

                    if (result > 0) {
                        classData?.id = result.toString()
                        setResult(RESULT_ADD, intent)
                        finish()
                    } else {
                        Toast.makeText(this@CreateUpdateClassActivity, "Failed to add data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}