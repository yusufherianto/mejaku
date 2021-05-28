package com.steamtofu.mejaku.db

import android.provider.BaseColumns

class DatabaseContract {

    internal class ClassColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "class"
            const val _ID = "_id"
            const val NAME = "name"
        }
    }

}