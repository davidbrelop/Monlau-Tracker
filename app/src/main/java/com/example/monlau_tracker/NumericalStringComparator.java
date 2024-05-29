package com.example.monlau_tracker;

import java.util.Comparator;

public class NumericalStringComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        // Extraer los números de las cadenas
        int num1 = extractNumber(s1);
        int num2 = extractNumber(s2);

        // Comparar los números
        if (num1 == num2) {
            // Si los números son iguales, comparar las cadenas completas
            return s1.compareTo(s2);
        }
        else {
            // Si los números son diferentes, comparar los números
            return Integer.compare(num1, num2);
        }
    }

    // Método para extraer números de una cadena
    private int extractNumber(String s) {
        String num = s.replaceAll("\\D", "");
        // Si la cadena está vacía después de eliminar los caracteres no numéricos, devolver 0
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
}