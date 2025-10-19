package com.example.stackapp

/**
 * Stack data structure implementation (LIFO - Last In First Out)
 * Coded from scratch without using library functions as per requirements
 * Maximum size: 3 elements
 * Elements: integers 0-9
 */
class Stack {
    // Using an array to store stack elements (requirement: use array)
    private val data = IntArray(MAX_SIZE)

    // Top of stack index (-1 means empty stack)
    private var topIndex = -1

    companion object {
        const val MAX_SIZE = 3
    }

    /**
     * Push an element onto the stack
     * @param value the integer value to push (must be 0-9)
     * @return true if successful, false if stack is full
     */
    fun push(value: Int): Boolean {
        if (isFull()) {
            return false
        }
        topIndex++
        data[topIndex] = value
        return true
    }

    /**
     * Pop an element from the stack
     * @return the popped value, or null if stack is empty
     */
    fun pop(): Int? {
        if (isEmpty()) {
            return null
        }
        val value = data[topIndex]
        topIndex--
        return value
    }

    /**
     * Check if stack is empty
     * @return true if empty, false otherwise
     */
    fun isEmpty(): Boolean {
        return topIndex == -1
    }

    /**
     * Check if stack is full
     * @return true if full, false otherwise
     */
    fun isFull(): Boolean {
        return topIndex == MAX_SIZE - 1
    }

    /**
     * Get the current size of the stack
     * @return number of elements in the stack
     */
    fun size(): Int {
        return topIndex + 1
    }

    /**
     * Get the contents of the stack as a string representation
     * Format: [bottom ... top] with rightmost being top of stack
     * @return string representation of stack contents
     */
    fun getContents(): String {
        if (isEmpty()) {
            return "[ ]"
        }

        val contents = StringBuilder("[")
        for (i in 0..topIndex) {
            contents.append(data[i])
            if (i < topIndex) {
                contents.append(" ")
            }
        }
        contents.append("]")
        return contents.toString()
    }
}
