package com.example.senderos4.helpers

class Validator(private val input: String) {
    private var conditions: MutableList<Pair<String, (String) -> Boolean>> = mutableListOf()

    fun isRequired(errorMessage: String): Validator {
        conditions.add(errorMessage to { it.isNotEmpty() })
        return this
    }


    fun minLength(length:Int, errorMessage: String):Validator {
        conditions.add(errorMessage to { it.length >= length})
        return this
    }

    fun maxLength(length: Int, errorMessage: String):Validator{
        conditions.add(errorMessage to {it.length <= length})
        return this
    }

    fun matches(regex: Regex, errorMessage: String): Validator {
        conditions.add(errorMessage to { it.matches(regex)})
        return this
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        conditions.forEach {(errorMessage, condition) ->
            if(!condition(input)) {
                errors.add(errorMessage)
            }
        }
        return  errors;
    }
}