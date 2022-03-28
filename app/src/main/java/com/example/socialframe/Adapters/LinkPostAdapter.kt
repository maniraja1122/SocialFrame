package com.example.socialframe.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LinkPostAdapter(var context: Context, var arr:List<String>): RecyclerView.Adapter<LinkPostAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.postheading)
        var postimage=itemView.findViewById<ImageView>(R.id.postimage)
        var likebtn=itemView.findViewById<Button>(R.id.likebtn)
        var commentbtn=itemView.findViewById<Button>(R.id.commentbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
        return MyViewHolder(v)
    }
    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: LinkPostAdapter.MyViewHolder, position: Int) {
        AuthHelper.manager.db.reference.child("Posts").child(arr[position]).addListenerForSingleValueEvent(object:ValueEventListener{
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                var post = snapshot.getValue(Post::class.java)
                holder.title.setText(post!!.title)
                Glide.with(context).load(post.imagelink).into(holder.postimage);
                holder.likebtn.setText(post.Likes.size.toString())
                holder.commentbtn.setText(post.Comments.size.toString())
                if(post.Likes.contains(OpenModel.CurrentUser.value!!.key)){
                    holder.likebtn.setBackgroundColor(Color.RED)
                }
                //Listener
                holder.likebtn.setOnClickListener(){
                    if(post.Likes.contains(OpenModel.CurrentUser.value!!.key)){
                        post.Likes.remove(OpenModel.CurrentUser.value!!.key)
                        AuthHelper.UpdatePost(post)
                        //Temporary Update
                        holder.likebtn.setBackgroundColor(R.color.fore_200)
                        holder.likebtn.setText((post.Likes.size).toString())
                    }
                    else{
                        post.Likes.add(OpenModel.CurrentUser.value!!.key)
                        AuthHelper.UpdatePost(post)
                        //Temporary Update
                        holder.likebtn.setBackgroundColor(Color.RED)
                        holder.likebtn.setText((post.Likes.size).toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}