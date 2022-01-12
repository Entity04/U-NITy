package com.entity.unity.ui.counsellor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.R
import com.entity.unity.adapter.ChatUserAdapter
import com.entity.unity.databinding.FragmentCounsellorBinding
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CounsellorFragment : Fragment() {

    private lateinit var counsellorViewModel: CounsellorViewModel
    private var _binding: FragmentCounsellorBinding? = null
    private lateinit var chatUserRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var adapter: ChatUserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var pbChat: ProgressBar


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        counsellorViewModel =
            ViewModelProvider(this).get(CounsellorViewModel::class.java)

        _binding = FragmentCounsellorBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbChat=view.findViewById(R.id.pbChat)
        mAuth= FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList=ArrayList()
        adapter= ChatUserAdapter(requireContext(),userList)
        chatUserRecyclerView=view.findViewById(R.id.rvChat)
        chatUserRecyclerView.layoutManager= LinearLayoutManager(requireActivity())
        chatUserRecyclerView.adapter=adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            //@SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid ){
                        userList.add(currentUser!!)
                        //Log.i("Data",currentUser.email.toString())
                    }
                }
                adapter.notifyDataSetChanged()
                pbChat.visibility= View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}