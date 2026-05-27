package com.diegoruiz;

import com.diegoruiz.util.DataInitializer;

public class Main {
    public static void main(String[] args) {
        // Inicializar datos maestros si la DB está vacía
        try {
            DataInitializer.seed();
        } catch (Exception e) {
            System.err.println("Error al inicializar datos: " + e.getMessage());
        }
        
        App.main(args);
    }
}