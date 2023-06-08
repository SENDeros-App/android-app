package com.example.senderos4.ui.clasificacion.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.R
import com.example.senderos4.data.Header
import com.example.senderos4.data.User

class ClassificationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_USER = 1

    private var headers: List<Header>? = null
    private var users: List<User>? = null

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val headerTitleTextView: TextView = itemView.findViewById(R.id.header_title)
        private val declineIcon: ImageView = itemView.findViewById(R.id.ascentIcon)
        private val declineIcon2: ImageView = itemView.findViewById(R.id.ascentIcon2)
        private val ascentIcon: ImageView = itemView.findViewById(R.id.ascentIcon)
        private val ascentIcon2: ImageView = itemView.findViewById(R.id.ascentIcon2)
        private val GREEN_COLOR = itemView.context.getColor(R.color.green_headers)
        private val RED_COLOR = itemView.context.getColor(R.color.red_headers)

        fun bind(header: Header, position: Int) {

            headerTitleTextView.text = header.tittle

            when (getHeaderPosition(position)) {
                0 -> {
                    headerTitleTextView.setTextColor(GREEN_COLOR)
                    ascentIcon.setImageResource(R.drawable.icon_ascent_division)
                    ascentIcon2.setImageResource(R.drawable.icon_ascent_division)
                }

                1 -> {
                    headerTitleTextView.setTextColor(RED_COLOR)
                    declineIcon.setImageResource(R.drawable.icon_decline_division)
                    declineIcon2.setImageResource(R.drawable.icon_decline_division)
                }
            }

        }

    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameUser: TextView = itemView.findViewById(R.id.nameUser)
        private val pxUser: TextView = itemView.findViewById(R.id.pxUser)
        private val numPosition: TextView = itemView.findViewById(R.id.numPosition)

        fun bind(user: User, position: Int) {
            nameUser.text = user.name
            pxUser.text = user.px

            when {
                position + 1 > 15 -> numPosition.text = "${position - 1}"

                position + 1 > 5 -> numPosition.text =
                    "$position"

                else -> numPosition.text = "${position + 1}"
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }

            VIEW_TYPE_USER -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                UserViewHolder(view)
            }

            else -> throw IllegalArgumentException("ViewType desconocido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                val headerPosition = getHeaderPosition(position)
                headerHolder.bind(headers?.get(headerPosition) ?: Header(""), position)
            }

            VIEW_TYPE_USER -> {
                val userHolder = holder as UserViewHolder
                val userPosition = getUserPosition(position)
                userHolder.bind(users?.get(userPosition) ?: User("", "", ""), position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderPosition(position)) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_USER
        }
    }


    fun submitData(headers: List<Header>, users: List<User>) {
        this.headers = headers
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        val itemCount = users?.size?.plus(2) ?: 0
        return itemCount
    }

    private fun isHeaderPosition(position: Int): Boolean {
        // Verificar si la posición es una posición de encabezado
        return (position == 5 || position == 15)
    }

    private fun getHeaderPosition(position: Int): Int {
        // Obtener la posición del encabezado correspondiente para la posición dada
        return when (position) {
            5 -> 0 // Primer header
            15 -> 1 // Segundo header
            else -> throw IllegalArgumentException("no correspondienye")//-1 // no se como manejarlo
        }
    }

    private fun getUserPosition(position: Int): Int {
        // Obtener la posición del usuario correspondiente para la posición dada
        return when {
            position > 15 -> position - 2
            position > 5 -> position - 1
            else -> position
        }
    }

}
