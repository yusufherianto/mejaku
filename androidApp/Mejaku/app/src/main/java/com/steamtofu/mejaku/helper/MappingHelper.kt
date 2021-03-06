package com.steamtofu.mejaku.helper

import android.database.Cursor
import com.steamtofu.mejaku.db.DatabaseContract
import com.steamtofu.mejaku.entity.classes.ClassData

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