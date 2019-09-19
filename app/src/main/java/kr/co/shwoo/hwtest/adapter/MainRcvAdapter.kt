package kr.co.shwoo.hwtest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.shwoo.hwtest.R
import kr.co.shwoo.hwtest.network.model.Document


class MainRcvAdapter(val context: Context, var docList: MutableList<Document>) :
    RecyclerView.Adapter<MainRcvAdapter.Holder>() {


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(docList[position], context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_linear, parent, false)
        return Holder(view)
    }


    override fun getItemCount(): Int {
        return docList.size
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val ivThumbnail = itemView?.findViewById<ImageView>(R.id.iv_item_main_linear_thumbnail)
        val label = itemView?.findViewById<TextView>(R.id.tv_item_main_linear_label)
        val title = itemView?.findViewById<TextView>(R.id.tv_item_main_linear_title)
        val name = itemView?.findViewById<TextView>(R.id.tv_item_main_linear_name)
        val dateTime = itemView?.findViewById<TextView>(R.id.tv_item_main_linear_datetime)

        fun bind(document: Document, context: Context) {
            // THUMBNAIL
            if(!document.thumbnail.isNullOrBlank()) {
                ivThumbnail?.let {
                    Glide.with(context)
                        .load(document.thumbnail)
                        .override(600, 600)
                        .fitCenter()
                        .into(it)
                }

            } else {
                ivThumbnail?.setImageResource(R.mipmap.ic_launcher)

            }

            // CATEGORY & // NAME
            if(!document.blogname.isNullOrBlank()) {
                label?.setText(R.string.label_category_blog)
                name?.setText(document.blogname)

            } else if(!document.cafename.isNullOrBlank()) {
                label?.setText(R.string.label_category_cafe)
                name?.setText(document.cafename)

            } else {
                // TODO :: check default.
                label?.setText(R.string.label_category_blog)
                name?.setText("")
            }

            // TITLE
            title?.setText(document.title)

            // DATE TIME  (오늘, 어제, 그외(YYYY년 MM월 DD일) )
            dateTime?.setText(document.datetime)

        }
    }
}