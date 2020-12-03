package com.example.ps


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class QuestionList(private val context: Activity, private var Questions: List<String>) : ArrayAdapter<String>(context,
    R.layout.layout_question_list, Questions) {

    override fun getItem(position: Int): String? {
        return answer!![position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_question_list, null, true)
        if(answer.isEmpty()) {
            var iter = 0
            while (iter < Questions.size){
                answer.add("Empty")
                iter++
            }
        }
        val textViewName = listViewItem.findViewById<View>(R.id.textViewQuestion) as TextView
        val radiogroup1 =  listViewItem.findViewById(R.id.RadioGroup1) as RadioGroup
        radiogroup1.setOnCheckedChangeListener{
            group,i ->
            val radiobut = listViewItem.findViewById(i) as RadioButton

            answer.set(position, radiobut.text.toString())

        }

        val question = Questions[position]
        textViewName.text = "/ " + question + " /"
        return listViewItem
    }
    companion object {
       val answer= ArrayList<String>()


    }
}