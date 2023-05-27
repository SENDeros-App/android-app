package com.example.senderos4.ui.clasificacion.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.R
import com.example.senderos4.data.Header
import com.example.senderos4.data.User

class ClasificationAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_USER = 1

    private var headers: List<Header> = emptyList()
    private var users: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }
            else -> throw IllegalArgumentException("ViewType desconocido")
        }
    }

    override fun getItemCount(): Int {
        return users.size + getHeaderCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderPosition(position)) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_USER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                val headerPosition = getHeaderPosition(position)
                headerHolder.bind(headers[headerPosition])
            }
            VIEW_TYPE_USER -> {
                val userHolder = holder as UserViewHolder
                val userPosition = getUserPosition(position)
                userHolder.bind(users[userPosition])
            }
        }
    }

    fun submitData(headers: List<Header>, users: List<User>) {
        this.headers = headers
        this.users = users
        notifyDataSetChanged()
    }

    private fun getHeaderCount(): Int {
        // Calcular la cantidad de encabezados según la lógica deseada (cada dos usuarios, cada cuatro usuarios)
        return users.size / 2
    }

    private fun isHeaderPosition(position: Int): Boolean {
        // Verificar si la posición es una posición de encabezado
        return (position % 3 == 2)
    }

    private fun getHeaderPosition(position: Int): Int {
        // Obtener la posición del encabezado correspondiente para la posición dada

        return position / 3 // Encabezado en las posiciones 2, 5, 8, etc. (posición / 3)
    }

    private fun getUserPosition(position: Int): Int {
        // Obtener la posición del usuario correspondiente para la posición dada
        return if (position > 2) (position - getHeaderCount()) else position
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(header:Header) {
            itemView.findViewById<TextView>(R.id.header_title).text = header.tittle
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.nameUser).text = user.name
            itemView.findViewById<TextView>(R.id.pxUser).text = user.px

        }
    }
}
    /*private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_USER = 1

    private var header: List<Header>? = null
    private var users: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_USER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }
            else -> throw IllegalArgumentException("ViewType desconocido")
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            VIEW_TYPE_HEADER
        }else{
            VIEW_TYPE_USER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.bind(headers[position])
            }
            VIEW_TYPE_USER -> {
                val userHolder = holder as UserViewHolder
                userHolder.bind(users[position])
            }
        }
    }

    fun submitData(header: List<Header>, users: List<User>) {
        this.header = header
        this.users = users
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(header:Header) {
            itemView.findViewById<TextView>(R.id.header_title).text = header.tittle
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.nameUser).text=user.name
        }
    }*/








    /*class ViewHolderUser(itemView: View):RecyclerView.ViewHolder(itemView) {
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
    }*/