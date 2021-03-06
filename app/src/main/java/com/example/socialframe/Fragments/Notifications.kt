package com.example.socialframe.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Activities.binding
import com.example.socialframe.Adapters.NotificationsAdapter
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.databinding.FragmentNotificationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class Notifications : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_notifications, container, false)
        binding!!.bottomnav.getOrCreateBadge(R.id.notificationbtn).number = 0
        var binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        CoroutineScope(Dispatchers.Main).launch { async {
            var adapter =
                NotificationsAdapter(OpenModel.CurrentUser.value!!.MyNotifications.reversed())
            binding.allnotifications.layoutManager = LinearLayoutManager(OpenModel.mycontext)
            binding.allnotifications.adapter = adapter
            AuthHelper.UpdateReadNotifications(OpenModel.CurrentUser.value!!.MyNotifications.size)
        }}
        try {
            OpenModel.CurrentUser.observe(requireActivity(), Observer {
                if(it!=null) {
                    if (it.MyNotifications.size != binding.allnotifications.adapter!!.itemCount) {
                        CoroutineScope(Dispatchers.Main).launch { async {
                            var adapter1 = NotificationsAdapter(it.MyNotifications.reversed())
                            binding.allnotifications.layoutManager =
                                LinearLayoutManager(OpenModel.mycontext)
                            binding.allnotifications.adapter = adapter1
                        }}
                    }
                    AuthHelper.UpdateReadNotifications(it!!.MyNotifications.size)
                }
            })
        }
        catch (e:Exception){

        }

        return binding.root
    }

}