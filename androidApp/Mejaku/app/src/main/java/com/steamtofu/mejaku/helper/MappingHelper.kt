package com.steamtofu.mejaku.helper

import android.database.Cursor
import com.steamtofu.mejaku.classes.entity.ClassData
import com.steamtofu.mejaku.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(classCursor: Cursor?): ArrayList<ClassData>{
        val classesList = ArrayList<ClassData>()
        classCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.ClassColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.ClassColumns.NAME))
                classesList.add(ClassData(id.toString(), name))
            }
        }
        return classesList
    }

}