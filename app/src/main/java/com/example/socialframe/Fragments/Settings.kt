package com.example.socialframe.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Activities.SelectorActivity
import com.example.socialframe.Activities.SignupActivity
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.databinding.FragmentSearchBinding
import com.example.socialframe.databinding.FragmentSettingsBinding
import kotlinx.coroutines.*


class Settings : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentSettingsBinding.inflate(inflater,container,false)
        var mymodel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.changename.setText(mymodel.CurrentUser.value!!.Name)
        binding.changedesc.setText(mymodel.CurrentUser.value!!.Description)
        binding.savebtn.setOnClickListener(){
            var myname=binding.changename.text.toString()
            var mydesc = binding.changedesc.text.toString()
            if(myname==""){
                binding.changename.setError("Name can not be empty")
            }
            else if(mydesc==""){
                binding.changedesc.setError("Description can not be empty")
            }
            else if(mydesc.length>50)
            {
                binding.changedesc.setError("Description should be in between 50 characters")
            }
            else{
            mymodel.CurrentUser.value!!.Name=myname
            mymodel.CurrentUser.value!!.Description=mydesc
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        AuthHelper.AddUser(mymodel.CurrentUser.value!!)
                        withContext(Dispatchers.Main){
                            async {
                                Toast.makeText(OpenModel.mycontext, "Saved Successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        binding.delbtn.setOnClickListener(){
            AuthHelper.DeletePosts(OpenModel.CurrentUser.value!!.key)
        }
        binding.logoutbtn.setOnClickListener(){
            AuthHelper.manager.auth.signOut()
            startActivity(Intent(OpenModel.mycontext, SelectorActivity::class.java))
            Toast.makeText(OpenModel.mycontext, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}