package com.entity.unity.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.R
import com.entity.unity.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.feed_item.view.*
import java.io.File


class FeedAdapter(private val posts: ArrayList<Post>, private val context: Context) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description = view.findViewById<TextView>(R.id.postDescription)
        val postimage = view.findViewById<ImageView>(R.id.dp)
        val likes = view.findViewById<TextView>(R.id.reactions)
        val thumpUp = view.findViewById<ImageView>(R.id.thump_up)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)   //inflation layout as a view
        val holder =
            FeedViewHolder(view)                                         //passing the above view into view holder
        return holder                                                             //returning holder
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val curr_post = posts[position]
        var like: Int = curr_post.like.toInt()

        holder.description.text = curr_post.desc
        holder.likes.text = curr_post.like
        val localFile = File.createTempFile("images", ".jpeg")

        curr_post.gref!!.getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                holder.postimage.setImageBitmap(bitmap)

            }.addOnFailureListener {
                // Handle any errors

                Log.d("Error", it.toString())
            }

        val userid = FirebaseAuth.getInstance().uid.toString()
        val isLiked = curr_post.likedBy

        if(userid.isNotEmpty()) {

            if (isLiked[userid] == 1L) {
                holder.thumpUp.setImageResource(R.drawable.ic_blue_thumb_up)
            }
        }


        holder.thumpUp.setOnClickListener {

            if (isLiked[userid]==1L) {
                val db = FirebaseFirestore.getInstance()
                db.collection("Feed").document(curr_post.id)
                    .update("likes", (--like).toString()).addOnSuccessListener {
                        holder.likes.text = like.toString()
                        holder.thumpUp.setImageResource(R.drawable.ic_thums_up)
                    }.addOnFailureListener {
                        Log.d("Error", "$it + ${curr_post.id}")
                    }
                isLiked[userid] = 0L
                db.collection("Feed").document(curr_post.id)
                    .update("isLiked", isLiked)

            } else {
                val db = FirebaseFirestore.getInstance()
                db.collection("Feed").document(curr_post.id)
                    .update("likes", (++like).toString()).addOnSuccessListener {
                        holder.likes.text = like.toString()
                        holder.thumpUp.setImageResource(R.drawable.ic_blue_thumb_up)
                    }.addOnFailureListener {
                        Log.d("Error", "$it + ${curr_post.id}")
                    }
                isLiked[userid] = 1L
                db.collection("Feed").document(curr_post.id)
                    .update("isLiked", isLiked)
            }
        }
        holder.itemView.ivDots.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.ivDots)
            popup.inflate(R.menu.dots_menu)
            popup.setOnMenuItemClickListener {
                val uid = FirebaseAuth.getInstance().uid.toString()
                if (uid == curr_post.uid) {
                    Log.d("Delete",uid+curr_post.uid)
                    val db = FirebaseFirestore.getInstance()
                    db.collection("Feed").document(curr_post.id).delete()
                    val storage =
                        FirebaseStorage.getInstance().reference.child("images/${curr_post.id}")
                    storage.delete()
                    posts.removeAt(position)
                    notifyItemRemoved(position)
                }
                else
                {
                    Toast.makeText(context,"You don't have Permission to delete this Post",Toast.LENGTH_LONG)
                        .show()
                }
                true
            }
            popup.show()
        }



    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
