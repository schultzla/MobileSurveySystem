package com.example.ps

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DashboardList(private val context: Activity, private var Survey: List<String>) : ArrayAdapter<String>(context,
    R.layout.dashboard_list, Survey) {

    /*
        Returns the view for each survey item in the dashboard list of a user's surveys
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.dashboard_list, null, true)

        val title = view.findViewById<TextView>(R.id.dashboardTitle)

        title.text = Survey[position]
        return view
    }
}