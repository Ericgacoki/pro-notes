package com.ericg.pronotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericg.pronotes.R
import com.ericg.pronotes.model.TodoData

/**
 * @author eric
 * @date 10/2/20
 */

class TodoRecyclerviewAdapter(
    private val todoClickListener: OnTodoClick,
    private val todoDataList: List<TodoData>

) : RecyclerView.Adapter<TodoRecyclerviewAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_create_todo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoDataList[position])
    }

    override fun getItemCount(): Int = todoDataList.size

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {

        }

        fun bind(todoData: TodoData) {

        }

        override fun onClick(view: View?) {
            return todoClickListener.todoClicked(
                adapterPosition, view?.id
            )
        }
    }

    interface OnTodoClick {
        fun todoClicked(position: Int, id: Int?)
    }
}