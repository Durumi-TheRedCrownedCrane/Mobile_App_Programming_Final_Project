package edu.skku.cs.moappfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.skku.cs.moappfinal.databinding.ItemPerformanceBinding

class PerformanceAdapter(
    private val onClick: (Performance) -> Unit
) : RecyclerView.Adapter<PerformanceAdapter.ViewHolder>() {

    private val items = mutableListOf<Performance>()

    fun submitList(list: List<Performance>?) {
        items.clear()
        if (list != null) items.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemPerformanceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Performance, onClick: (Performance) -> Unit) {
            binding.tvTitle.text = item.title
            binding.tvPeriod.text = "${item.startDate} ~ ${item.endDate}"
            binding.tvPlace.text = item.place
            binding.tvGenre.text = item.genre
            Glide.with(binding.root).load(item.poster).error(R.drawable.placeholder).into(binding.ivPoster)

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val performance = items[position]
        holder.bind(performance, onClick)
    }

    override fun getItemCount(): Int = items.size
}