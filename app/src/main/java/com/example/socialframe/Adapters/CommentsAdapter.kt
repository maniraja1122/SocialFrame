package com.example.socialframe.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.Comment
import com.example.socialframe.classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CommentsAdapter(var arr:MutableList<Comment>):RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var username=itemView.findViewById<TextView>(R.id.compname)
        var userimage=itemView.findViewById<ImageView>(R.id.compimage)
        var commentcontent=itemView.findViewById<TextView>(R.id.commentcontent)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v =LayoutInflater.from(parent.context).inflate(R.layout.comment_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.commentcontent.setText(arr[position].text)
        CoroutineScope(Dispatchers.IO).launch {
            async {
        AuthHelper.manager.db.reference.child("Users").child(arr[position].userkey).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user=snapshot.getValue(User::class.java)
                holder.username.setText(user!!.Name)
                CoroutineScope(Dispatchers.Main).launch { async {
                    Glide.with(OpenModel.mycontext!!).load(user!!.MyPICUrl)
                        .placeholder(R.drawable.empty_profile).into(holder.userimage)
                }}
                holder.userimage.setOnClickListener(){
                    if (OpenModel.CurrentUser.value!!.key != user.key) {
                        OpenModel.VisitedUser.value = user
                    }
                }
                holder.username.setOnClickListener(){
                    if (OpenModel.CurrentUser.value!!.key != user.key) {
                        OpenModel.VisitedUser.value = user
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
            }
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }

}