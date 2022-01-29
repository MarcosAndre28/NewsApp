package com.example.newsapp.utils

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R
import com.example.newsapp.model.Article

// compartilhar noticias
fun shareNews(context: Context? , article: Article){
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, article.urlToImage)
        putExtra(Intent.EXTRA_STREAM, article.urlToImage)
        putExtra(Intent.EXTRA_TITLE, article.title)
        type = "image/*"
    }
    context?.startActivity(Intent.createChooser(intent,"Compartilha notícias  com "))
}
//Carregar imagem no image view
fun getCircularDrawable(context: Context?): CircularProgressDrawable? {
    return context?.let {
        CircularProgressDrawable(it).apply {
            strokeWidth = 8f
        centerRadius = 48f
        setTint(context.resources.getColor(R.color.bgLineColor))
            start()
    }
    }
}
fun ImageView.loadImage(url : String, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_background)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}
@BindingAdapter("loadImage")
fun  loadImage(imageView: ImageView, url: String){
    if (url != null){
        getCircularDrawable(imageView.context)?.let { imageView.loadImage(url!!, it) }
    }
}