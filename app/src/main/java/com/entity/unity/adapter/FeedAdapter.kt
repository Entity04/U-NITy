package com.entity.unity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entity.unity.R
import com.entity.unity.model.Post
import com.entity.unity.ui.health.HealthFragment


class FeedAdapter(private val posts: ArrayList<Post>, private val context: HealthFragment) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>()  {
        inner class FeedViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val description = view.findViewById<TextView>(R.id.postDescription)
            val postimage=view.findViewById<ImageView>(R.id.dp)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item , parent , false)
            val holder = FeedViewHolder(view)
            return holder
        }

        @SuppressLint("CheckResult")
        override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
            val curr_post = posts[position]
            holder.description.text = curr_post.desc
            Glide.with(context)
                .load(curr_post.gref)
                .centerCrop()
                .into(holder.postimage)

        }
        override fun getItemCount(): Int {
            return posts.size
        }
    }
