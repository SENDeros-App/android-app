package com.example.senderos4.ui.clasificacion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.R
import com.example.senderos4.data.User

class ClasificationAdapter:RecyclerView.Adapter<ClasificationAdapter.ViewHolderUser>(){

    private var  users:List<User>?=null

    class ViewHolderUser(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(user:User){

            itemView.findViewById<TextView>(R.id.nameUser).text = user.name
            itemView.findViewById<TextView>(R.id.pxUser).text = user.px
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUser {
        val inflater  = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolderUser(view)
    }

    override fun getItemCount(): Int = users?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolderUser, position: Int) {
        users?.let {
            holder.bind(it[position])
        }
    }

    fun submitData(users:List<User>){
        this.users = users
        notifyDataSetChanged()
    }
}