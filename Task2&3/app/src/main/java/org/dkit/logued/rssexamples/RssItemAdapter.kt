package org.dkit.logued.rssexamples

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RssItemAdapter(private val dataSet: ArrayList<RssItem>) :
    RecyclerView.Adapter<RssItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item: ConstraintLayout
        val title: TextView
        val description: TextView
        val pubDate: TextView
        val copyright: TextView

        init {
            item = view.findViewById(R.id.item)
            title = view.findViewById(R.id.title)
            description = view.findViewById(R.id.description)
            pubDate = view.findViewById(R.id.pubDate)
            copyright = view.findViewById(R.id.copyright)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_layout, viewGroup, false)



        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rssItem = dataSet[position]
        viewHolder.title.text = rssItem.title
        viewHolder.description.text = rssItem.description
        viewHolder.pubDate.text = rssItem.pubDate
        viewHolder.copyright.text = rssItem.copyright

        viewHolder.item.setOnClickListener {
            Toast.makeText(viewHolder.item.context, viewHolder.pubDate.text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = dataSet.size
}