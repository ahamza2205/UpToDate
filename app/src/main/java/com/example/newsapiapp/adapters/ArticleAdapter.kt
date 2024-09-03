package com.example.newsapiapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapiapp.R
import com.example.newsapiapp.Utils
import com.example.newsapiapp.database.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleHolder>() {

    private var newsList = listOf<Article>()
    private var listener: ItemClicklistner? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newlist, parent, false)
        return ArticleHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = newsList[position]
        holder.itemView.apply {
            holder.pb.visibility = View.VISIBLE
            Glide.with(this).load(article.urlToImage)
                .error(R.drawable.news)
                .placeholder(R.drawable.ic_search)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
            holder.pb.visibility = View.GONE

            holder.textTitle.text = article.title
            holder.tvSource.text = article.source!!.name
            holder.tvDescription.text = article.description
            holder.tvPubslishedAt.text = Utils.DateFormat(article.publishedAt)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClicked(position, article)
        }
    }

    fun setItemClickListener(listener: ItemClicklistner) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setlist(news: List<Article>) {
        newsList = news
        notifyDataSetChanged()
    }
}

class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    var textTitle: TextView = itemView.findViewById(R.id.tvTitle)
    var tvSource: TextView = itemView.findViewById(R.id.tvSource)
    var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    var tvPubslishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    var pb: ProgressBar = itemView.findViewById(R.id.pbLoading) // Use the correct ProgressBar ID
}

interface ItemClicklistner {
    fun onItemClicked(position: Int, article: Article)
}
