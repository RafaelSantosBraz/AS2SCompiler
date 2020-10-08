/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

import java.util.ArrayDeque;

/**
 * generic high-performance thread-safe Stack implementation.
 *
 * @author Rafael Braz
 * @param <T> type of the stack's elements. Extends Object.
 */
public class SafeStack<T extends Object> {

    private final ArrayDeque<T> stack;

    public SafeStack() {
        stack = new ArrayDeque<>();
    }

    /**
     * Tests if this stack is empty.
     *
     * @return true or false.
     */
    public synchronized boolean empty() {
        return stack.isEmpty();
    }

    /**
     * Looks at the object at the top of this stack without removing it from the
     * stack.
     *
     * @return item.
     */
    public synchronized T peek() {
        return stack.peek();
    }

    /**
     * Removes the object at the top of this stack and returns that object as
     * the value of this function.
     *
     * @return item.
     */
    public synchronized T pop() {
        return stack.pop();
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param item the item to be included.
     * @return true if item is not null; false otherwise.
     */
    public synchronized boolean push(T item) {
        try {
            stack.push(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Pushes all items (from 0..(n-1)) onto the top of this stack. It checks
     * for null values before pushing.
     *
     * @param items array of items to be included.
     * @return true if all items are not null; false otherwise.
     */
    public synchronized boolean push(T[] items) {
        for (T item : items) {
            if (item == null) {
                return false;
            }
        }
        for (T item : items) {
            stack.push(item);
        }
        return true;
    }

}
