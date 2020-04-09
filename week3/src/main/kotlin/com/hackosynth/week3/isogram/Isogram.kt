package com.hackosynth.week3.isogram

object Isogram {
    fun isIsogram(input: String): Boolean {
        return input.toLowerCase().replace("[ -]".toRegex(), "").let {
            it.toSet().size == it.length
        }
    }
}