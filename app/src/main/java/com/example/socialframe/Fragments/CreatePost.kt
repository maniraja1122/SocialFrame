package com.example.socialframe.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.classes.Post
import com.example.socialframe.databinding.FragmentCreatePostBinding


class CreatePost : Fragment() {
    var postimage: Uri?=null
    lateinit var binding:FragmentCreatePostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentCreatePostBinding.inflate(inflater,container,false)
        binding.publishbtn.setOnClickListener(){
            var posttitle=binding.posttitle.text.toString()
            if(posttitle==""){
                binding.posttitle.setError("Please Enter A Title First")
            }
            else if(postimage==null){
                Toast.makeText(OpenModel.mycontext!!, "Please Select An Image First", Toast.LENGTH_SHORT).show()
            }
            else{
                val key = AuthHelper.CreatePost(Post(posttitle),postimage!!)
                OpenModel.CurrentUser.value!!.MyPosts.add(key)
                AuthHelper.AddUser(OpenModel.CurrentUser.value!!)
                Toast.makeText(requireContext(), "Post Published Successfully...", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()
            }
        }
        binding.uploadbtn.setOnClickListener(){
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, 1002)
        }
        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1002 && resultCode== Activity.RESULT_OK){
            var url = data?.data
            postimage=url
            Glide.with(OpenModel.mycontext!!).load(url).into(binding.imageView);
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}