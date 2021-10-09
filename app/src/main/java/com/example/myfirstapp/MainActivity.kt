package com.example.myfirstapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.security.acl.LastOwnerException

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
const val FIRSTNAME = "com.example.myfirstapp.FIRSTNAME"
const val LASTNAME = "com.example.myfirstapp.LASTNAME"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun authWithStrava(view: View){
        val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", "72880")
            .appendQueryParameter("redirect_uri", "http://localhost")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter("scope", "activity:read")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, intentUri)
        startActivity(intent)
    }

    fun doSomeRequestStuff(view: View){
        view.isEnabled = false

        val queue = Volley.newRequestQueue(this)
        val url = "https://www.google.com"
        var message = ""

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                message = "Response is: ${response.substring(0, 500)}"
                val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
            },
            {
                message = "There was an error"
                val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)})

        queue.add(stringRequest)
    }

    // THANK YOU: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin/53141982#53141982
    fun doSomeBetterStuff(view: View){
        view.isEnabled = false

        val queue = Volley.newRequestQueue(this)
        val url = "https://www.strava.com/api/v3/athlete"

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    val firstName = response.getString("firstname")
                    val lastName = response.getString("lastname")
                    putExtra(FIRSTNAME, firstName)
                    putExtra(LASTNAME, lastName)
                    putExtra(EXTRA_MESSAGE, response.toString())
                }
                startActivity(intent)
            },
            {
                val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, "Nope")
                }
                startActivity(intent)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer 4528da320f002c90a039f5b88211cc3d0a518abf"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }
}