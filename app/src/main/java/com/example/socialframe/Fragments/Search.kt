package com.example.socialframe.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.MyRecyclerViewAdapter
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.classes.User
import com.example.socialframe.databinding.FragmentSearchBinding


class Search : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       var binding : FragmentSearchBinding = FragmentSearchBinding.inflate(inflater,container,false)
       var mymodel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        var adapter=MyRecyclerViewAdapter(OpenModel.mycontext, OpenModel.AllUsers.value!!)
        binding.recyclerView2.layoutManager=LinearLayoutManager(OpenModel.mycontext)
        binding.recyclerView2.adapter=adapter
        mymodel.AllUsers.observe(requireActivity(), Observer {
            adapter=MyRecyclerViewAdapter(OpenModel.mycontext, it)
            binding.recyclerView2.layoutManager=LinearLayoutManager(OpenModel.mycontext)
            binding.recyclerView2.adapter=adapter
        })
        binding.editTextTextPersonName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                adapter.findObject(editable.toString().toLowerCase())
            }
        })
        return binding.root
    }


}