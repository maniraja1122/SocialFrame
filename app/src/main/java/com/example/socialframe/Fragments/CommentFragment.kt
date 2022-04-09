package com.example.socialframe.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Adapters.CommentsAdapter
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.R
import com.example.socialframe.classes.Comment
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.example.socialframe.databinding.FragmentCommentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CommentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_comment, container, false)
        var binding = FragmentCommentBinding.inflate(inflater,container,false)
        CoroutineScope(Dispatchers.IO).launch {
            async {
                AuthHelper.manager.db.reference.child("Posts").child(OpenModel.OpenedCommentPost.value!!).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var CurrentPost=snapshot.getValue(Post::class.java)
                        var adapter=CommentsAdapter(CurrentPost!!.Comments)
                        binding.allcomments.layoutManager=LinearLayoutManager(OpenModel.mycontext)
                        binding.allcomments.adapter=adapter
                        binding.allcomments.scrollToPosition(CurrentPost.Comments.size - 1);
                        binding.compostheading.setText(CurrentPost!!.title)
                        Glide.with(OpenModel.mycontext!!).load(CurrentPost.imagelink).placeholder(R.drawable.empty_profile).into(binding.compostimage)
                        AuthHelper.manager.db.reference.child("Users").child(CurrentPost.author).addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var user=snapshot.getValue(User::class.java)
                                Glide.with(OpenModel.mycontext!!).load(user!!.MyPICUrl).placeholder(R.drawable.empty_profile).into(binding.comuserimage)
                                binding.comusername.setText(user.Name)
                                binding.comuserimage.setOnClickListener(){
                                    if (OpenModel.CurrentUser.value!!.key != user.key) {
                                        OpenModel.VisitedUser.value = user
                                    }
                                    else{
                                    }
                                }
                                binding.comusername.setOnClickListener(){
                                    if (OpenModel.CurrentUser.value!!.key != user.key) {
                                        OpenModel.VisitedUser.value = user
                                    }
                                    else{
                                    }
                                }
                                binding.commentaddbtn.setOnClickListener(){
                                    var newcomment=binding.commenttext.text.toString()
                                    if(newcomment!=""){
                                        AuthHelper.AddComment(OpenModel.OpenedCommentPost.value!!,newcomment)
                                        binding.commenttext.setText("")
                                        //Temporary
                                        CurrentPost!!.Comments.add(Comment(newcomment, OpenModel.CurrentUser.value!!.key))
                                        var adapter1=CommentsAdapter(CurrentPost!!.Comments)
                                        binding.allcomments.layoutManager=LinearLayoutManager(OpenModel.mycontext)
                                        binding.allcomments.adapter=adapter1
                                        binding.allcomments.scrollToPosition(CurrentPost.Comments.size - 1);
                                    }
                                    else{
                                        binding.commenttext.setError("Please Enter A Comment First")
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

        return binding.root
    }
}