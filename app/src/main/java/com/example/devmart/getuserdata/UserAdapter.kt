package com.example.devmart.getuserdata

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devmart.R

class UserAdapter(private val userList: MutableList<ApiResponse.Data>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var filteredItems = userList.toMutableList()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.userName)
        val tvNumbar: TextView = itemView.findViewById(R.id.userNumber)
        val tvAmount: TextView = itemView.findViewById(R.id.userAmount)
        val tvDate: TextView = itemView.findViewById(R.id.userDate)
        val tvDescription: TextView = itemView.findViewById(R.id.userDescription)
        val ivImage: ImageView = itemView.findViewById(R.id.userImage)
        val tvDueAmount: TextView = itemView.findViewById(R.id.userDueAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredItems[position]
        holder.tvName.text = "Name: ${user.name ?: "N/A"}"
        holder.tvNumbar.text = "Number: ${user.numbar ?: "N/A"}"
        holder.tvAmount.text = "Amount: ${user.amount ?: "N/A"}"
        holder.tvDueAmount.text = "Amount: ${user.due ?: "N/A"}"
        holder.tvDate.text = "Date: ${user.date ?: "N/A"}"
        holder.tvDescription.text = "Description: ${user.description ?: "N/A"}"

        val amountValue = user.amount?.toDoubleOrNull() ?: 0.0
        val dueValue = user.due?.toDoubleOrNull() ?: 0.0

        val remainingDue = dueValue - amountValue

        holder.tvDueAmount.text = "Due: $remainingDue"










        if (!user.image.isNullOrEmpty()) {
            Log.d("UserAdapter", "Loading image: ${user.image}")

            Glide.with(holder.itemView.context)
                .load(user.image)
                .placeholder(R.drawable.logo)
                .into(holder.ivImage)
        } else {
            holder.ivImage.setImageResource(R.drawable.logo)
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    fun getTotalAmount(): Double {
        return userList.sumOf { user ->
            user.amount?.toDoubleOrNull() ?: 0.0
        }
    }
    fun getTotalAmountdue(): Double {
        return userList.sumOf { user ->
            user.due?.toDoubleOrNull() ?: 0.0
        }
    }

    fun filter(query: String) {
        Log.d("Filter", "Query received: $query")
        filteredItems = if (query.isEmpty()) {
            userList.toMutableList()
        } else {
            userList.filter { parent ->
                parent.name?.contains(query, ignoreCase = true) ?: false
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    // 🔹 Update full list
    fun updateData(newList: List<ApiResponse.Data>) {
        userList.clear()
        userList.addAll(newList)
        filteredItems = userList.toMutableList()
        notifyDataSetChanged()
    }

    // 🔹 Add a single item dynamically
    fun addItem(newItem: ApiResponse.Data) {
        userList.add(newItem)
        filteredItems.add(newItem)
        notifyItemInserted(filteredItems.size - 1)
    }

    // 🔹 Remove a single item
    fun removeItem(position: Int) {
        if (position in 0 until filteredItems.size) {
            val item = filteredItems[position]
            userList.remove(item) // Remove from original list
            filteredItems.removeAt(position) // Remove from filtered list
            notifyItemRemoved(position)
            // Notify range to handle any shifts in positions
            notifyItemRangeChanged(position, filteredItems.size - position)
        }
    }

    // 🔹 Update a single item
    fun updateItem(position: Int, updatedItem: ApiResponse.Data) {
        if (position in 0 until filteredItems.size) {
            val indexInOriginal = userList.indexOf(filteredItems[position])
            if (indexInOriginal != -1) {
                userList[indexInOriginal] = updatedItem // Update original list
            }
            filteredItems[position] = updatedItem // Update filtered list
            notifyItemChanged(position)
        }
    }

    // 🔹 Add multiple items
    fun addItems(newItems: List<ApiResponse.Data>) {
        val startPosition = filteredItems.size
        userList.addAll(newItems)
        filteredItems.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    // 🔹 Clear all items
    fun clearData() {
        val oldSize = filteredItems.size
        userList.clear()
        filteredItems.clear()
        notifyItemRangeRemoved(0, oldSize)
    }
}