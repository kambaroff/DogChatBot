package com.example.android.dogchatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.android.dogchatbot.adapter.ChatMessageAdapter
import com.example.android.dogchatbot.api.DogApiService
import com.example.android.dogchatbot.model.ChatMessage
import com.example.android.dogchatbot.model.DogApiResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var btnSend: FloatingActionButton
    lateinit var editTextMsg: EditText
    lateinit var imageView: ImageView

    private lateinit var chatMessageAdapter: ChatMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        btnSend = findViewById(R.id.btnSend)
        editTextMsg = findViewById(R.id.editTextMsg)

        val message: ArrayList<ChatMessage> = ArrayList()

        chatMessageAdapter = ChatMessageAdapter(this, 0, message )
        listView.adapter = chatMessageAdapter



        botReply("How can I help you?", false)

        btnSend.setOnClickListener{

            val msg = editTextMsg.text.toString()

            sendMessage(msg)

            if(msg.isEmpty()){
                Toast.makeText(this, "Please enter a query", Toast.LENGTH_LONG).show()
            }
            else if(listOfKeyWords.contains(msg.toLowerCase(Locale.ENGLISH))){
                val listString = listOfBreeds.joinToString(separator = "\n", prefix = "List of all breeds:\n")
                botReply(listString, false)
            }
            else if (listOfBreeds.contains(msg.toLowerCase(Locale.ENGLISH))){
                if(msg.contains(" ")){
                        val msgArray = msg.split(" ")
                        val apiResponse = DogApiService.create().getRandomDogImageWithSB(msgArray[0], msgArray[1])
                        apiResponse.enqueue(object: Callback<DogApiResponse>{
                            override fun onFailure(call: Call<DogApiResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                            }
                            override fun onResponse(
                                call: Call<DogApiResponse>,
                                response: Response<DogApiResponse>
                            ) {
                                val imageUrl = response.body()?.message
                                botReply(imageUrl!!, true)
                            }
                        })
                }
                else {
                        val apiResponse = DogApiService.create().getRandomDogImage(msg)
                        apiResponse.enqueue(object: Callback<DogApiResponse>{
                        override fun onFailure(call: Call<DogApiResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(
                            call: Call<DogApiResponse>,
                            response: Response<DogApiResponse>
                        ) {
                            val imageUrl = response.body()!!.message
                            Toast.makeText(this@MainActivity, imageUrl, Toast.LENGTH_LONG).show()
                            botReply(imageUrl, true)
                        }
                    })
                }
            }


            editTextMsg.setText("")
            listView.setSelection(chatMessageAdapter.count - 1)


        }
    }

    private val listOfBreeds: List<String> = listOf("affenpinscher", "african", "airedale", "akita", "appenzeller", "australian", "basenji",
        "beagle", "bluetick", "borzoi", "bouvier", "boxer", "brabancon", "briard", "buhund", "bulldog" , "bullterrier", "cairn", "cattledog", "chihuahua",
        "chow", "clumber", "cockapoo", "collie", "coonhound", "corgi", "cotondetulear", "dachshund", "dalmatian", "dane", "deerhound", "dhole", "doberman",
        "elkhound", "entlebucher", "eskimo", "finnish", "frise", "germanshepherd", "greyhound", "groenendael", "havanese", "hound afghan", "hound basset",
        "hound blood", "hound english", "hound ibizan", "hound plott", "hound walker", "husky", "keeshond", "komondor", "kuvasz", "labrador", "leonberg",
        "lhasa","malamute", "malinois", "maltese", "mastiff bull", "mastiff english", "mastiff tibetan", "mexicanhairless", "mix", "mountain bernese",
        "mountain swiss", "newfoundland", "otterhound", "ovcharka caucasian", "papillon", "pekinese", "pembroke", "pinscher miniature", "pitbull",
        "pointer german", "pointer germanlonghair", "pomeranian", "poodle miniature", "poodle standard", "poodle toy", "pug", "puggle", "pyreness",
        "redbone", "retriever chesapeake", "retriever curly", "retriever flatcoated", "retriever golden", "rhodesian ridgeback", "rottweiler", "saluki",
        "samoyed", "schipperke", "schnauzer giant", "schnauzer miniature", "setter english", "setter gordon", "setter irish", "sheepdog english", "sheepdog shetland",
        "shiba", "shihtzu", "spaniel blenheim", "spaniel brittany", "spaniel cocker", "spaniel irish", "spaniel japanese", "spaniel sussex", "spaniel welsh",
        "springer english", "stbernard", "terrier american", "terrier australian", "terrier bedlington", "terrier border", "terrier dandie", "terrier fox",
        "terrier irish", "terrier kerryblue", "terrier lakeland", "terrier norfolk", "terrier patterdale", "terrier russell", "terrier scottish", "terrier sealyham",
        "terrier silky", "terrier tibetan", "terrier toy", "terrier westhighland", "terrier wheaten", "terrier yorkshire", "vizsla", "waterdog spanish",
        "weimaraner", "whippet", "wolfhound irish")

    private val listOfKeyWords: List<String> = listOf("list", "breeds", "breed")

    private fun sendMessage( message: String) {
        val chatMsg = ChatMessage(true, message, false)
        chatMessageAdapter.add(chatMsg)
    }

    private fun botReply(response: String, isImage: Boolean){
        val chatMsg = ChatMessage(false, response, isImage)
        chatMessageAdapter.add(chatMsg)
    }
}