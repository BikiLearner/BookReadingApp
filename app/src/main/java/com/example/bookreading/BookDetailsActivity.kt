package com.example.bookreading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookreading.databinding.ActivityBookDetailsBinding
import com.example.travelinindia.activities.impClasses.LinkToImage


class BookDetailsActivity : AppCompatActivity() {
    private var binding:ActivityBookDetailsBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        window.statusBarColor=getColor(R.color.white)
        val bookImage=intent.getStringExtra(Constant.imageLink)
        val bookName=intent.getStringExtra(Constant.bookName)
        val bookAuthorName=intent.getStringExtra(Constant.bookAuthorName)
        val bookSummary=intent.getStringExtra(Constant.bookSummary)

        LinkToImage().loadImg(bookImage!!,
            binding!!.bookCoverPage)

        binding!!.bookNameTv.text=bookName!!
        binding!!.bookAuthorNameTv.text=bookAuthorName!!
        binding!!.bookDescriptionTv.text=bookSummary!!
        binding!!.backButtonToolBar.setOnClickListener {
            onBackPressed()
        }
        binding!!.bookMarkButtonToolBar.setOnClickListener {
            Toast.makeText(this@BookDetailsActivity,"No Yet Implemented",Toast.LENGTH_LONG).show()
        }
    }
}