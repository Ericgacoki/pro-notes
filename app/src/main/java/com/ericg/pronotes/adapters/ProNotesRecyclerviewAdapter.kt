package com.ericg.pronotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ericg.pronotes.R
import com.ericg.pronotes.model.ProNoteData
import com.ramotion.foldingcell.FoldingCell
import kotlinx.android.synthetic.main.final_pro_note_item.view.*

/**
 * @author eric gacoki
 * @date 10/2/20
 */

class ProNotesRecyclerviewAdapter(
    private val proNoteClickListener: OnProNoteClick,
    private val proNotesList: List<ProNoteData>

) : RecyclerView.Adapter<ProNotesRecyclerviewAdapter.ProNoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProNoteViewHolder {
        return ProNoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.final_pro_note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProNoteViewHolder, position: Int) {
        holder.bind(proNotesList[position])
    }

    override fun getItemCount(): Int = proNotesList.size

    inner class ProNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var foldedState : LinearLayout? = itemView.foldedProNoteState
        private var foldingCell : FoldingCell? = itemView.findViewById(R.id.proNoteFoldingCell)

        private var foldedTitle : TextView? = itemView.findViewById(R.id.foldedProNoteTitle)
        private var foldedBody : TextView? = itemView.findViewById(R.id.foldedProNoteBody)


        private var unfoldedTitle : TextView? = itemView.findViewById(R.id.unfoldedProNoteTitle)
        private var unfoldedBody : TextView? = itemView.findViewById(R.id.unfoldedProNoteBody)
        private var unfoldedDate: TextView? = itemView.findViewById(R.id.proNoteDate)

        private var edit : ImageView? = itemView.findViewById(R.id.editProNote)
        private var delete : ImageView? = itemView.findViewById(R.id.deleteProNote)

        init {

            foldingCell?.setOnClickListener(this)
            edit?.setOnClickListener(this)
            delete?.setOnClickListener(this)
        }

        fun bind(proNoteData: ProNoteData) {
            foldedTitle?.text = proNoteData.title
            foldedBody?.text = proNoteData.body

            unfoldedTitle?.text = proNoteData.title
            unfoldedBody?.text = proNoteData.body

            unfoldedDate?.text = proNoteData.date
        }

        override fun onClick(view: View?) {
            return proNoteClickListener.proNoteClick(
              foldingCell,  view?.id, adapterPosition
            )
        }
    }

    interface OnProNoteClick {
        fun proNoteClick(foldingCell: FoldingCell?, id: Int?, position: Int)
    }
}