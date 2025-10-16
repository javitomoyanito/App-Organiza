package com.example.proyectobase.funciones

object OpMatematicas {
    fun sumar(valor1: Int, valor2 : Int): Int{
        return valor1 + valor2
    }

    fun restar(valor1: Int, valor2 : Int): Int{
        return valor1 - valor2
    }

    fun multiplicar(valor1: Int, valor2 : Int): Int{
        return valor1 * valor2
    }

    fun dividir(valor1: Int, valor2 : Int): Int {
        try {
            return valor1 / valor2
        }
        catch (e: ArithmeticException){
            return 101111
        }
        finally {
            println("CONTROLANDON ERROR DIVISION 0")
        }
    }
}