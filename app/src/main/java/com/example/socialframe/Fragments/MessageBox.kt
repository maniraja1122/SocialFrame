package com.example.socialframe.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.MessageAdapter
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.MessageModel
import com.example.socialframe.databinding.FragmentMessageBoxBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MessageBox : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_message_box, container, false)
        var templist:MutableList<MessageModel> = mutableListOf()
        var binding:FragmentMessageBoxBinding= FragmentMessageBoxBinding.inflate(inflater,container,false)
        try {
            if(OpenModel.AllMessages.value!!.containsKey(OpenModel.MessageReciever.value!!.key)) {
                CoroutineScope(Dispatchers.Main).launch {
                    async {
                        var adapter = MessageAdapter(
                            OpenModel.mycontext!!,
                            (OpenModel.AllMessages.value!![OpenModel.MessageReciever.value!!.key]!!.value)!!
                        )
                        binding.allmessages.layoutManager = LinearLayoutManager(OpenModel.mycontext)
                        binding.allmessages.adapter = adapter
                        binding.allmessages.scrollToPosition(binding.allmessages.adapter!!.getItemCount() - 1);
                    }
                }
            }
            else{
                    var arrlist =
                        (OpenModel.AllMessages.value!![OpenModel.MessageReciever.value!!.key]!!.value)
                    if (arrlist != null) {
                        var newlist: MutableList<MessageModel> = mutableListOf()
                        newlist.addAll(arrlist)
                        templist = newlist
                    }
                CoroutineScope(Dispatchers.Main).launch {
                    async {
                        var adapter = MessageAdapter(
                            OpenModel.mycontext!!,
                            templist
                        )
                        binding.allmessages.layoutManager = LinearLayoutManager(OpenModel.mycontext)
                        binding.allmessages.adapter = adapter
                        binding.allmessages.scrollToPosition(binding.allmessages.adapter!!.getItemCount() - 1);
                    }
                }
            }
        }
        catch (e:Exception){
        }
        OpenModel.MessageReciever.observe(requireActivity(), Observer {
            binding.recievername.setText(it.Name)
        })
        if(OpenModel.AllMessages.value!!.containsKey(OpenModel.MessageReciever.value!!.key)) {
            OpenModel.AllMessages.value!![OpenModel.MessageReciever.value!!.key]!!.observe(
                requireActivity(),
                Observer {
                    if (binding.allmessages.adapter!=null && it.size != binding.allmessages.adapter!!.itemCount) {
                        CoroutineScope(Dispatchers.Main).launch {

                        async {
                            var adapter1 = MessageAdapter(
                                OpenModel.mycontext!!,
                                it
                            )
                            binding.allmessages.layoutManager =
                                LinearLayoutManager(OpenModel.mycontext)
                            binding.allmessages.adapter = adapter1
                            binding.allmessages.scrollToPosition(binding.allmessages.adapter!!.getItemCount() - 1);
                        }}
                    }
                    else if(binding.allmessages.adapter==null){
                        CoroutineScope(Dispatchers.Main).launch {

                            async {
                                var adapter1 = MessageAdapter(
                                    OpenModel.mycontext!!,
                                    it
                                )
                                binding.allmessages.layoutManager =
                                    LinearLayoutManager(OpenModel.mycontext)
                                binding.allmessages.adapter = adapter1
                                binding.allmessages.scrollToPosition(binding.allmessages.adapter!!.getItemCount() - 1)
                            }}
                    }
                })
       }
        binding.sendbtn.setOnClickListener(){
            var newmessage=binding.entertext.text.toString()
            if(newmessage!=""){
                binding.entertext.setText("")
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        AuthHelper.SendMessage(OpenModel.CurrentUser.value!!.key, OpenModel.MessageReciever.value!!.key,newmessage)
                    }
                }
                        if (!OpenModel.AllMessages.value!!.containsKey(OpenModel.MessageReciever.value!!.key)) {
                            templist.add(MessageModel(newmessage, 1))
                            //Temporary
                            CoroutineScope(Dispatchers.Main).launch {
                                async {
                                    var adapter2 = MessageAdapter(
                                        OpenModel.mycontext!!,
                                        templist
                                    )
                                    binding.allmessages.layoutManager =
                                        LinearLayoutManager(OpenModel.mycontext)
                                    binding.allmessages.adapter = adapter2
                                    binding.allmessages.scrollToPosition(binding.allmessages.adapter!!.getItemCount() - 1);
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainerView, MessageBox()).commit()
                                } }
                        }
        }
            else{
                binding.entertext.setError("Please Enter A Message First")
            }
        }
        binding.recievername.setOnClickListener(){
            OpenModel.VisitedUser.value= OpenModel.MessageReciever.value
        }

        return binding.root
    }

}