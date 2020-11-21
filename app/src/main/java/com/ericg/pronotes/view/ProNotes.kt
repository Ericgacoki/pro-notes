package com.ericg.pronotes.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericg.pronotes.R
import com.ericg.pronotes.adapters.ProNotesRecyclerviewAdapter
import com.ericg.pronotes.extentions.Extensions.toast
import com.ericg.pronotes.firebase.Utils.userDatabase
import com.ericg.pronotes.firebase.Utils.userUID
import com.ericg.pronotes.model.DataType
import com.ericg.pronotes.model.GetDataRepository
import com.ericg.pronotes.model.ProNoteData
import com.ericg.pronotes.model.SaveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FieldValue
import com.ramotion.foldingcell.FoldingCell
import kotlinx.android.synthetic.main.pro_notes_fragment.*
import kotlinx.android.synthetic.main.pro_notes_fragment.view.*
import kotlinx.android.synthetic.main.raw_create_pro_note.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * @author eric
 * @date 10/2/20
 */

class ProNotes : Fragment(), ProNotesRecyclerviewAdapter.OnProNoteClick {
    lateinit var proNoteAdapter: ProNotesRecyclerviewAdapter
    private var previousTitle: String? = ""
    private var previousBody: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* userDatabase?.collection("users/$userUID/proNotes")?.get()?.addOnSuccessListener {
            val notesList = it.toObjects(ProNoteData::class.java)
            proNoteAdapter.apply {
                proNotesList = notesList
                notifyDataSetChanged()
            }
        } */
        //getProNotes(true)

        return inflater.inflate(R.layout.pro_notes_fragment, container, false).apply {
            getProNotes(true)

            this.proNotesRecyclerview.apply {
                adapter = proNoteAdapter
            }
            this.fabCreateProNote.setOnClickListener {
                createProNote()
            }

            this.swipeToRefreshProNotes.setOnRefreshListener {
                getProNotes(false).observe(viewLifecycleOwner, { complete ->
                    if (complete) {
                        swipeToRefreshProNotes.isRefreshing = false
                    }
                })
            }
        }
    }
    /* use the repository directly since viewModel doesn't seem to work well with viewpager2 */

    private fun getProNotes(showLoadingView: Boolean): MutableLiveData<Boolean> {
        val completedGetting: MutableLiveData<Boolean> = MutableLiveData(false)

        loadingView(showLoadingView)
        proNoteAdapter = ProNotesRecyclerviewAdapter(this@ProNotes, listOf<ProNoteData>())
        var fetchedProNotes: List<ProNoteData>

        GetDataRepository(DataType.PRO_NOTE).taskQuerySnapshot?.addOnCompleteListener {
            completedGetting.value = true

            loadingView(false)
            toast("completed fetching pronotes")

            fetchedProNotes = it.result!!.toObjects(ProNoteData::class.java)
            proNoteAdapter.apply {
                proNotesList = fetchedProNotes
                notifyDataSetChanged()
            }

            noDataLay.visibility = if (fetchedProNotes.isEmpty()) VISIBLE else INVISIBLE
        }

        return completedGetting
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProNote() {
        val createProNoteDialog = BottomSheetDialog(requireContext())

        val proNoteView = LayoutInflater.from(this.requireContext()).inflate(
            R.layout.raw_create_pro_note,
            this.requireActivity().findViewById(R.id.createProNoteRootLay)
        ).apply {

            this.newProNoteTitle.setText(previousTitle ?: "")
            this.newProNoteBody.setText(previousBody ?: "")

            val inputs = listOf<EditText>(
                this.newProNoteTitle, newProNoteBody
            )

            saveNewProNote.setOnClickListener {
                val notEmpty: Boolean =
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

                    val savingJob: LiveData<Job> =
                        MutableLiveData<Job>(GlobalScope.launch(Dispatchers.IO) {
                            SaveData(DataType.PRO_NOTE, docId, proNoteData, null).saveData()
                        })

                    toast("uploading . . .")
                    saveNewProNote.isEnabled = false
// todo clear previous inputs and update the UI after saving a new note
                    savingJob.observe(viewLifecycleOwner, { job ->
                        if (job.isCompleted) {
                            // swipeToRefreshProNotes.isRefreshing = true
                            getProNotes(false)
                            createProNoteDialog.dismiss()
                        }
                    })

                } else {
                    inputs.forEach {
                        if (it.text.toString().trim().isEmpty()) {
                            it.error = "${it.hint} is required"
                        }
                    }
                }
            }
        }
        /* in case the user cancels the dialog the inputs are not lost instantly */

        createProNoteDialog.setOnCancelListener {
            previousTitle = proNoteView.newProNoteTitle.text.toString().trim()
            previousBody = proNoteView.newProNoteBody.text.toString().trim()
        }

        createProNoteDialog.apply {
            setContentView(proNoteView)
            setCancelable(true)
            // create()
        }.show()
    }

    private fun loadingView(show: Boolean) {
        if (show) {
            loadingProNotesView.apply {
                this?.visibility = VISIBLE
                this?.setViewColor(R.color.colorOrange)
                this?.startAnim()
            }

        } else {
            loadingProNotesView.apply {
                this?.stopAnim()
                this?.visibility = INVISIBLE
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