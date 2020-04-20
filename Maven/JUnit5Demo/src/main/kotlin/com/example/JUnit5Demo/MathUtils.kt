package com.example.JUnit5Demo

import kotlin.math.PI

class MathUtils {
    fun add(a:Int, b:Int): Int{
        return a+b
    }

    fun div(a:Int, b:Int): Int{
        return a/b
    }

    fun multiply(a:Int, b:Int): Int{
        return a*b
    }

    fun computeCircleArea(r:Int): Double {
        return PI*r*r
    }
}