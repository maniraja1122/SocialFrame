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
import android.widget.Toast
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

class PostAdapter(var context:Context,var arr:List<Post>):RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.postheading)
        var postimage=itemView.findViewById<ImageView>(R.id.postimage)
        var likebtn=itemView.findViewById<Button>(R.id.likebtn)
        var commentbtn=itemView.findViewById<Button>(R.id.commentbtn)
        var userimage=itemView.findViewById<ImageView>(R.id.userimage)
        var username=itemView.findViewById<TextView>(R.id.username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
        return MyViewHolder(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.setText(arr[position].title)
        CoroutineScope(Dispatchers.Main).launch { async {
            Glide.with(context).load(arr[position].imagelink).placeholder(R.drawable.empty_profile)
                .into(holder.postimage);
        }}
        holder.likebtn.setText(arr[position].Likes.size.toString())
        holder.commentbtn.setText(arr[position].Comments.size.toString())
        //Fetching and Adding User
        var userkey = arr[position].author
        CoroutineScope(Dispatchers.IO).launch {
            async {
        AuthHelper.manager.db.reference.child("Users").child(userkey).addListenerForSingleValueEvent(object :
            ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                var myuser = snapshot.getValue(User::class.java)
                holder.username.setText(myuser!!.Name)
                CoroutineScope(Dispatchers.Main).launch { async {
                    Glide.with(context).load(myuser.MyPICUrl).placeholder(R.drawable.empty_profile)
                        .into(holder.userimage);
                }}
                holder.userimage.setOnClickListener(){
                    if (OpenModel.CurrentUser.value!!.key != myuser.key) {
                        OpenModel.VisitedUser.value = myuser
                    }
                    else{
                        Toast.makeText(OpenModel.mycontext, "You can visit your profile from the top...", Toast.LENGTH_SHORT).show()
                    }
                }
                holder.username.setOnClickListener(){
                    if (OpenModel.CurrentUser.value!!.key != myuser.key) {
                        OpenModel.VisitedUser.value = myuser
                    }
                    else{
                        Toast.makeText(OpenModel.mycontext, "You can visit your profile from the top...", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })}}
        if(arr[position].Likes.contains(OpenModel.CurrentUser.value!!.key)){
            holder.likebtn.setBackgroundColor(Color.BLACK)
        }
        //Listener
        holder.likebtn.setOnClickListener(){
            CoroutineScope(Dispatchers.Main).launch { async {
                if (arr[position].Likes.contains(OpenModel.CurrentUser.value!!.key)) {
                    arr[position].Likes.remove(OpenModel.CurrentUser.value!!.key)
                    AuthHelper.UpdatePost(arr[position])
                    //Temporary Update
                    holder.likebtn.setBackgroundColor(Color.parseColor("#02B387"))
                    holder.likebtn.setText((arr[position].Likes.size).toString())
                } else {
                    arr[position].Likes.add(OpenModel.CurrentUser.value!!.key)
                    AuthHelper.UpdatePost(arr[position])
                    //Notified
                    CoroutineScope(Dispatchers.IO).launch {
                        async {
                            if (arr[position].author != OpenModel.CurrentUser.value!!.key) {
                                AuthHelper.manager.db.reference.child("Users")
                                    .child(arr[position].author)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var myuser = snapshot.getValue(User::class.java)
                                            var newnotification = Notifications(
                                                "post",
                                                "Someone liked your post", arr[position].key
                                            )
                                            myuser!!.MyNotifications.add(newnotification)
                                            AuthHelper.AddUser(myuser)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                            }
                        }
                    }
                    //Temporary Update
                    holder.likebtn.setBackgroundColor(Color.BLACK)
                    holder.likebtn.setText((arr[position].Likes.size).toString())
                }
            }}
        }
        holder.commentbtn.setOnClickListener(){
            OpenModel.OpenedCommentPost.value=arr[position].key
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }
}