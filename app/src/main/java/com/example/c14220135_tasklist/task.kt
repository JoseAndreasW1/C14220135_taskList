package com.example.c14220135_tasklist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class task(
    var nama: String,
    var tanggal: String,
    var deskripsi: String,
    var status: String,
    var statusJson:Boolean
): Parcelable
