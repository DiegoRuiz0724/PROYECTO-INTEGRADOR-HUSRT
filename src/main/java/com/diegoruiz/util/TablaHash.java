package com.diegoruiz.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementación de una Tabla Hash con encadenamiento para el Proyecto Integrador.
 * Reemplaza el uso de HashMap (mockup).
 */
public class TablaHash<K, V> {

    private static final int TAMANIO_INICIAL = 16;
    private LinkedList<Entrada<K, V>>[] cubetas;
    private int size = 0;

    private static class Entrada<K, V> {
        K clave;
        V valor;

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    @SuppressWarnings("unchecked")
    public TablaHash() {
        cubetas = new LinkedList[TAMANIO_INICIAL];
        for (int i = 0; i < TAMANIO_INICIAL; i++) {
            cubetas[i] = new LinkedList<>();
        }
    }

    private int obtenerIndice(K clave) {
        return Math.abs(clave.hashCode() % cubetas.length);
    }

    public void insertar(K clave, V valor) {
        int indice = obtenerIndice(clave);
        for (Entrada<K, V> entrada : cubetas[indice]) {
            if (entrada.clave.equals(clave)) {
                entrada.valor = valor;
                return;
            }
        }
        cubetas[indice].add(new Entrada<>(clave, valor));
        size++;
    }

    public V obtener(K clave) {
        int indice = obtenerIndice(clave);
        for (Entrada<K, V> entrada : cubetas[indice]) {
            if (entrada.clave.equals(clave)) {
                return entrada.valor;
            }
        }
        return null;
    }

    public V eliminar(K clave) {
        int indice = obtenerIndice(clave);
        Entrada<K, V> aEliminar = null;
        for (Entrada<K, V> entrada : cubetas[indice]) {
            if (entrada.clave.equals(clave)) {
                aEliminar = entrada;
                break;
            }
        }
        if (aEliminar != null) {
            cubetas[indice].remove(aEliminar);
            size--;
            return aEliminar.valor;
        }
        return null;
    }

    public Collection<V> valores() {
        List<V> listaValores = new ArrayList<>();
        for (LinkedList<Entrada<K, V>> cubeta : cubetas) {
            for (Entrada<K, V> entrada : cubeta) {
                listaValores.add(entrada.valor);
            }
        }
        return listaValores;
    }

    public int size() {
        return size;
    }
}
