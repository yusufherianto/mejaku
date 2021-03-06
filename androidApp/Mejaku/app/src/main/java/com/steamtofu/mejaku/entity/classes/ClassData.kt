package com.steamtofu.mejaku.entity.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassData(
    var id: String = "0",
    var name: String = "none"
) : Parcelable
