package com.abidzar.githubusersapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abidzar.githubusersapp.R
import com.abidzar.githubusersapp.model.response.userList.UsersResponseItem
import com.abidzar.githubusersapp.view.adapter.UserListAdapter.UserViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.new_card_item.view.*

class UserListAdapter(private val listItem: ArrayList<UsersResponseItem>) :
    RecyclerView.Adapter<UserViewHolder>() {


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

//    private val differCallback = object : DiffUtil.ItemCallback<UsersResponseItem>() {
//        override fun areItemsTheSame(oldItem: UsersResponseItem, newItem: UsersResponseItem): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: UsersResponseItem, newItem: UsersResponseItem): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(   this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.new_card_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val usersResponseItem = listItem[position]
        print("userrEsaers $usersResponseItem")
        holder.itemView.apply {

            Glide.with(context)
                .load(usersResponseItem.avatar_url)
                .into(imvAvatar)

            txvId.text = "#" + usersResponseItem.id

            txvUsernamme.text = usersResponseItem.login

            setOnClickListener {
                onItemClickListener?.let { it(usersResponseItem) }
            }

        }
    }

    fun clearList() {
        val size = listItem.size
        listItem.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(newList: List<UsersResponseItem>) {
        listItem.clear()
        listItem.addAll(newList)

        println("sdafbdgrt " + listItem.size)
        println("dfrdefwde " + newList.size)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        println("asdasdgtgrefr " + listItem.size)
        return listItem.size
    }

    private var onItemClickListener: ((UsersResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (UsersResponseItem) -> Unit) {
        onItemClickListener = listener
    }

}