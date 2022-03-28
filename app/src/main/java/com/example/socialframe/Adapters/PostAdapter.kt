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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.Post

class PostAdapter(var context:Context,var arr:List<Post>):RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.postheading)
        var postimage=itemView.findViewById<ImageView>(R.id.postimage)
        var likebtn=itemView.findViewById<Button>(R.id.likebtn)
        var commentbtn=itemView.findViewById<Button>(R.id.commentbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
        return MyViewHolder(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.setText(arr[position].title)
        Glide.with(context).load(arr[position].imagelink).into(holder.postimage);
        holder.likebtn.setText(arr[position].Likes.size.toString())
        holder.commentbtn.setText(arr[position].Comments.size.toString())
        if(arr[position].Likes.contains(OpenModel.CurrentUser.value!!.key)){
            holder.likebtn.setBackgroundColor(Color.RED)
        }
        //Listener
        holder.likebtn.setOnClickListener(){
            if(arr[position].Likes.contains(OpenModel.CurrentUser.value!!.key)){
                arr[position].Likes.remove(OpenModel.CurrentUser.value!!.key)
                AuthHelper.UpdatePost(arr[position])
                //Temporary Update
                holder.likebtn.setBackgroundColor(R.color.fore_200)
                holder.likebtn.setText((arr[position].Likes.size).toString())
            }
            else{
                arr[position].Likes.add(OpenModel.CurrentUser.value!!.key)
                AuthHelper.UpdatePost(arr[position])
                //Temporary Update
                holder.likebtn.setBackgroundColor(Color.RED)
                holder.likebtn.setText((arr[position].Likes.size).toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}