package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val firstName: String? = intent.getStringExtra(FIRSTNAME)
        val lastName: String? = intent.getStringExtra(LASTNAME)

        val textView = findViewById<TextView>(R.id.textView).apply {
            text = message
        }
        val nameTextView = findViewById<TextView>(R.id.name).apply {
            text = getString(R.string.name_text, firstName, lastName)
        }
    }
}