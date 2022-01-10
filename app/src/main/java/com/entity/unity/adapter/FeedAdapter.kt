package com.entity.unity.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entity.unity.R
import com.entity.unity.model.Post
import java.io.File


class FeedAdapter(private val posts : ArrayList<Post>, private val context : Context) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>()  {
        inner class FeedViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val description = view.findViewById<TextView>(R.id.postDescription)
            val postimage=view.findViewById<ImageView>(R.id.dp)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item , parent , false)   //inflation layout as a view
            val holder = FeedViewHolder(view)                                         //passing the above view into view holder
            return holder                                                             //returning holder
        }

        override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
            val curr_post = posts[position]
            holder.description.text = curr_post.desc

            val localFile = File.createTempFile("images", ".jpeg")
            curr_post.gref!!.getFile(localFile)
                .addOnSuccessListener {
                val bitmap=BitmapFactory.decodeFile(localFile.absolutePath)
                    holder.postimage.setImageBitmap(bitmap)
                    Log.d("Error","Hey")
            }.addOnFailureListener {
                // Handle any errors
                    Log.d("Error",it.toString())
            }

        }
        override fun getItemCount(): Int {
            return posts.size
        }
    }
