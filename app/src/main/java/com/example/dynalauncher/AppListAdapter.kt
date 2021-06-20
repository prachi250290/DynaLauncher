package com.example.dynalauncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dynalaunchersdk.models.AppDetail

class AppListAdapter (context: Context?, appListArray: MutableList<AppDetail>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var appList: MutableList<AppDetail>? = null
    private var appListFilter: MutableList<AppDetail>? = null
    private val ITEM = 0
    private val LOADING = 1
    var onItemClick: ((AppDetail) -> Unit)? = null
    private var isLoadingAdded = false

    init {
        appList = ArrayList()
        appListFilter = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when(viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2: View  = inflater.inflate(R.layout.app_list_row, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }


        return viewHolder!!
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1: View = inflater.inflate(R.layout.app_list_row, parent, false)
        viewHolder = AppViewHolder(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = appList!![position]
        when(getItemViewType(position)) {
            ITEM -> {
                val appViewHolder = holder as AppViewHolder
                appViewHolder.bind(app, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (appList == null) 0 else appList!!.size
    }


    protected  class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView
        private val appPackageNameTextView: TextView
        private val appActivityNameTextView: TextView
        private val appIconImageView: ImageView

        fun bind(app: AppDetail, position: Int) {
            appNameTextView.text = app.label
            appPackageNameTextView.text = app.packageName
            appActivityNameTextView.text = app.activityName
            appIconImageView.setImageDrawable(app.icon)
        }

        init {
            appNameTextView = itemView.findViewById(R.id.app_name);
            appPackageNameTextView = itemView.findViewById(R.id.app_package_name);
            appActivityNameTextView = itemView.findViewById(R.id.app_activity_name);
            appIconImageView = itemView.findViewById(R.id.app_icon);
            itemView.setOnClickListener {
                onItemClick?.invoke(appList!!.get(position))
            }
        }
    }



    //Filter Methods
    fun add(r: AppDetail?) {
        r?.let { appList?.add(it) }
        notifyItemInserted(appList!!.size - 1)
    }

    fun addToFilterList(r: AppDetail?) {
        r?.let { appListFilter?.add(it) }
        notifyItemInserted(appList!!.size - 1)
    }

    fun addAll(appResults: List<AppDetail>) {
        for (result in appResults) {
            add(result)
            addToFilterList(result)
        }
    }

    fun remove(r: AppDetail) {
        val position = appList!!.indexOf(r)
        if (position > -1) {
            appList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(AppDetail())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = appList!!.size - 1
        val result = getItem(position)
        if (result != null) {
            appList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): AppDetail {
        return appList!![position]
    }


    override fun getFilter(): Filter? {
        return appListsFilter
    }

    private val appListsFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<AppDetail> = ArrayList()
            filteredList.clear()
            val charString = constraint.toString()
            if (charString == null || charString.isEmpty()) {
                appList!!.clear()
                //dataLists = dataListFilter;
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in appListFilter!!) {
                    if (item.label.toString()?.toLowerCase()?.contains(filterPattern) == true) {
                        filteredList.add(item)
                    }
                }
                appList!!.clear()
                //dataLists = filteredList;
            }
            val results = FilterResults()
            if (filteredList.size > 0) {
                results.values = filteredList
            } else {
                results.values = appListFilter
            }
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            appList?.addAll(filterResults.values as Collection<AppDetail>)
            notifyDataSetChanged()
        }
    }

}