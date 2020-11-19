package com.ericg.pronotes.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ericg.pronotes.R
import com.ericg.pronotes.adapters.ProNotesRecyclerviewAdapter
import com.ericg.pronotes.extentions.Extensions.toast
import com.ericg.pronotes.firebase.Utils.userDatabase
import com.ericg.pronotes.firebase.Utils.userUID
import com.ericg.pronotes.model.DataType
import com.ericg.pronotes.model.ProNoteData
import com.ericg.pronotes.model.SaveData
import com.ericg.pronotes.viewmodel.GetDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FieldValue
import com.ramotion.foldingcell.FoldingCell
import kotlinx.android.synthetic.main.pro_notes_fragment.view.*
import kotlinx.android.synthetic.main.raw_create_pro_note.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * @author eric
 * @date 10/2/20
 */

class ProNotes : Fragment(), ProNotesRecyclerviewAdapter.OnProNoteClick {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val proNoteViewModel = ViewModelProvider(
            this@ProNotes
        ).get(GetDataViewModel::class.java)

        val proNoteAdapter = ProNotesRecyclerviewAdapter(this@ProNotes, listOf<ProNoteData>())

        userDatabase?.collection("users/$userUID/proNotes")?.get()?.addOnSuccessListener {
            val notesList = it.toObjects(ProNoteData::class.java)
            proNoteAdapter.apply {
                proNotesList = notesList
                notifyDataSetChanged()
            }
        }
        // todo check this out and add no data image visibility

        proNoteViewModel.mutableDone.observe(viewLifecycleOwner, { done ->
            if (done) {
                toast("done loading from ${Thread.currentThread().name}")
                proNoteAdapter.proNotesList = proNoteViewModel.getProNotesList()
                proNoteAdapter.notifyDataSetChanged()
            }
        })

        return inflater.inflate(R.layout.pro_notes_fragment, container, false).apply {
            this.proNotesRecyclerview.apply {
                adapter = proNoteAdapter
            }

            this.fabCreateProNote.setOnClickListener {
                createProNote()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProNote() {
        val createProNoteDialog = BottomSheetDialog(requireContext())

        val proNoteView = LayoutInflater.from(this.requireContext()).inflate(
            R.layout.raw_create_pro_note,
            this.requireActivity().findViewById(R.id.createProNoteRootLay)
        ).apply {

            val inputs = listOf<EditText>(
                this.newProNoteTitle, newProNoteBody
            )

            saveNewProNote.setOnClickListener {
                val notEmpty =
                    newProNoteTitle.text.toString().trim().isNotEmpty() &&
                            newProNoteBody.text.toString().trim().isNotEmpty()

                if (notEmpty) {
                    val docId = userDatabase?.collection("users/$userUID/proNotes")?.document()?.id
                    val noteTitle = newProNoteTitle.text.toString().trim()
                    val noteBody = newProNoteBody.text.toString().trim()
                    val date = LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                    val timeStamp = FieldValue.serverTimestamp()

                    val proNoteData = ProNoteData(docId!!, noteTitle, noteBody, date, timeStamp)
                    GlobalScope.launch(Dispatchers.IO) {
                        SaveData(DataType.PRO_NOTE, docId, proNoteData, null).saveData()
                    }
                    createProNoteDialog.dismiss()
                    toast("uploading . . .")

                    // todo refresh data in the viewModel to reflect to the recyclerview

                } else {
                    inputs.forEach {
                        if (it.text.toString().trim().isEmpty()) {
                            it.error = "${it.hint} is required"
                        }
                    }
                }
            }
        }

        createProNoteDialog.apply {
            setContentView(proNoteView)
            setCancelable(true)
            // create()
        }.show()

    }

    override fun proNoteClick(foldingCell: FoldingCell?, id: Int?, position: Int) {
        when (id) {
            R.id.proNoteFoldingCell -> {
                foldingCell?.toggle(false)
            }
        }
    }
}