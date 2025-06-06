package com.example.project3.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic dynamic array (list) implementation.
 *
 * @param <E> the type of elements stored
 * @author YourName
 */
public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;

    /**
     * Constructs an empty list with an initial capacity of 4.
     */
    @SuppressWarnings("unchecked")
    public List() {
        objects = (E[]) new Object[4];
        size = 0;
    }

    /**
     * Finds the index of the given element in the list.
     * @param e the element to find
     * @return the index of the element if found, otherwise -1
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e))
                return i;
        }
        return -1;
    }

    /**
     * Expands the internal array by increasing its size by 4.
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newArray = (E[]) new Object[objects.length + 4];
        for (int i = 0; i < size; i++) {
            newArray[i] = objects[i];
        }
        objects = newArray;
    }

    /**
     * Checks whether the list contains a specific element.
     * @param e the element to check for
     * @return true if the element is in the list, otherwise false
     */
    public boolean contains(E e) {
        return find(e) != -1;
    }

    /**
     * Adds an element to the end of the list and resizes.
     * @param e the element to add
     */
    public void add(E e) {
        if (size == objects.length)
            grow();
        objects[size++] = e;
    }

    /**
     * Removes the first specified element from the list.
     * @param e the element to remove
     */
    public void remove(E e) {
        int idx = find(e);
        if (idx == -1)
            return;
        for (int i = idx; i < size - 1; i++) {
            objects[i] = objects[i + 1];
        }
        size--;
        objects[size] = null;
    }

    /**
     * Checks whether the list is empty.
     * @return true if the list is empty, otherwise false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retrieves size of list
     * @return size of list
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves the element at a specified index.
     * @param index the index of the element
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return objects[index];
    }

    /**
     * Replaces the element at the specified index with a new element.
     * @param index the index of the element to replace
     * @param e the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        objects[index] = e;
    }


    /**
     * Returns the index of the first occurrence of a specified element.
     * @param e the element to find
     * @return the index of the element if found, otherwise -1
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * Returns the internal array storing elements.
     * @return the internal array of elements
     */
    public E[] getObjects() {
        return objects;
    }

    /**
     * Returns an iterator over the elements in the list.
     * @return an iterator for this list
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * An iterator implementation for iterating through the list.
     */
    private class ListIterator implements Iterator<E> {
        int current = 0;
        @Override
        public boolean hasNext() {
            return current < size;
        }
        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return objects[current++];
        }
    }

    /**
     * Returns a string representation of the list.
     * @return a string containing the elements in the list, separated by commas
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "The list is empty.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < size; i++) {
            sb.append(objects[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }



}
