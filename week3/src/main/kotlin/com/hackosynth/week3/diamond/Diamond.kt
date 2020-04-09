package com.hackosynth.week3.diamond

object Diamond {
    fun printToList(letter: Char): List<String> {
        return ('A'..letter).map {
            " ".repeat(letter - it) + it.toUpperCase() + " ".repeat(it - 'A')
        }.map {
            it + it.reversed().drop(1)
        }.let {
            it.plus(it.reversed().drop(1))
        }
    }
}