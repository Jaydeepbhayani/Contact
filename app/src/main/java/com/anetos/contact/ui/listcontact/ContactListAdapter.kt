package com.anetos.contact.ui.listcontact

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anetos.contact.R
import com.anetos.contact.data.model.ContactResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


/**
 * * [ContactListAdapter]
 *
 * [RecyclerView.Adapter] class for showing Top-Headlines of news.
 * @author
 * created by Jaydeep Bhayani on 30/10/2020
 */
class ContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()/*(REPO_COMPARATOR)*/ {
    val TAG = javaClass.simpleName
    var itemArrayList: List<ContactResponse> = ArrayList()
    lateinit var context: Context

    var onItemClick: onItemclickListener? = null

    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    fun setData(mContext: Context, setList: List<ContactResponse>) {
        itemArrayList = setList
        context = mContext
        notifyDataSetChanged()
        Log.d(TAG, "notified")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        return SubCategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = itemArrayList.get(position)
        //val data = getItem(position)

        (holder as SubCategoryViewHolder).tvName.text = data.name
        holder.tvPhone.text = data.phone
        //Picasso.get().load(data.urlToImage).into(holder.ivBackground)
        Glide.with(context)
            .load(data.photo)
            .apply(RequestOptions.circleCropTransform())
            .apply(
                RequestOptions.bitmapTransform(CircleCrop())
                    .error(R.drawable.ic_baseline_account_circle_24)
            )
            .transition(
                DrawableTransitionOptions()
                    .crossFade()
            )
            //.circleCrop()
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            //.error(R.drawable.ic_baseline_account_circle_24)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivPhoto)

        holder.ivCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + data.phone)
            context.startActivity(intent)
        }

        holder.container.setOnClickListener {
            //onItemClick?.onItemClick(position, data)
            onItemClick?.onItemClick(position, data)
        }

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        Log.e("size", itemArrayList.size.toString())
        return itemArrayList.size
    }

    private inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val container: ConstraintLayout
        val ivPhoto: ImageView
        val ivCall: ImageView
        var tvName: TextView
        var tvPhone: TextView

        init {
            container = itemView.findViewById(R.id.container)
            ivPhoto = itemView.findViewById(R.id.ivPhoto)
            tvName = itemView.findViewById(R.id.tvName)
            tvPhone = itemView.findViewById(R.id.tvPhone)
            ivCall = itemView.findViewById(R.id.ivCall)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ContactResponse>() {
            override fun areItemsTheSame(
                oldItem: ContactResponse,
                newItem: ContactResponse
            ): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: ContactResponse,
                newItem: ContactResponse
            ): Boolean =
                oldItem.phone == newItem.phone
        }
    }

    /*fun getData(): List<Articles> {
        return itemArrayList
    }*/

    interface onItemclickListener {
        //fun onItemClick(position: Int, data: Articles)
        fun onItemClick(position: Int, item: ContactResponse)
    }

    fun setonItemClickListener(onItemclickListener: onItemclickListener) {
        this.onItemClick = onItemclickListener
    }

    /**
     * Here is the key method to apply the animation
     */
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(
                context,
                android.R.anim.slide_in_left
            )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}