package com.example.socialframe.AuthFunctions

import android.app.ProgressDialog
import android.net.Uri
import android.widget.Toast
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Repository.FirebaseManager
import com.example.socialframe.classes.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


object AuthHelper {
    var manager = FirebaseManager()
    fun AddUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                manager.db.getReference().child("Users").child(user.key).setValue(user)
            }
        }
    }
    fun UpdateReadNotifications(newcount:Int){
        CoroutineScope(Dispatchers.IO).launch {
            async {

                manager.db.getReference().child("Users").child(OpenModel.CurrentUser.value!!.key).child("readNotifications").setValue(newcount)
            }
        }
    }
    fun CreatePost(post: Post, uri: Uri): String {
        val dialog = ProgressDialog(OpenModel.mycontext)
        dialog.setMessage("Posting.....")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var key = manager.db.reference.child("Posts").push().key
        post.key = key!!
        CoroutineScope(Dispatchers.IO).launch {
            async {
                var myref = manager.storage.reference.child("PostImages").child(key!!)
                myref.putFile(uri).addOnSuccessListener {
                    myref.downloadUrl.addOnSuccessListener {
                        post.imagelink = it.toString()
                        manager.db.reference.child("Posts").child(key).setValue(post)
                        dialog.dismiss()
                        Toast.makeText(
                            OpenModel.mycontext,
                            "Post Published Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        return key
    }
    fun UpdatePost(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                manager.db.reference.child("Posts").child(post.key).setValue(post)
            }
        }
    }
    fun AddComment(postkey: String, newcomment: String) {
        CoroutineScope(Dispatchers.IO).launch {
            async {
                manager.db.reference.child("Posts").child(postkey)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var post = snapshot.getValue(Post::class.java)
                            var comment = Comment(newcomment, OpenModel.CurrentUser.value!!.key)
                            if(post!!.author!= OpenModel.CurrentUser.value!!.key){
                                manager.db.reference.child("Users").child(post.author)
                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var myuser = snapshot.getValue(User::class.java)
                                            var newnotification = Notifications("post",
                                                OpenModel.CurrentUser.value!!.Name+" commented on your post",post.key)
                                            myuser!!.MyNotifications.add(newnotification)
                                            AddUser(myuser)
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                            }
                            post!!.Comments.add(comment)
                            UpdatePost(post)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
        }}
    fun AFollowedB(user1: User, user2: User) {
                var currentuser: User? = null
                var visiteduser: User? = null
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        manager.db.reference.child("Users").child(user1.key)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    currentuser = snapshot.getValue(User::class.java)!!
                                    manager.db.reference.child("Users").child(user2.key)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                visiteduser = snapshot.getValue(User::class.java)
                                                var newnotification = Notifications("user",currentuser!!.Name+" followed you",currentuser!!.key)
                                                visiteduser!!.MyNotifications.add(newnotification)
                                                currentuser!!.Followed.add(visiteduser!!.key)
                                                visiteduser!!.Followers.add(currentuser!!.key)
                                                AddUser(currentuser!!)
                                                AddUser(visiteduser!!)
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
            }
    fun AUnfollowedB(user1: User, user2: User) {
                var currentuser: User? = null
                var visiteduser: User? = null
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        manager.db.reference.child("Users").child(user1.key)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    currentuser = snapshot.getValue(User::class.java)!!
                                    manager.db.reference.child("Users").child(user2.key)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                visiteduser = snapshot.getValue(User::class.java)
                                                currentuser!!.Followed.remove(visiteduser!!.key)
                                                visiteduser!!.Followers.remove(currentuser!!.key)
                                                AddUser(currentuser!!)
                                                AddUser(visiteduser!!)
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
            }
    fun DeletePosts(key: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        async {
                            var mynotifications = OpenModel.CurrentUser.value!!.MyNotifications.filter {
                                it.type=="user"
                            }
                            manager.db.getReference().child("Users").child(OpenModel.CurrentUser.value!!.key)
                                .child("myNotifications").setValue(mynotifications)
                        }
                        UpdateReadNotifications(0)
                        manager.db.reference.child("Users").child(key).child("myPosts")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var arr = snapshot.children.mapNotNull {
                                        it.getValue(String::class.java)
                                    }.toList()
                                    for (links in arr) {
                                        manager.db.reference.child("Posts").child(links)
                                            .removeValue()
                                    }
                                    manager.db.reference.child("Users").child(key).child("myPosts")
                                        .removeValue()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                }
            }
    fun SendMessage(user1:String,user2:String,message:String){
                CoroutineScope(Dispatchers.IO).launch {
                    async {
                        var newmessage = MessageModel(message,1)
                        var key = manager.db.getReference().child("Messages").child(user1).child(user2).push().key
                        manager.db.getReference().child("Messages").child(user1).child(user2).child(key!!).setValue(newmessage)
                        var newmessage1 = MessageModel(message,2)
                        var key1 = manager.db.getReference().child("Messages").child(user2).child(user1).push().key
                        manager.db.getReference().child("Messages").child(user2).child(user1).child(key1!!).setValue(newmessage1)
                    }
                }
            }
        }