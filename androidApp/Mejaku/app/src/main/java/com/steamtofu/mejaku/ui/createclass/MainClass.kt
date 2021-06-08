package com.steamtofu.mejaku.ui.createclass

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ActivityMainBinding
import com.steamtofu.mejaku.db.helper.ClassHelper
import com.steamtofu.mejaku.entity.classes.entity.ClassData
import com.steamtofu.mejaku.helper.MappingHelper
import com.steamtofu.mejaku.ui.createclass.adapter.ClassAdapter
import com.steamtofu.mejaku.ui.createclass.createupdate.CreateUpdateClassActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainClass : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter: ClassAdapter
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvClass.layoutManager = LinearLayoutManager(this)
        binding.rvClass.setHasFixedSize(true)
        adapter = ClassAdapter(this)
        binding.rvClass.adapter = adapter

        binding.fabCreateClass.setOnClickListener(this)

        if (savedInstanceState == null){
            // get data
            loadClassAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<ClassData>(EXTRA_STATE)
            if (list != null){
                adapter.listClasses = list
            }
        }


    }

    private fun loadClassAsync() {
        GlobalScope.launch(Dispatchers.Main ) {
            binding.progressCircular.visibility = View.VISIBLE
            val classHelper = ClassHelper.getInstance(applicationContext)
            classHelper.open()
            val deferredClass = async (Dispatchers.IO){
                val cursor = classHelper.queryALL()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressCircular.visibility = View.INVISIBLE
            val classes = deferredClass.await()
            if (classes.size > 0){
                adapter.listClasses = classes
            } else {
                adapter.listClasses = ArrayList()
                showSnackbarMessagge("No Class yet")
            }

            classHelper.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fab_create_class -> {
                val intent = Intent(this, CreateUpdateClassActivity::class.java)
                startActivityForResult(intent, CreateUpdateClassActivity.REQUEST_ADD)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            when(requestCode) {
                CreateUpdateClassActivity.REQUEST_ADD -> {
                    if (resultCode == CreateUpdateClassActivity.RESULT_ADD){
                        val classData = data.getParcelableExtra<ClassData>(CreateUpdateClassActivity.EXTRA_CLASS) as ClassData

                        adapter.addItem(classData)
                        showSnackbarMessagge("Success add one item")
                    }
                }

                CreateUpdateClassActivity.REQUEST_UPDATE -> {
                    when (resultCode){
                        CreateUpdateClassActivity.RESULT_UPDATE -> {
                            val classData = data.getParcelableExtra<ClassData>(
                                CreateUpdateClassActivity.EXTRA_CLASS) as ClassData
                            val position = data.getIntExtra(CreateUpdateClassActivity.EXTRA_POSITION, 0)
                            adapter.updateItem(position, classData)
                            binding.rvClass.smoothScrollToPosition(position)
                            showSnackbarMessagge("Success update one item")
                        }
                        CreateUpdateClassActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(CreateUpdateClassActivity.EXTRA_POSITION, 0)
                            adapter.removeItem(position)
                            showSnackbarMessagge("Success delete one item")
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listClasses)
    }

    private fun showSnackbarMessagge(message: String) {
        Snackbar.make(binding.rvClass, message, Snackbar.LENGTH_SHORT).show()
    }
}