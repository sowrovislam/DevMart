package com.example.devmart.getuserdata

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devmart.Details_data_Activity
import com.example.devmart.EdtActivity
import com.example.devmart.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserAdapter(private val userList: MutableList<ApiResponse.Data>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var filteredItems = userList.toMutableList()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.billTitle)
        val tvNumbar: TextView = itemView.findViewById(R.id.accountNumber)
        val tvAmount: TextView = itemView.findViewById(R.id.amount)
        val btnEdit: TextView = itemView.findViewById(R.id.btnEdit)
        val tvDescription: TextView = itemView.findViewById(R.id.userDescription)
        val ivImage: CircleImageView = itemView.findViewById(R.id.userImage)
        val tvDueAmount: TextView = itemView.findViewById(R.id.dueDate)
        val CardDetails: CardView = itemView.findViewById(R.id.cardView)
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
//        holder.tvDate.text = "Date: ${user.date ?: "N/A"}"
        holder.tvDescription.text = "Description: ${user.description ?: "N/A"}"

        val amountValue = user.amount?.toDoubleOrNull() ?: 0.0
        val dueValue = user.due?.toDoubleOrNull() ?: 0.0



        holder.tvDueAmount.text = "Due: $dueValue"



        holder.CardDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, Details_data_Activity::class.java)

            // Optional: pass data
             intent.putExtra("userName", user.name)
             intent.putExtra("numbar", user.numbar)
            intent.putExtra("amount", user.amount)

            intent.putExtra("due", user.due)
            intent.putExtra("description", user.description)
            intent.putExtra("image", user.image)

            intent.putExtra("date", user.date)
            holder.itemView.context.startActivity(intent)
        }

         holder.btnEdit.setOnClickListener {
             val intent = Intent(holder.itemView.context, EdtActivity::class.java)

             // Optional: pass data
             intent.putExtra("userName", user.name)
             intent.putExtra("numbar", user.numbar)
             intent.putExtra("amount", user.amount)

             intent.putExtra("due", user.due)
             intent.putExtra("description", user.description)
             intent.putExtra("image", user.image)

             intent.putExtra("id",user.id)

             val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
             intent.putExtra("date", currentDate)
             intent.putExtra("userId",user.userId)
             holder.itemView.context.startActivity(intent)



//             intent.putExtra("date", user.date)


         }



        if (!user.image.isNullOrEmpty()) {
            Log.d("UserAdapter", "Loading image: ${user.image}")

            Glide.with(holder.itemView)
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


    fun reverseItems() {
        filteredItems.reverse() // reverses in-place
        notifyDataSetChanged() // refresh RecyclerView
    }

}