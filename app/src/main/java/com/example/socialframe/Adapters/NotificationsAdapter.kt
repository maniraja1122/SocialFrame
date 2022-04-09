package com.example.socialframe.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.Notifications
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotificationsAdapter(var arr:List<Notifications>):RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var notificationtext=itemView.findViewById<TextView>(R.id.notificationtext)
        var notificationimage=itemView.findViewById<ImageView>(R.id.notificationimage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var v= LayoutInflater.from(OpenModel.mycontext).inflate(R.layout.notification_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationtext.setText(arr[position].mytext)
        if(arr[position].type=="post"){
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    AuthHelper.manager.db.reference.child("Posts").child(arr[position].VisitingUnit).addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var mypost=snapshot.getValue(Post::class.java)
                            Glide.with(OpenModel.mycontext!!).load(mypost!!.imagelink).placeholder(R.drawable.empty_profile).into(holder.notificationimage);
                            holder.itemView.setOnClickListener(){
                                OpenModel.OpenedCommentPost.value=mypost.key
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }
        else if(arr[position].type=="user"){
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    AuthHelper.manager.db.reference.child("Users").child(arr[position].VisitingUnit).addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var myuser=snapshot.getValue(User::class.java)
                            Glide.with(OpenModel.mycontext!!).load(myuser!!.MyPICUrl).placeholder(R.drawable.empty_profile).into(holder.notificationimage);
                            holder.itemView.setOnClickListener(){
                                OpenModel.VisitedUser.value=myuser
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}