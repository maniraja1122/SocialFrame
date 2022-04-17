package com.example.socialframe.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.LinkPostAdapter
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.FragmentProfileBinding
import com.example.socialframe.databinding.FragmentVisitedProfileBinding
import kotlinx.coroutines.*


class VisitedProfile : Fragment() {

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding: FragmentVisitedProfileBinding =
            FragmentVisitedProfileBinding.inflate(inflater, container, false)
        var mymodel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.profilename.setText(mymodel.VisitedUser.value?.Name)
        binding.profiledesc.setText(mymodel.VisitedUser.value?.Description)
        binding.followers.setText(mymodel.VisitedUser.value?.Followers?.size.toString())
        binding.followed.setText(mymodel.VisitedUser.value?.Followed?.size.toString() + " Followed")
        if(mymodel.CurrentUser.value?.Followed!!.contains(mymodel.VisitedUser.value?.key)){
            binding.followers.setBackgroundColor(Color.BLACK)
        }
        binding.msgbtn.setOnClickListener(){
            OpenModel.MessageReciever.value= OpenModel.VisitedUser.value
        }
        //Adding Follow Button
        binding.followers.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch { async {
                if (mymodel.CurrentUser.value?.Followed!!.contains(mymodel.VisitedUser.value?.key)) {
                    async {
                        AuthHelper.AUnfollowedB(
                            OpenModel.CurrentUser.value!!,
                            mymodel.VisitedUser.value!!
                        )}
                    withContext(Dispatchers.Main) {
                        async {
                            binding.followers.setBackgroundColor(Color.parseColor("#02B387"))
                            mymodel.VisitedUser.value!!.Followers.remove(OpenModel.CurrentUser.value!!.key)
                            OpenModel.CurrentUser.value!!.Followed.remove(mymodel.VisitedUser.value!!.key)
                            binding.followers.setText(mymodel.VisitedUser.value?.Followers?.size.toString())
                        }}
                } else {
                    async {
                            AuthHelper.AFollowedB(
                                OpenModel.CurrentUser.value!!,
                                mymodel.VisitedUser.value!!
                            )}
                    withContext(Dispatchers.Main) {
                        async {
                            binding.followers.setBackgroundColor(Color.BLACK)
                            mymodel.VisitedUser.value!!.Followers.add(OpenModel.CurrentUser.value!!.key)
                            OpenModel.CurrentUser.value!!.Followed.add(mymodel.VisitedUser.value!!.key)
                            binding.followers.setText(mymodel.VisitedUser.value?.Followers?.size.toString())
                        }}
                }
            }}
        }
        //Setting Recycler
        var adapter =
            LinkPostAdapter(OpenModel.mycontext!!, mymodel.VisitedUser.value!!.MyPosts.reversed(),mymodel.VisitedUser.value!!)
        binding.myposts.layoutManager = LinearLayoutManager(OpenModel.mycontext)
        binding.myposts.adapter = adapter
        CoroutineScope(Dispatchers.Main).launch { async {
            Glide.with(OpenModel.mycontext!!).load(mymodel.VisitedUser.value!!.MyPICUrl)
                .placeholder(R.drawable.empty_profile).into(binding.profilephoto)
        }}
        mymodel.VisitedUser.observe(requireActivity(), Observer {
            binding.profilename.setText(it.Name)
            binding.profiledesc.setText(it.Description)
            binding.followers.setText(it.Followers.size.toString())
            binding.followed.setText(it.Followed.size.toString() + " Followed")
            if(mymodel.CurrentUser.value?.Followed!!.contains(mymodel.VisitedUser.value?.key)){
                binding.followers.setBackgroundColor(Color.BLACK)
            }
            CoroutineScope(Dispatchers.Main).launch { async {
                Glide.with(OpenModel.mycontext!!).load(mymodel.VisitedUser.value!!.MyPICUrl)
                    .placeholder(R.drawable.empty_profile).into(binding.profilephoto)
            }}
            //Updating Recycler
            if (it.MyPosts.size != binding.myposts.adapter!!.itemCount) {
                CoroutineScope(Dispatchers.Main).launch { async {
                    var adapter =
                        LinkPostAdapter(OpenModel.mycontext!!, it!!.MyPosts.reversed(), it)
                    binding.myposts.layoutManager = LinearLayoutManager(OpenModel.mycontext)
                    binding.myposts.adapter = adapter
                }}
            }
        })

        return binding.root
    }
}