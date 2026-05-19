package com.denislav.ostanibeden.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denislav.ostanibeden.R
import com.denislav.ostanibeden.data.local.Question

class QuestionAdapter(
    private var questions: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvQuestion: TextView = view.findViewById(R.id.tvQuestion)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)

        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {

        val question = questions[position]

        holder.tvQuestion.text = question.questionText
        holder.tvCategory.text = question.category
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun updateData(newQuestions: List<Question>) {
        questions = newQuestions
        notifyDataSetChanged()
    }
}