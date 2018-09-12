package com.example.testmid.models

import android.os.Parcel
import android.os.Parcelable

class Movie(

        val title: String,
        val poster_path: String,
        val overview: String,
        val release_date: String)  {
    override fun toString(): String {
        return "Movie(title='$title', poster_path='$poster_path', overview='$overview', release_date='$release_date')"
    }
}
