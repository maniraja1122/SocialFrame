package com.example.socialframe.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.NotificationsAdapter
import com.example.socialframe.R
import com.example.socialframe.databinding.FragmentNotificationsBinding


class Notifications : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_notifications, container, false)
        var binding = FragmentNotificationsBinding.inflate(inflater,container,false)
        var adapter = NotificationsAdapter(OpenModel.CurrentUser.value!!.MyNotifications.reversed())
        binding.allnotifications.layoutManager=LinearLayoutManager(OpenModel.mycontext)
        binding.allnotifications.adapter=adapter
        try {
            OpenModel.CurrentUser.observe(requireActivity(), Observer {
                if(it!=null) {
                    if (it.MyNotifications.size != binding.allnotifications.adapter!!.itemCount) {
                        var adapter1 = NotificationsAdapter(it.MyNotifications.reversed())
                        binding.allnotifications.layoutManager =
                            LinearLayoutManager(OpenModel.mycontext)
                        binding.allnotifications.adapter = adapter1
                    }
                }
            })
        }
        catch (e:Exception){

        }

        return binding.root
    }

}