package com.example.socialframe.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.LinkPostAdapter
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.FragmentProfileBinding
import kotlinx.coroutines.*


class Profile : Fragment() {
    lateinit var mymodel:MainViewModel
    lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        mymodel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.profilename.setText(mymodel.CurrentUser.value?.Name)
        binding.profiledesc.setText(mymodel.CurrentUser.value?.Description)
        binding.followers.setText(mymodel.CurrentUser.value?.Followers?.size.toString()+" Followers")
        binding.followed.setText(mymodel.CurrentUser.value?.Followed?.size.toString()+" Followed")
        //Setting Recycler
        var adapter = LinkPostAdapter(OpenModel.mycontext!!,mymodel.CurrentUser.value!!.MyPosts.reversed(),mymodel.CurrentUser.value!!)
        binding.myposts.layoutManager=LinearLayoutManager(OpenModel.mycontext)
        binding.myposts.adapter=adapter
        Glide.with(OpenModel.mycontext!!).load(mymodel.CurrentUser.value!!.MyPICUrl).placeholder(R.drawable.empty_profile).into(binding.profilephoto)
        mymodel.CurrentUser.observe(requireActivity(), Observer {
            binding.profilename.setText(it.Name)
            binding.profiledesc.setText(it.Description)
            binding.followers.setText(it.Followers.size.toString()+" Followers")
            binding.followed.setText(it.Followed.size.toString()+" Followed")
            Glide.with(OpenModel.mycontext!!).load(mymodel.CurrentUser.value!!.MyPICUrl).placeholder(R.drawable.empty_profile).into(binding.profilephoto)
            //Updating Recycler
            if(it.MyPosts.size!=binding.myposts.adapter!!.itemCount) {
                var adapter = LinkPostAdapter(OpenModel.mycontext!!, it!!.MyPosts.reversed(),it)
                binding.myposts.layoutManager = LinearLayoutManager(OpenModel.mycontext)
                binding.myposts.adapter = adapter
            }
        })
        binding.profilephoto.setOnClickListener(){
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, 1001)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1001 && resultCode==RESULT_OK){
            var url = data?.data
            mymodel.ChangePic(url)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}