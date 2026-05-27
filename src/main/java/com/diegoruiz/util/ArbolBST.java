package com.diegoruiz.util;

import java.util.function.Function;

/**
 * Implementación de un Árbol Binario de Búsqueda (BST) para el Proyecto Integrador.
 * Reemplaza el uso de TreeMap (mockup).
 */
public class ArbolBST<K extends Comparable<K>, V> {

    private Nodo<K, V> raiz;

    private static class Nodo<K, V> {
        K clave;
        V valor;
        Nodo<K, V> izquierdo, derecho;

        Nodo(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    public void insertar(K clave, V valor) {
        raiz = insertarRecursivo(raiz, clave, valor);
    }

    private Nodo<K, V> insertarRecursivo(Nodo<K, V> actual, K clave, V valor) {
        if (actual == null) {
            return new Nodo<>(clave, valor);
        }

        int comparacion = clave.compareTo(actual.clave);
        if (comparacion < 0) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, clave, valor);
        } else if (comparacion > 0) {
            actual.derecho = insertarRecursivo(actual.derecho, clave, valor);
        } else {
            actual.valor = valor; // Actualizar si la clave ya existe
        }

        return actual;
    }

    public V buscar(K clave) {
        return buscarRecursivo(raiz, clave);
    }

    private V buscarRecursivo(Nodo<K, V> actual, K clave) {
        if (actual == null) {
            return null;
        }

        int comparacion = clave.compareTo(actual.clave);
        if (comparacion == 0) {
            return actual.valor;
        }

        return comparacion < 0 
            ? buscarRecursivo(actual.izquierdo, clave) 
            : buscarRecursivo(actual.derecho, clave);
    }
}
