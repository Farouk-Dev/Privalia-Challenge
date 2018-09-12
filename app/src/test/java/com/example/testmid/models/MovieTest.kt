package com.example.testmid.models

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieTest {


    private
    lateinit  var movie : Movie
    @Before
    fun setUp() {
        movie= Movie("title","image","overview","date")
    }



    @Test
    fun getTitle() {
        Assert.assertEquals("title",movie.title)
    }

    @Test
    fun getIntro() {
        Assert.assertEquals("image",movie.poster_path)
    }

    @Test
    fun getYear() {
        Assert.assertEquals("year",movie.release_date)
    }

    @Test
    fun getText() {
        Assert.assertEquals("overview",movie.overview)

    }
}