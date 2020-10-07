package com.ericg.pronotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericg.pronotes.R
import com.ericg.pronotes.model.ProNoteData

/**
 * @author eric
 * @date 10/2/20
 */

class ProNotesRecyclerviewAdapter(
    private val proNoteClickListener: OnProNoteClick,
    private val proNotesListData: List<ProNoteData>

) : RecyclerView.Adapter<ProNotesRecyclerviewAdapter.ProNoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProNoteViewHolder {
        return ProNoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.raw_create_pro_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProNoteViewHolder, position: Int) {
        holder.bind(proNotesListData[position])
    }

    override fun getItemCount(): Int = proNotesListData.size

    inner class ProNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {

        }

        fun bind(proNoteData: ProNoteData) {

        }

        override fun onClick(view: View?) {
            return proNoteClickListener.proNoteClick(
                adapterPosition, view?.id
            )
        }
    }

    interface OnProNoteClick {
        fun proNoteClick(position: Int, id: Int?)
    }
}