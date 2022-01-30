package com.entity.unity.ui.memes

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.entity.unity.R
import com.entity.unity.databinding.FragmentMemesBinding


class MemesFragment : Fragment() {

    private var _binding: FragmentMemesBinding? = null
    private var currentImgUrl :String? = null
    private var subreddittext : String = "Unknown Subreddit"

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.subredditText.text = subreddittext
        loadMeme()
        binding.nextBtn.setOnClickListener {
            loadMeme()
        }
        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Checkout this awesome meme from reddit : $currentImgUrl" )
            val chooser =Intent.createChooser(intent , "Select an app :")
            startActivity(chooser)
        }
    }
    fun loadMeme(){
        binding.memeProgressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(requireContext())
        val api_link = "https://meme-api.herokuapp.com/gimme"

        // Request a jsonObject response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            api_link,
            null,
            { response ->
                currentImgUrl = response.getString("url")
                subreddittext = response.getString("subreddit")
                binding.subredditText.text = subreddittext

                Glide.with(this).load(currentImgUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.memeProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.memeProgressBar.visibility = View.GONE
                        return false
                    }
                }).into(binding.memeImg)

            },
            {
                Toast.makeText(requireContext() , "oops try again :(" , Toast.LENGTH_SHORT ).show()
            }
        )

        // Add the request to the RequestQueue.[singleton not implemented because this is the only queue in the project
        queue.add(jsonObjectRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}