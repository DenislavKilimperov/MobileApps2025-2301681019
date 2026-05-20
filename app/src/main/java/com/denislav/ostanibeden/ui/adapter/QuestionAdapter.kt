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
    private var questions: List<Question>,
    private val onDeleteClick: (Question) -> Unit,
    private val onEditClick: (Question) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvQuestion: TextView = view.findViewById(R.id.tvQuestion)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
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
        holder.btnDelete.setOnClickListener {

            onDeleteClick(question)
        }
        holder.btnEdit.setOnClickListener {

            onEditClick(question)
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun updateData(newQuestions: List<Question>) {
        questions = newQuestions
        notifyDataSetChanged()
    }
}