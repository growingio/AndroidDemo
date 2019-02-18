package com.growingio.giodemo.category

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.include_page_title.*

class CommentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        back.setOnClickListener {
            finish()
        }
        recycler_comments.adapter = MyCommentsAdapter(this)
        recycler_comments.layoutManager = LinearLayoutManager(this)
    }
}


class MyCommentsAdapter(context: Context) : RecyclerView.Adapter<MyCommentsViewHolder>() {
    private val context = context
    private val profileInfo: List<CommentsInfo> = listOf(
        CommentsInfo("科科", "喜欢喜欢喜欢喜欢喜欢死了", false),
        CommentsInfo("坤坤", "质量不错，大容量，够喝", true),
        CommentsInfo("关关", "啥杯子都一样，能喝水就行", false),
        CommentsInfo("民民", "支持支持，造型一般，但是加了 logo 瞬间高大上", false),
        CommentsInfo("老宋", "喜欢，送老婆了，什么好东西，都要送给老婆", false),
        CommentsInfo("飞飞", "不错，得给女朋友买一个，能打折不？", false),
        CommentsInfo("颖颖", "喜欢喜欢喜欢，好评模板，喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢喜欢", false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCommentsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false)

        return MyCommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileInfo.size
    }

    override fun onBindViewHolder(holder: MyCommentsViewHolder, position: Int) {
        holder.name.text = profileInfo[position].name
        holder.comment.text = profileInfo[position].comment
        if (profileInfo[position].isFavorite) {
            holder.favorite.setImageResource(R.drawable.ic_favorite_orange)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_favorite)
            profileInfo[position].isFavorite = true
        }

        holder.favorite.setOnClickListener {
            Toast.makeText(context, "系统默认只给坤坤点赞，不能修改", Toast.LENGTH_SHORT).show()
        }
    }

}

class MyCommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name = itemView.findViewById<View>(R.id.tv_name) as TextView
    val comment = itemView.findViewById<View>(R.id.tv_comment) as TextView
    val favorite = itemView.findViewById<View>(R.id.img_favorite) as ImageView

}

data class CommentsInfo(val name: String, val comment: String, var isFavorite: Boolean)