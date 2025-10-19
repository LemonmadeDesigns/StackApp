package com.example.stackapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {
    // Stack instance (coded from scratch)
    private val stack = Stack()

    // UI components
    private lateinit var etCommand: EditText
    private lateinit var btnSubmit: Button
    private lateinit var stackContainer: LinearLayout
    private lateinit var tvStackEmpty: TextView
    private lateinit var tvLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        etCommand = findViewById(R.id.etCommand)
        btnSubmit = findViewById(R.id.btnSubmit)
        stackContainer = findViewById(R.id.stackContainer)
        tvStackEmpty = findViewById(R.id.tvStackEmpty)
        tvLog = findViewById(R.id.tvLog)

        // Set up button click listener
        btnSubmit.setOnClickListener {
            processCommand()
        }

        // Set up Enter key listener on EditText
        etCommand.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processCommand()
                true
            } else {
                false
            }
        }

        // Initialize display
        updateStackDisplay()
        tvLog.text = "Ready. Enter a command."
    }

    /**
     * Process the command entered by the user
     * Commands: "push X" (X is 0-9), "pop", "quit"
     */
    private fun processCommand() {
        val command = etCommand.text.toString().trim().lowercase()

        if (command.isEmpty()) {
            showMessage("Please enter a command (push X, pop, or quit)")
            return
        }

        val parts = command.split(" ")
        val operation = parts[0]

        when (operation) {
            "push" -> handlePush(parts)
            "pop" -> handlePop()
            "quit" -> handleQuit()
            else -> showMessage("Invalid command. Use: push X (0-9), pop, or quit")
        }

        // Clear input field
        etCommand.text.clear()
    }

    /**
     * Handle push operation
     * Format: "push X" where X is 0-9
     */
    private fun handlePush(parts: List<String>) {
        // Check for "push8" style error (missing space)
        if (parts.size == 1 && parts[0].length > 4 && parts[0].startsWith("push")) {
            val attemptedValue = parts[0].substring(4)
            showMessage("Format Error: Missing space between 'push' and '$attemptedValue'. Use: push $attemptedValue")
            return
        }

        if (parts.size != 2) {
            showMessage("Invalid push format. Use: push X (where X is 0-9)")
            return
        }

        val valueStr = parts[1]

        // Validate that value is a single digit 0-9
        if (valueStr.length != 1 || !valueStr[0].isDigit()) {
            showMessage("Error: Value must be a single digit (0-9)")
            return
        }

        val value = valueStr.toInt()

        // Attempt to push
        if (stack.push(value)) {
            showMessage("$value is pushed. Stack ${stack.getContents()}")
        } else {
            showMessage("Stack is FULL. Stack ${stack.getContents()}")
        }

        updateStackDisplay()
    }

    /**
     * Handle pop operation
     */
    private fun handlePop() {
        val poppedValue = stack.pop()

        if (poppedValue != null) {
            showMessage("$poppedValue is popped. Stack ${stack.getContents()}")
        } else {
            showMessage("Stack is EMPTY. Stack ${stack.getContents()}")
        }

        updateStackDisplay()
    }

    /**
     * Handle quit operation
     */
    private fun handleQuit() {
        showMessage("Exiting application...")
        finish() // Close the application
    }

    /**
     * Update the stack display with colored blocks
     */
    private fun updateStackDisplay() {
        // Clear the container
        stackContainer.removeAllViews()

        if (stack.isEmpty()) {
            // Show empty message
            tvStackEmpty.visibility = TextView.VISIBLE
            stackContainer.addView(tvStackEmpty)
        } else {
            // Hide empty message and build colored blocks
            tvStackEmpty.visibility = TextView.GONE

            // Get stack data (we need to access the stack contents)
            // Since we can't directly access the array, we'll build from the string representation
            val contents = stack.getContents()
            val values = parseStackContents(contents)

            // Add blocks from bottom to top (reverse order for visual stack)
            for (i in values.indices.reversed()) {
                val blockView = createStackBlock(values[i])
                stackContainer.addView(blockView, 0) // Add at position 0 to build from bottom up
            }
        }
    }

    /**
     * Parse stack contents string to get individual values
     * Format: "[ ]" (empty) or "[5 3 7]" (with values)
     */
    private fun parseStackContents(contents: String): List<Int> {
        val trimmed = contents.trim()
        if (trimmed == "[ ]" || trimmed == "[]") {
            return emptyList()
        }

        // Remove brackets and split by spaces
        val inner = trimmed.substring(1, trimmed.length - 1).trim()
        return if (inner.isEmpty()) {
            emptyList()
        } else {
            inner.split(" ").map { it.toInt() }
        }
    }

    /**
     * Create a colored block for a stack value
     */
    private fun createStackBlock(value: Int): TextView {
        val block = TextView(this)

        // Set text
        block.text = value.toString()
        block.textSize = 24f
        block.setTextColor(Color.WHITE)
        block.gravity = Gravity.CENTER

        // Set background color based on value
        block.setBackgroundColor(getColorForValue(value))

        // Set layout parameters
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(60)
        )
        params.setMargins(0, 0, 0, dpToPx(8))
        block.layoutParams = params

        // Set padding
        block.setPadding(dpToPx(16))

        // Add elevation/shadow effect
        block.elevation = 4f

        return block
    }

    /**
     * Get a distinct color for each digit (0-9)
     */
    private fun getColorForValue(value: Int): Int {
        return when (value) {
            0 -> Color.parseColor("#E57373") // Red
            1 -> Color.parseColor("#F06292") // Pink
            2 -> Color.parseColor("#BA68C8") // Purple
            3 -> Color.parseColor("#9575CD") // Deep Purple
            4 -> Color.parseColor("#7986CB") // Indigo
            5 -> Color.parseColor("#64B5F6") // Blue
            6 -> Color.parseColor("#4FC3F7") // Light Blue
            7 -> Color.parseColor("#4DB6AC") // Teal
            8 -> Color.parseColor("#81C784") // Green
            9 -> Color.parseColor("#FFB74D") // Orange
            else -> Color.GRAY
        }
    }

    /**
     * Convert dp to pixels
     */
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /**
     * Show a message in the log TextView
     */
    private fun showMessage(message: String) {
        tvLog.text = "Output: $message"
    }
}

