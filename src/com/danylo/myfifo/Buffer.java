package com.danylo.myfifo;

public class Buffer<E> {
    private static class Node<E> {
        E value;
        Node prev;
        Node next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node first;
    private Node last;
    private int size;
    private final int MAX_SIZE;

    public Buffer(int maxSize) {
        if (maxSize > 0) {
            this.MAX_SIZE = maxSize;
        } else {
            throw new IllegalArgumentException("Illegal size value: " + size);
        }
    }

    public synchronized void push(E elem) throws InterruptedException {
        while (size == MAX_SIZE) {
            this.wait();
        }
        if (size == 0) {
            first = new Node<>(elem);
            last = first;
        } else {
            Node<E> node = new Node<>(elem);
            first.prev = node;
            node.next = first;
            first = first.prev;
        }
        size++;
        this.notifyAll();
    }

    public synchronized E pop() throws InterruptedException {
        while (size == 0) {
            this.wait();
        }
        E out = (E) last.value;
        last.value = null;
        last = last.prev;
        size--;
        this.notifyAll();
        return out;
    }

    public int size() {
        return size;
    }
}
