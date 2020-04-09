package com.hackosynth.week3.naturalnumber

enum class Classification {
    DEFICIENT, PERFECT, ABUNDANT
}

object NaturalNumber {
    fun classify(naturalNumber: Int): Classification {
        require(naturalNumber > 0)
        return when(naturalNumber.aliquotSum()) {
            in 0 until naturalNumber -> Classification.DEFICIENT
            naturalNumber -> Classification.PERFECT
            else -> Classification.ABUNDANT
        }
    }
}

private fun Int.aliquotSum() = (1 until this).filter { this % it == 0 }.sum()