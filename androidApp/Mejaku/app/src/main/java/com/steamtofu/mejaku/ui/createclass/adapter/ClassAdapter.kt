package com.steamtofu.mejaku.ui.createclass.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.steamtofu.mejaku.R
import com.steamtofu.mejaku.databinding.ItemClassRowBinding
import com.steamtofu.mejaku.entity.classes.entity.ClassData
import com.steamtofu.mejaku.ui.createclass.createupdate.CreateUpdateClassActivity
import com.steamtofu.mejaku.ui.uploadscore.UploadScoreActivity
import com.steamtofu.mejaku.utils.CustomOnClickListener

class ClassAdapter(private val activity: Activity) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    var listClasses = ArrayList<ClassData>()
        set(listClasses){
            if (listClasses.size > 0){
                this.listClasses.clear()
            }
            this.listClasses.addAll(listClasses)

            notifyDataSetChanged()
        }

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemClassRowBinding.bind(itemView)
        fun bind(classData: ClassData) {
            binding.tvClassName.text = classData.name
            binding.btnEditClass.setOnClickListener(CustomOnClickListener(adapterPosition, object : CustomOnClickListener.OnItemClickCallback{
                override fun onItemClicked(view: View, position: Int) {
                    val intentEditClass = Intent(activity, CreateUpdateClassActivity::class.java)
                    intentEditClass.putExtra(CreateUpdateClassActivity.EXTRA_POSITION, position)
                    intentEditClass.putExtra(CreateUpdateClassActivity.EXTRA_CLASS, classData)
                    activity.startActivityForResult(intentEditClass, CreateUpdateClassActivity.REQUEST_UPDATE)
                }

            }))
            binding.itemRow.setOnClickListener(CustomOnClickListener(adapterPosition, object : CustomOnClickListener.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
                    val intent = Intent(activity, UploadScoreActivity::class.java)
                    activity.startActivity(intent)
                }

            }))
        }
    }

    fun addItem(classData: ClassData){
        this.listClasses.add(classData)
        notifyItemInserted(this.listClasses.size - 1)
    }

    fun updateItem(position: Int, classData: ClassData){
        this.listClasses[position] = classData
        notifyItemChanged(position, classData)
    }

    fun removeItem(position: Int) {
        this.listClasses.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listClasses.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class_row, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(listClasses[position])
    }

    override fun getItemCount(): Int = this.listClasses.size

}