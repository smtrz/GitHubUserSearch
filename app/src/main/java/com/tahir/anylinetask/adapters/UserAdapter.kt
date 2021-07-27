package com.tahir.anylinetask.adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tahir.anylinetask.R
import com.tahir.anylinetask.interfaces.RVClickCallback
import com.tahir.anylinetask.models.Item
import kotlinx.android.synthetic.main.appartment_row.view.*


open class AppartmentsAdapter(
    var context: Context,
    var notes: List<Item>?,
    var rv: RVClickCallback

)

    : RecyclerView.Adapter<AppartmentsAdapter.AppartmentViewHolder>() {
    var listener: RVClickCallback? = null

    init {
        listener = rv
    }

    fun loadItems(newItems: List<Item>, rv: RVClickCallback) {
        notes = newItems
        listener = rv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppartmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.appartment_row, parent, false)

        return AppartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppartmentViewHolder, position: Int) {

        // Binding all the data with the views.
        try {
            holder.title?.text = notes?.get(position)?.login
            holder.app_desc?.text = notes?.get(position)?.type

            holder.app_booked?.visibility = View.GONE
            holder.app_distance?.visibility = View.GONE

            Picasso.get().load(notes?.get(position)?.avatar_url).into(holder.imageUrl);



        } catch (e: Exception) {
        }

    }

    override fun getItemCount(): Int {
        var count = 0

        if (notes != null && notes!!.count() != 0) {
            count = notes!!.size
        }
        return count

    }

    inner class AppartmentViewHolder
        (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {


        init {

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        internal var title: TextView? = itemView.title
        internal var app_desc: TextView? = itemView.description
        internal var app_booked: TextView? = itemView.app_booked
        internal var app_distance: TextView? = itemView.app_distance
        internal var imageUrl: ImageView? = itemView.notes_img
        override fun onClick(p0: View?) {
            listener?.onItemClick(adapterPosition)

        }

        override fun onLongClick(p0: View?): Boolean {
            return true

        }


    }
    class OnScrollListener(val mLayoutManager: LinearLayoutManager,var moredata:callData) : RecyclerView.OnScrollListener() {
        var previousTotal = 0
        var loading = true
        val visibleThreshold = 10
        var firstVisibleItem = 0
        var visibleItemCount = 0
        var totalItemCount = 0
        var pastVisiblesItems=0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy > 0) { //check for scroll down
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Log.v("...", "Last Item Wow !");
                        // Do pagination.. i.e. fetch new data
                        moredata.getMoreData()
                        loading = true;

                    }
                }
            }
        }
        }
    }

 interface callData {

    fun getMoreData()

}




