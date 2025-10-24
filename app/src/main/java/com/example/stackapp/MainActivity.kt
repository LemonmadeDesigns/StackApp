package com.example.stackapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Stack instance (coded from scratch)
    private val stack = Stack()

    // UI components
    private lateinit var etInput: EditText
    private lateinit var btnPush: Button
    private lateinit var btnPop: Button
    private lateinit var btnQuit: Button
    private lateinit var tvStackDisplay: TextView
    private lateinit var tvErrors: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        etInput = findViewById(R.id.etInput)
        btnPush = findViewById(R.id.btnPush)
        btnPop = findViewById(R.id.btnPop)
        btnQuit = findViewById(R.id.btnQuit)
        tvStackDisplay = findViewById(R.id.tvStackDisplay)
        tvErrors = findViewById(R.id.tvErrors)

        // Set up button click listeners
        btnPush.setOnClickListener {
            handlePush()
        }

        btnPop.setOnClickListener {
            handlePop()
        }

        btnQuit.setOnClickListener {
            handleQuit()
        }

        // Initialize display
        updateStackDisplay()
        showMessage("Ready")
    }

    /**
     * Handle push operation
     * Reads value from input field and pushes to stack
     */
    private fun handlePush() {
        val inputStr = etInput.text.toString().trim()

        // Check if input is empty
        if (inputStr.isEmpty()) {
            showError("Error: Please enter a value (0-9)")
            return
        }

        // Validate that input is a single digit 0-9
        if (inputStr.length != 1 || !inputStr[0].isDigit()) {
            showError("Error: Value must be a single digit (0-9)")
            return
        }

        val value = inputStr.toInt()

        // Validate range
        if (value < 0 || value > 9) {
            showError("Error: Value must be between 0 and 9")
            return
        }

        // Attempt to push
        if (stack.push(value)) {
            showMessage("Success: $value pushed to stack")
            etInput.text.clear() // Clear input after successful push
        } else {
            showError("Error: Stack is FULL. Cannot push $value")
        }

        updateStackDisplay()
    }

    /**
     * Handle pop operation
     * Removes top element from stack
     */
    private fun handlePop() {
        val poppedValue = stack.pop()

        if (poppedValue != null) {
            showMessage("Success: $poppedValue popped from stack")
        } else {
            showError("Error: Stack is EMPTY. Cannot pop")
        }

        updateStackDisplay()
    }

    /**
     * Handle quit operation
     * Closes the application
     */
    private fun handleQuit() {
        showMessage("Exiting application...")
        finish() // Close the application
    }

    /**
     * Update the stack display TextView
     * Shows current stack contents and status (Empty/Full)
     */
    private fun updateStackDisplay() {
        val contents = stack.getContents()
        val status = when {
            stack.isEmpty() -> "Empty"
            stack.isFull() -> "Full"
            else -> "Normal (${stack.size()}/${Stack.MAX_SIZE})"
        }

        tvStackDisplay.text = "Stack: $contents\nStatus: $status"
    }

    /**
     * Show a regular message in the errors TextView
     */
    private fun showMessage(message: String) {
        tvErrors.text = message
    }

    /**
     * Show an error message in the errors TextView
     */
    private fun showError(errorMessage: String) {
        tvErrors.text = errorMessage
    }
}

