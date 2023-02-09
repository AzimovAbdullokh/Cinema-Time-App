package com.example.movieappazi.ui.zAdapter.movie.adapter_for_popular

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.network.api.utils.Utils
import com.example.movieappazi.R
import com.example.movieappazi.databinding.ItemFavMoviesBinding
import com.example.movieappazi.uiModels.movie.MovieUi
import com.squareup.picasso.Picasso

class SavedMoviesAdapter(
    private val list: List<MovieUi>,
    private val listener: RecyclerFavOnClickListener,
) : ListAdapter<MovieUi, SavedMoviesAdapter.ViewHolder>(SavedMoviesDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fav_movies, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface RecyclerFavOnClickListener {
        fun onLongClick(position: Int)
        fun onItemClick(position: Int)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFavMoviesBinding.bind(itemView)
        fun bind(article: MovieUi) = article.apply {
            with(binding) {
                Picasso.get().load(Utils.POSTER_PATH_URL + article.posterPath).into(posterImage)
                movieTitle.text = article.title
                dateSave.text = article.releaseDate
                rating.text = article.rating.toString()
                voteCount.text = article.voteCount.toString()

                val voteAverage = (popularity!! * 10.0)
                if (voteAverage.toInt() >= 70) {
                    progressView.setProgressColorRes(R.color.green)
                } else if (voteAverage.toInt() in 51..69) {
                    progressView.setProgressColorRes(android.R.color.holo_orange_light)
                } else {
                    progressView.setProgressColorRes(android.R.color.holo_red_dark)
                }
                progressView.setProgress(voteAverage.toInt(), true)
            }
        }
    }
}

class SavedMoviesDiffCallBack : DiffUtil.ItemCallback<MovieUi>() {
    override fun areItemsTheSame(
        oldItem: MovieUi,
        newItem: MovieUi,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: MovieUi,
        newItem: MovieUi,
    ): Boolean {
        return oldItem == newItem
    }
}