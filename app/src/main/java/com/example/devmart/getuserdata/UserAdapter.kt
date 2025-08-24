package com.example.devmart.getuserdata



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


import com.example.devmart.R

class UserAdapter(private val userList: List<ApiResponse.Data>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvId: TextView = itemView.findViewById(R.id.tvId)
//        val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)


        val tvName: TextView = itemView.findViewById(R.id.userName)
        val tvNumbar: TextView = itemView.findViewById(R.id.userNumber)
        val tvAmount: TextView = itemView.findViewById(R.id.userAmount)
        val tvDate: TextView = itemView.findViewById(R.id.userDate)
        val tvDescription: TextView = itemView.findViewById(R.id.userDescription)
        val ivImage: ImageView = itemView.findViewById(R.id.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvName.text = "Name: ${user.name ?: "N/A"}"
        holder.tvNumbar.text = "Number: ${user.numbar ?: "N/A"}"
        holder.tvAmount.text = "Amount: ${user.amount ?: "N/A"}"
        holder.tvDate.text = "Date: ${user.date ?: "N/A"}"
        holder.tvDescription.text = "Description: ${user.description ?: "N/A"}"

        // Load image with Glide
        if (!user.image.isNullOrEmpty()) {

//            Glide.with()
//                .load
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .into(imageView)

        } else {
            holder.ivImage.setImageResource(R.drawable.logo)
        }
    }

    override fun getItemCount(): Int = userList.size
}