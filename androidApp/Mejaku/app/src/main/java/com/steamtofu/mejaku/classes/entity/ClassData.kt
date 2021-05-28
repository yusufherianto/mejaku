package com.steamtofu.mejaku.classes.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassData(
    var id: String = "0",
    var name: String = "none"
) : Parcelable
