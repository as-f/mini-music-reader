package com.albertford.autoflip.editsheetactivity.pagerecycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.albertford.autoflip.R

/**
 * Placeholder adapter for the list of pages shown in the edit sheet activity.
 *
 * This adapter is used when the activity is first loaded so that the user sees a blank white page
 * instead of a flash of black before the real pages are loaded (the real pages are managed by
 * PageAdapter)
 */

class PlaceholderPageAdapter : RecyclerView.Adapter<PlaceholderPageAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.page_tile, parent, false)
        view.layoutParams.width = parent.width - parent.paddingStart - parent.paddingEnd
        view.layoutParams.height = parent.height - parent.paddingTop - parent.paddingBottom
        view.requestLayout()
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
