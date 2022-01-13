package com.entity.unity.ui.health

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.animation.core.animateDpAsState
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.CreateFeed
import com.entity.unity.R
import com.entity.unity.adapter.FeedAdapter
import com.entity.unity.databinding.FragmentHealthBinding
import com.entity.unity.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.airbnb.lottie.LottieAnimationView


class HealthFragment : Fragment() {

    private lateinit var healthViewModel: HealthViewModel
    private var _binding: FragmentHealthBinding? = null
    private lateinit var adapter: FeedAdapter
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var postsList:ArrayList<Post>
    private lateinit var animLoading:LottieAnimationView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        healthViewModel =
            ViewModelProvider(this).get(HealthViewModel::class.java)
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addFeed.setOnClickListener {
            val intent= Intent(requireActivity(),CreateFeed::class.java)
            startActivity(intent)
        }

        animLoading=view.findViewById(R.id.animLoading)

        val swipeRefreshLayout:SwipeRefreshLayout=view.findViewById(R.id.swipeRefresh)

        postsList=ArrayList()
        adapter= FeedAdapter(postsList,requireContext())
        feedRecyclerView=binding.feedRecyclerview
        feedRecyclerView.layoutManager= LinearLayoutManager(requireActivity())
        feedRecyclerView.adapter=adapter

        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            swipeRefreshLayout.setRefreshing(false)
            loadData()
        })

        val likeButton= getView()?.findViewById<ImageView>(R.id.thump_up)
        loadData()

    }

    fun loadData(){

        feedRecyclerView.visibility=View.GONE
        animLoading.visibility=View.VISIBLE
        postsList.clear()
        val db= FirebaseFirestore.getInstance()

        val storage = FirebaseStorage.getInstance()
        db.collection("Feed").get().addOnSuccessListener{
            val list: MutableList<DocumentSnapshot> = it.documents
            list.let {
                for(d in list) {
                    val id:String=d.id
                    val gsReference = storage.getReference("images/$id")
                    val post: Post=Post(
                        id,
                        d.get("uid").toString(),
                        d.get("description").toString(),
                        d.get("likes").toString(),
                        gsReference,
                        d.get("isLiked") as HashMap<String,Long>
                    )
                    postsList.add(post)
                }
                adapter.notifyDataSetChanged()
            }
            stopLoading()
        }
    }

    private fun stopLoading(){
        Handler().postDelayed({
            feedRecyclerView.visibility=View.VISIBLE
            animLoading.visibility=View.GONE
        },3000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


