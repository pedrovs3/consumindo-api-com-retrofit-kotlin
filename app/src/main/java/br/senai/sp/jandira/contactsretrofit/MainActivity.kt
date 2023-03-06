package br.senai.sp.jandira.contactsretrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.contactsretrofit.api.contacts.ContactRetrofitService
import br.senai.sp.jandira.contactsretrofit.api.contacts.RetrofitApi
import br.senai.sp.jandira.contactsretrofit.models.Contact
import br.senai.sp.jandira.contactsretrofit.ui.theme.ContactsRetrofitTheme
import retrofit2.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactsRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var nameState by remember {
        mutableStateOf("")
    }
    var emailState by remember {
        mutableStateOf("")
    }
    var phoneState by remember {
        mutableStateOf("")
    }
    var activeState by remember {
        mutableStateOf(false)
    }
    var visibleState by remember {
        mutableStateOf(false)
    }
    var idState by remember {
        mutableStateOf(0L)
    }

    val context = LocalContext.current

    val retrofit = RetrofitApi.getRetrofit()
    val contactsCall = retrofit.create(ContactRetrofitService::class.java)
    val call = contactsCall.getAllUsers()

    var contacts by remember {
        mutableStateOf(listOf<Contact>())
    }

    call.enqueue(object: Callback<List<Contact>> {
        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            Log.i("xxx", response.body().toString())
            contacts = response.body()!!
        }

        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            Log.i("eee", t.message.toString())
        }

    })

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Cadastro de contatos")
        OutlinedTextField(
            value = nameState, onValueChange = { nameState = it},
            Modifier.fillMaxWidth(),
            label = { Text(text = "Nome")}
        )
        OutlinedTextField(
            value = emailState, onValueChange = { emailState = it},
            Modifier.fillMaxWidth(),
            label = { Text(text = "Email")}
        )
        OutlinedTextField(
            value = phoneState, onValueChange = { phoneState = it},
            Modifier.fillMaxWidth(),
            label = { Text(text = "Telefone")}
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            Checkbox(
                checked = activeState,
                onCheckedChange = { activeState = it},
            )
            Text(text = "Ativo")
        }
        Button(
            onClick = {
                val contact = Contact(
                    name = nameState,
                    email = emailState,
                    active = activeState,
                    phone = phoneState
                )

                val callContactPost = contactsCall.createContact(contact)

                callContactPost.enqueue(object: Callback<Contact>{
                    override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                       Log.i("ds3m", response.body().toString())
                    }

                    override fun onFailure(call: Call<Contact>, t: Throwable) {
                        Log.i("ds3m", t.message.toString())
                    }

                })
            },
            Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        ) {
            Text(text = "Save new contact", Modifier.padding(5.dp))
        }
        AnimatedVisibility(visible = visibleState) {
            Button(
                onClick = {
                    if(idState !== 0L) {
                        val updateCall = contactsCall.updateUser(idState, Contact(
                            name = nameState,
                            email = emailState,
                            active = activeState,
                            phone = phoneState)
                        )

                        updateCall.enqueue(object: Callback<Contact>{
                            override fun onResponse(
                                call: Call<Contact>,
                                response: Response<Contact>
                            ) {
                                Log.i("xxx", response.body().toString())
                            }

                            override fun onFailure(call: Call<Contact>, t: Throwable) {
                                Log.i("xxx", t.message.toString())
                            }
                        })
                    }

                    val contact = Contact(
                        name = nameState,
                        email = emailState,
                        active = activeState,
                        phone = phoneState
                    )

                    val callContactPost = contactsCall.createContact(contact)

                    callContactPost.enqueue(object: Callback<Contact>{
                        override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                            Log.i("ds3m", response.body().toString())
                        }

                        override fun onFailure(call: Call<Contact>, t: Throwable) {
                            Log.i("ds3m", t.message.toString())
                        }

                    })
                },
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                Text(text = "Update user", Modifier.padding(5.dp))
            }
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()){
            items(contacts) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable {
                            idState = it.id
                            nameState = it.name
                            emailState = it.email
                            phoneState = it.phone
                            activeState = it.active
                            visibleState = true
                        }
                    ,
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Text(text = it.name)
                        Text(text = it.email)
                        Text(text = it.phone)
                        Button(
                            onClick = {
                                val deleteCall = contactsCall.deleteContact(it.id)

                                deleteCall.enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        call: Call<String>,
                                        response: Response<String>
                                    ) {
                                        Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {

                                    }
                                })
                            }
                        ) {
                            Text(text = "Deletar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ContactsRetrofitTheme {
        Greeting("Android")
    }
}