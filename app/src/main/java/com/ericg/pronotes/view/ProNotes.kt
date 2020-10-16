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
     /*   val p1 = ProNoteData(
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
        )*/

        //  val proNotesList = listOf(p1, p2, p3, p4, p5, p6, p7, p, p8, p9, p10)
/*
        val proNoteViewModel = ViewModelProvider(
            this, defaultViewModelProviderFactory
        ).get(GetDataViewModel::class.java)

        var proNotesList = listOf<ProNoteData>()
        val proAdapter = ProNotesRecyclerviewAdapter(this, proNotesList)

        proNoteViewModel.done.observe(viewLifecycleOwner, { done ->
            if (done) {
                proNotesList = proNoteViewModel.proNotesList()!!
                // proAdapter = ProNotesRecyclerviewAdapter(this, proNotesList)
                proAdapter.notifyDataSetChanged()
            }
        })*/

        val proNoteViewModel = ViewModelProvider(
            this, defaultViewModelProviderFactory
        ).get(GetDataViewModel::class.java)

        var proNotesList = listOf<ProNoteData>()
        val proNoteAdapter = ProNotesRecyclerviewAdapter(this@ProNotes, proNotesList)

        proNoteViewModel.done.observe(viewLifecycleOwner, { done ->
            if (done) {
                toast("done fetching data")
                proNotesList = proNoteViewModel.getProNotesList()
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