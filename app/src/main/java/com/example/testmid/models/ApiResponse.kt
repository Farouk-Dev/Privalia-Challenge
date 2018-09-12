package com.example.testmid.models

class ApiResponse(val results :ArrayList<Movie>){
    override fun toString(): String {
        return "ApiResponse(results=$results)"
    }
}
