package com.example.bookreading

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.bookreading.databinding.TheBookListLayoutBinding
import com.example.travelinindia.activities.impClasses.LinkToImage

class TheBookListAdapter(
    private val list:ArrayList<BookInfo>,
    private val context: Context
):RecyclerView.Adapter<TheBookListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: TheBookListLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun itemBinding(imageUrl:String,bookTitle: String,bookAuthorName:String){
            binding.bookTitle.text=bookTitle
            binding.bookAuthorName.text=bookAuthorName
            LinkToImage().loadImg(imageUrl,binding.imageViewBookImage)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(binding = TheBookListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = list[position]
        Log.e("In the adapter",list.size.toString())
        holder.itemBinding(book.bookImageURL, book.bookName, book.authorFullName)
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra(Constant.imageLink, list[position].bookImageURL) // Pass the book's ID
            intent.putExtra(Constant.bookName, list[position].bookName) // Pass the book's ID
            intent.putExtra(Constant.bookAuthorName, list[position].authorFullName) // Pass the book's ID
            intent.putExtra(Constant.bookSummary, list[position].summary) // Pass the book's ID
            context.startActivity(intent)
        }
    }

}
