package com.example.android.dogchatbot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.dogchatbot.R
import com.example.android.dogchatbot.model.ChatMessage

class ChatMessageAdapter(context: Context, resource: Int, objects: ArrayList<ChatMessage>) :
    ArrayAdapter<ChatMessage>(context, resource, objects) {

    private val MY_MESSAGE: Int = 0;
    private val BOT_MESSAGE: Int = 1;
    private val BOT_IMAGE: Int = 2;

    override fun getItemViewType(position: Int): Int {

        val item: ChatMessage = getItem(position)!!

        return if(item.isMine){
            MY_MESSAGE
        }else if(item.isImage && !item.isMine){
            BOT_IMAGE
        }else{
            BOT_MESSAGE
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val viewType = getItemViewType(position)


        if(viewType == MY_MESSAGE){
            val view = LayoutInflater.from(context).inflate(R.layout.user_query_layout, parent, false)

            val textView = view.findViewById<TextView>(R.id.textUser)
            textView.text = getItem(position)?.content ?: "Item is null"
            return view
        }
        else if(viewType == BOT_MESSAGE){
            val view = LayoutInflater.from(context).inflate(R.layout.bots_reply_layout, parent, false)


            val textView = view.findViewById<TextView>(R.id.textBot)
            textView.text = getItem(position)?.content ?: "Item is null"
            return view
        }
        else if(viewType == BOT_IMAGE){
            val view = LayoutInflater.from(context).inflate(R.layout.bot_image_layout, parent, false)

            val imageView = view.findViewById<ImageView>(R.id.image)

            val imageUrl = getItem(position)?.content!!

            imageUrl.let {
                val imgUri = imageUrl.toUri()

                Glide.with(imageView.context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(imageView)
            }

            return view
        }

        convertView?.findViewById<TextView>(R.id.chatMessageView)?.setOnClickListener {
            Toast.makeText(context, "Clicked...", Toast.LENGTH_LONG).show()
        }

        return convertView!!
    }

    override fun getViewTypeCount(): Int {
        return 3
    }

}