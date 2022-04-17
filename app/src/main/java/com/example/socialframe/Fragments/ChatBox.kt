package com.example.socialframe.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.ChatAdapter
import com.example.socialframe.R
import com.example.socialframe.databinding.FragmentChatBoxBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ChatBox : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_chat_box, container, false)
        var binding = FragmentChatBoxBinding.inflate(inflater,container,false)
        var adapter=ChatAdapter(OpenModel.AllChats.value!!)
        binding.allchats.layoutManager=LinearLayoutManager(OpenModel.mycontext)
        binding.allchats.adapter=adapter
        OpenModel.AllChats.observe(requireActivity(), Observer {
            CoroutineScope(Dispatchers.Main).launch { async {
            var adapter=ChatAdapter(it)
            binding.allchats.layoutManager=LinearLayoutManager(OpenModel.mycontext)
            binding.allchats.adapter=adapter
            } }
        })
        return binding.root
    }

}