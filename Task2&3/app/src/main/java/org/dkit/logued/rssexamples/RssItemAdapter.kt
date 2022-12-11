package org.dkit.logued.rssexamples

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView

class RssItemAdapter(private val dataSet: ArrayList<RssItem>) :
    RecyclerView.Adapter<RssItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item: ConstraintLayout
        val title: TextView
        val description: TextView
        val copyright: TextView

        init {
            item = view.findViewById(R.id.item)
            title = view.findViewById(R.id.title)
            description = view.findViewById(R.id.description)
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

        val title = (position + 1).toString() + ". " + rssItem.title
        viewHolder.title.text = title
        viewHolder.description.text = HtmlCompat.fromHtml(rssItem.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY);
        viewHolder.copyright.text = rssItem.copyright

        viewHolder.item.setOnClickListener {
            Toast.makeText(viewHolder.item.context, "Posted on " + rssItem.pubDate, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = dataSet.size
}