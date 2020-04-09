package com.hackosynth.week3.forth

object Forth {
    fun evaluate(vararg line: String): List<Int> =
        line.last().replaceWithWordsDefinition(line.toList().addWordsDefinition()).evaluate()

    private fun List<String>.addWordsDefinition(): Map<String, String> =
        filter { it.startsWith(":") }.fold(mapOf()) { acc, s ->
            acc.plus(s.getWordDefinition(acc))
        }

    private fun String.getWordDefinition(wordsDefinition: Map<String, String>): Pair<String, String> {
        val words = split(" ")
        val word = words[1]

        require(!word.all { it.isDigit() })

        val definition = (2 until words.size - 1).joinToString(" ") {
            wordsDefinition[words[it]].orEmpty().ifEmpty { words[it] }
        }

        return word to definition
    }

    private fun String.replaceWithWordsDefinition(wordsDefinition: Map<String, String>): String {
        return wordsDefinition.keys.fold(this) { acc, s ->
            acc.replace(s, wordsDefinition[s].toString(), true)
        }
    }

    private fun String.evaluate(): List<Int> {
        return split(" ").fold(emptyList()) { acc, s ->
            acc.evaluate(s)
        }
    }

    private fun List<Int>.evaluate(word: String): List<Int> {
        return when(val name = word.toUpperCase()) {
            in Operator.values().map { it.operatorName } -> {
                val operator = Operator.values().first() { it.operatorName == name }

                when {
                    isEmpty() -> throw RuntimeException("empty stack")
                    size < operator.requiredValues -> throw RuntimeException("only one value on the stack")
                    else -> operator.operatorFuncation(this)
                }
            }

            else -> {
                if(!word.all { it.isDigit() }) throw RuntimeException("undefined operator")

                plus(word.toInt())
            }
        }
    }

    enum class Operator(val operatorName: String, val requiredValues: Int, val operatorFuncation: (List<Int>) -> List<Int>) {
        ADD("+", 2, { it.calculate { a, b -> a + b } }),
        SUBTRACT("-", 2, { it.calculate { a, b -> a - b } }),
        MULTIPLY("*", 2, { it.calculate { a, b -> a * b } }),
        DIVIDE("/", 2, { it.calculate { a, b -> a / b } }),
        DUP("DUP", 1, { it.plus(it.last()) }),
        DROP("DROP", 1, { it.dropLast(1) }),
        SWAP("SWAP", 2, { it.dropLast(2).plus(it.takeLast(2).reversed()) }),
        OVER("OVER", 2, { it.plus(it.takeLast(2).first()) });

        companion object {
            private fun List<Int>.calculate(arithemtic: (Int, Int) -> Int): List<Int> {
                return try {
                    this.dropLast(2).plus(arithemtic(takeLast(2).first(), last()))
                } catch (e: ArithmeticException) {
                    throw RuntimeException("divide by zero")
                }
            }
        }
    }
}