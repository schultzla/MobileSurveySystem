package com.example.ps

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class QuestionList(private val context: Activity, private var Questions: List<String>) : ArrayAdapter<String>(context,
    R.layout.layout_question_list, Questions) {

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_question_list, null, true)

        val textViewName = listViewItem.findViewById<View>(R.id.textViewQuestion) as TextView
        val question = Questions[position]
        textViewName.text = question

        return listViewItem
    }
}