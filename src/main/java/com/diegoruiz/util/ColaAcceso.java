package com.diegoruiz.util;

import java.util.LinkedList;

/**
 * Implementación de una Cola (FIFO) para el Proyecto Integrador.
 */
public class ColaAcceso<T> {
    private LinkedList<T> lista = new LinkedList<>();

    public void encolar(T elemento) {
        lista.addLast(elemento);
    }

    public T desencolar() {
        if (estaVacia()) return null;
        return lista.removeFirst();
    }

    public boolean estaVacia() {
        return lista.isEmpty();
    }
}
