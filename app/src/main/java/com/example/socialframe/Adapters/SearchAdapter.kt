package com.example.socialframe.Adapters


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.Fragments.CreatePost
import com.example.socialframe.Fragments.VisitedProfile
import com.example.socialframe.R
import com.example.socialframe.classes.User


class MyRecyclerViewAdapter(context: Context?, myObjectsList: List<User>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.SampleViewHolders>() {
    private val myObjectsList //holds the items of type MyObject
            : List<User>
    private val foundObjects //holds the indices of the found items
            : MutableSet<Int>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SampleViewHolders {
        val layoutView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.searchidlayout, null
        )
        return SampleViewHolders(layoutView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SampleViewHolders, position: Int) {
        //look for object in O(1) in the indices set
        if (!foundObjects.contains(position)) {
            //object not found => hide it.
            holder.hideLayout()
            return
        } else {
            //object found => show it.
                if(OpenModel.CurrentUser.value!!.key==myObjectsList[position].key){
                    holder.followButton.visibility=View.INVISIBLE
                }
            holder.nameTextView.setText(myObjectsList[position].Name)
            Glide.with(OpenModel.mycontext!!).load(myObjectsList[position].MyPICUrl).placeholder(R.drawable.empty_profile).into(holder.imgImageView)
            holder.followButton.setText(myObjectsList[position].Followers.size.toString())
            if(OpenModel.CurrentUser.value!!.Followed.contains(myObjectsList[position].key)){
                holder.followButton.setBackgroundColor(Color.RED)
            }
            holder.followButton.setOnClickListener() {
                if (OpenModel.CurrentUser.value!!.key != myObjectsList[position].key) {
                    if (!OpenModel.CurrentUser.value!!.Followed.contains(myObjectsList[position].key)) {
                        //Function
                        AuthHelper.AFollowedB(OpenModel.CurrentUser.value!!,myObjectsList[position])
                        //Temporary Show
                        myObjectsList[position].Followers.add(OpenModel.CurrentUser.value!!.key)
                        OpenModel.CurrentUser.value!!.Followed.add(myObjectsList[position].key)
                        holder.followButton.setText((myObjectsList[position].Followers.size).toString())
                        holder.followButton.setBackgroundColor(Color.RED)
                    } else {
                        //Function
                        AuthHelper.AUnfollowedB(OpenModel.CurrentUser.value!!,myObjectsList[position])
                        //Temporary Show
                        myObjectsList[position].Followers.remove(OpenModel.CurrentUser.value!!.key)
                        OpenModel.CurrentUser.value!!.Followed.remove(myObjectsList[position].key)
                        holder.followButton.setText((myObjectsList[position].Followers.size).toString())
                        holder.followButton.setBackgroundColor(R.color.fore_500)
                    }
                }
                else{
                    Toast.makeText(OpenModel.mycontext, "You can't follow yourself...", Toast.LENGTH_SHORT).show()
                }
            }
            holder.nameTextView.setOnClickListener(){
                if (OpenModel.CurrentUser.value!!.key != myObjectsList[position].key) {
                    OpenModel.VisitedUser.value = myObjectsList[position]
                }
                else{
                    Toast.makeText(OpenModel.mycontext, "You can visit your profile from the top...", Toast.LENGTH_SHORT).show()
                }
            }
            holder.imgImageView.setOnClickListener(){
                if (OpenModel.CurrentUser.value!!.key != myObjectsList[position].key) {
                    OpenModel.VisitedUser.value = myObjectsList[position]
                }
                else{
                    Toast.makeText(OpenModel.mycontext, "You can visit your profile from the top...", Toast.LENGTH_SHORT).show()
                }
            }
                holder.showLayout()
        }

    }

    override fun getItemCount(): Int {
        return myObjectsList.size
    }

    fun findObject(text: String) {
        //look for "text" in the objects list
        for (i in myObjectsList.indices) {
            //if it's empty text, we want all objects, so just add it to the set.
            if (text.length == 0) {
                foundObjects.add(i)
            } else {
                //otherwise check if it meets your search criteria and add it or remove it accordingly
                if (myObjectsList[i].Name.toLowerCase().contains(text.toLowerCase())) {
                    foundObjects.add(i)
                } else {
                    foundObjects.remove(i)
                }
            }
        }
        notifyDataSetChanged()
    }

    class SampleViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imgImageView: ImageView
        var nameTextView: TextView
        var followButton:Button
        private val layout: CardView
        private val hiddenLayoutParams: FrameLayout.LayoutParams
        private val shownLayoutParams: FrameLayout.LayoutParams
        override fun onClick(view: View?) {
        }

        fun hideLayout() {
            //hide the layout
            layout.layoutParams = hiddenLayoutParams
        }

        fun showLayout() {
            //show the layout
            layout.layoutParams = shownLayoutParams
        }

        init {
            itemView.setOnClickListener(this)
            imgImageView = itemView.findViewById(R.id.pimage)
            nameTextView = itemView.findViewById(R.id.pname)
            followButton=itemView.findViewById(R.id.pfollow)
            layout =
                itemView.findViewById(R.id.cardview) //card_view is the id of my androidx.cardview.widget.CardView in my xml layout
            //prepare hidden layout params with height = 0, and visible layout params for later - see hideLayout() and showLayout()
            hiddenLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hiddenLayoutParams.height = 0
            shownLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    init {
        this.myObjectsList = myObjectsList
        foundObjects = HashSet()
        //first, add all indices to the indices set
        for (i in this.myObjectsList.indices) {
            foundObjects.add(i)
        }
    }
}