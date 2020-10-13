package com.ericg.pronotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ericg.pronotes.R
import com.ericg.pronotes.adapters.ProNotesRecyclerviewAdapter
import com.ericg.pronotes.model.ProNoteData
import com.ericg.pronotes.viewmodel.GetDataViewModel
import com.ramotion.foldingcell.FoldingCell
import kotlinx.android.synthetic.main.pro_notes_fragment.view.*

/**
 * @author eric
 * @date 10/2/20
 */

class ProNotes : Fragment(), ProNotesRecyclerviewAdapter.OnProNoteClick {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val p1 = ProNoteData(
            "null", "Kotlin",
            "This is a statically typed programming language used to create android application",
            "Oct 11, 2020", null
        )
        val p2 = ProNoteData(
            "null", "Kotlin Lang",
            "statically typed programming language used to create android application \n\n",
            "Oct 11, 2020", null
        )
        val p3 = ProNoteData(
            "null", "Demo Kotlin",
            "This is a stytically typed programming language used to create android application",
            "Oct 11, 2017", null
        )
        val p4 = ProNoteData(
            "null", "Android Kotlin",
            "This is a statically typed programming language used to create android application",
            "Jan 15, 2020", null
        )
        val p5 = ProNoteData(
            "null", "Kotlin",
            "used to create android application",
            "Oct 11, 2020", null
        )
        val p6 = ProNoteData(
            "null", "Kotlin",
            "This is a statically typed programming language used to create android application",
            "Oct 11, 2020", null
        )
        val p7 = ProNoteData(
            "null", "Kotlin Lang",
            "statically typed programming language used to create android application",
            "Oct 11, 2020 10:20 AM", null
        )
        val p = ProNoteData(
            "null", "Kotlin Lang",
            "",
            "Oct 11, 2020 10:20 AM", null
        )
        val p8 = ProNoteData(
            "null", "Demo Kotlin",
            "This is a stytically typed programming language used to create android application",
            "Oct 11, 2017", null
        )
        val p9 = ProNoteData(
            "null", "Android Kotlin",
            "This is a statically typed programming language used to create android application",
            "Jan 15, 2020 ", null
        )
        val p10 = ProNoteData(
            "null", "Eric has made it",
            "Every new project becomes my favorite. i've liked this one very much. Now am not a noob.",
            "Oct 11, 2020", null
        )

        //  val proNotesList = listOf(p1, p2, p3, p4, p5, p6, p7, p, p8, p9, p10)

        val proNoteViewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        ).get(GetDataViewModel::class.java)

        var proNotesList = proNoteViewModel.proNotesList()!!

        var proAdapter = ProNotesRecyclerviewAdapter(this, proNotesList)

        proNoteViewModel.done.observe(viewLifecycleOwner, {
            if (it) {
                proNotesList = proNoteViewModel.proNotesList()!!
                proAdapter = ProNotesRecyclerviewAdapter(this, proNotesList)
                proAdapter.notifyDataSetChanged()
            }
        })

        return inflater.inflate(R.layout.pro_notes_fragment, container, false).apply {
            this.proNotesRecyclerview.apply {
                this.adapter = proAdapter
                proAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun proNoteClick(foldingCell: FoldingCell?, id: Int?, position: Int) {
        when (id) {
            R.id.proNoteFoldingCell -> {
                foldingCell?.toggle(false)
            }
        }
    }
}