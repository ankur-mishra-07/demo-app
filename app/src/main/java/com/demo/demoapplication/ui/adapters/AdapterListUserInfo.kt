package com.demo.demoapplication.ui.adapters

import com.demo.demoapplication.ui.models.SearchListModel
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.demo.demoapplication.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Filter
import com.bumptech.glide.Glide
import com.demo.demoapplication.ui.utils.ItemAnimation
import android.widget.Filterable
import android.widget.ImageView
import java.util.*

class AdapterListUserInfo(context: Context, items1: List<SearchListModel>, animation_type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var items: List<SearchListModel> = ArrayList()
    private var itemsFilter: List<SearchListModel>? = ArrayList()
    private val ctx: Context
    private var mOnItemClickListener: OnItemClickListener? = null
    private var animation_type = 0

    interface OnItemClickListener {
        fun onItemClick(view: View?, name: String?, email: String?, obj: Int?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView
        var name: TextView
        var email: TextView
        var lyt_parent: View

        init {
            image = v.findViewById<View>(R.id.image) as ImageView
            name = v.findViewById<View>(R.id.name) as TextView
            lyt_parent = v.findViewById(R.id.lyt_parent) as View
            email = v.findViewById<View>(R.id.email) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user_info, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {
            val view = holder
            val p = items[position]
            view.name.text = p.name
            view.email.text = p.email
            Glide.with(ctx).load(p.image).into(view.image)
            setAnimation(view.itemView, position)
            view.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(
                        view,
                        p.name,
                        p.email,
                        items[position].image,
                        position
                    )
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private var lastPosition = -1
    private var on_attach = true
    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (on_attach) position else -1, animation_type)
            lastPosition = position
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                items = results.values as ArrayList<SearchListModel> // has the filtered values
                notifyDataSetChanged() // notifies the data with new filtered values
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                var constraint: CharSequence? = constraint
                val results =
                    FilterResults() // Holds the results of a filtering operation in values
                val filteredArrList: ArrayList<SearchListModel> = ArrayList()
                if (itemsFilter == null) {
                    itemsFilter = ArrayList(items) // saves the original data in mOriginalValues
                }
                /********
                 *
                 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 * else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 */
                if (constraint == null || constraint.isEmpty()) {

                    // set the Original result to return
                    results.count = itemsFilter!!.size
                    results.values = itemsFilter
                } else {
                    constraint = constraint.toString().lowercase(Locale.getDefault())
                    for (i in itemsFilter!!.indices) {
                        val data = itemsFilter!![i].name
                        if (data != null) {
                            if (data.lowercase(Locale.getDefault()).startsWith(constraint.toString())) {
                                filteredArrList.add(
                                    SearchListModel(
                                        itemsFilter!![i].name,
                                        itemsFilter!![i].email,
                                        itemsFilter!![i].image
                                    )
                                )
                            }
                        }
                    }
                    // set the Filtered result to return
                    results.count = filteredArrList.size
                    results.values = filteredArrList
                }
                return results
            }
        }
    }

    init {
        items = items1
        itemsFilter = items1
        ctx = context
        this.animation_type = animation_type
    }
}