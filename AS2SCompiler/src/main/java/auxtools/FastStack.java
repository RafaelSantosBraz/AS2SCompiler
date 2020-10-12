/*
 * The MIT License - https://github.com/RafaelSantosBraz/AS2SCompiler/blob/master/LICENSE
 * Copyright 2020 Rafael Braz.
 */
package auxtools;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * generic high-performance Stack implementation.
 *
 * @author Rafael Braz
 * @param <T> type of the stack's elements. Extends Object.
 */
public class FastStack<T extends Object> {

    private final ArrayDeque<T> stack;

    public FastStack() {
        stack = new ArrayDeque<>();
    }

    /**
     * builds a stack with a initial capacity.
     *
     * @param initialCapacity the initial capacity of the stack.
     */
    public FastStack(int initialCapacity) {
        stack = new ArrayDeque<>(initialCapacity);
    }

    /**
     * Tests if this stack is empty.
     *
     * @return true or false.
     */
    public boolean empty() {
        return stack.isEmpty();
    }

    /**
     * Looks at the object at the top of this stack without removing it from the
     * stack.
     *
     * @return item.
     */
    public T peek() {
        return stack.peek();
    }

    /**
     * Removes the object at the top of this stack and returns that object as
     * the value of this function.
     *
     * @return item.
     */
    public T pop() {
        try {
            return stack.pop();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param item the item to be included.
     * @return true if item is not null; false otherwise.
     */
    public boolean push(T item) {
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
    public boolean push(T[] items) {
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

    /**
     * Returns the number of elements in this stack.
     *
     * @return the number of elements in this stack.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns all elements of this stack as an ArrayList.
     *
     * @return all elements as an ArrayList.
     */
    public ArrayList<T> asList() {
        var res = new ArrayList<T>(stack.size());
        stack.forEach((t) -> {
            res.add(t);
        });
        return res;
    }
}
