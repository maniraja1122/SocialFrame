package com.example.socialframe.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.PostAdapter
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding:FragmentHomeBinding= FragmentHomeBinding.inflate(inflater,container,false)
        var mymodel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mymodel.SetAllPosts()
        var adapter = PostAdapter(OpenModel.mycontext!!, OpenModel.AllPosts.value!!)
        binding.recyclerView.layoutManager=LinearLayoutManager(OpenModel.mycontext)
        binding.recyclerView.adapter=adapter
        binding.addbtn.setOnClickListener(){
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,CreatePost()).commit()
        }
        mymodel.AllPosts.observe(requireActivity(), Observer {
            CoroutineScope(Dispatchers.Main).launch { async {
                var adapter = PostAdapter(OpenModel.mycontext!!, it)
                binding.recyclerView.layoutManager = LinearLayoutManager(OpenModel.mycontext)
                binding.recyclerView.adapter = adapter
            }}
        })
        return binding.root
    }

}