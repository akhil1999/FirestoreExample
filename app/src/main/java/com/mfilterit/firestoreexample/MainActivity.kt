package com.mfilterit.firestoreexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mfilterit.firestoreexample.ui.theme.FirestoreExampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val personCollectionRef = Firebase.firestore.collection("persons")

    private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
        try{
            println("Save Person try block")
            personCollectionRef.add(person).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, "Successfully saved data", Toast.LENGTH_SHORT).show()
            }
        }catch(e : Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrievePerson() = CoroutineScope(Dispatchers.IO).launch {
        try{
            val querySnapshot = personCollectionRef.get().await()
            val sb = StringBuilder()
            for(document in querySnapshot){
                val person = document.toObject(Person::class.java)
                sb.append("$person\n")
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, sb.toString(), Toast.LENGTH_LONG).show()
            }
        }catch(e : Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstName = "Burgir"
        val lastName = "Lays"
        val age = 29
        val person = Person(firstName, lastName, age)
        enableEdgeToEdge()
        setContent {
            FirestoreExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                        FilledButtonExample {
                            savePerson(person)
                        }
                        FilledButtonExample2 {
                            retrievePerson()
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun textField(string : String){
    Text(
        text = string
    )
}

@Composable
fun FilledButtonExample2(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Retrieve Data")
    }
}

@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Save Data")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Firestore Example",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirestoreExampleTheme {
        Greeting("Android")
    }
}