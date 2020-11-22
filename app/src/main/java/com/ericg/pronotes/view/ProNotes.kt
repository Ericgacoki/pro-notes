package com.ericg.pronotes.view

import android.app.AlertDialog
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
    private var fetchedProNotes: List<ProNoteData> = listOf()
    lateinit var fragmentView: View
    private var previousTitle: String? = ""
    private var previousBody: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentView = inflater.inflate(R.layout.pro_notes_fragment, container, false)
        return fragmentView.apply {

            loadingView(true)
            getProNotes()
            this.proNotesRecyclerview.apply {
                adapter = proNoteAdapter
            }
            this.fabCreateProNote.setOnClickListener {
                createProNote()
            }

            this.swipeToRefreshProNotes.setOnRefreshListener {
                getProNotes().observe(viewLifecycleOwner, { complete ->
                    if (complete) {
                        this.proNotesRecyclerview.adapter?.notifyDataSetChanged()
                        swipeToRefreshProNotes.isRefreshing = false
                    }
                })
            }
        }
    }

/* use the repository directly since viewModel doesn't seem to work well with viewpager2 */

    private fun getProNotes(): MutableLiveData<Boolean> {
        val completedGetting: MutableLiveData<Boolean> = MutableLiveData(false)

        proNoteAdapter = ProNotesRecyclerviewAdapter(this@ProNotes, listOf<ProNoteData>())

        GetDataRepository(DataType.PRO_NOTE).taskQuerySnapshot?.addOnCompleteListener { it ->
            completedGetting.value = true

            toast("completed fetching pronotes")
            loadingView(false)

            fetchedProNotes = it.result!!.toObjects(ProNoteData::class.java)

            fragmentView.proNotesRecyclerview.adapter = proNoteAdapter.also { adapter ->
                adapter.proNotesList = fetchedProNotes
                adapter.notifyDataSetChanged()
            }

            fragmentView.noDataLay.visibility =
                if (fetchedProNotes.isEmpty()) VISIBLE else INVISIBLE
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

                    previousTitle = ""
                    previousBody = ""

                    savingJob.observe(viewLifecycleOwner, { job ->
                        if (job.isCompleted) {
                            loadingView(true)
                            getProNotes().observe(viewLifecycleOwner, { got ->
                                if (got) {
                                    fragmentView.proNotesRecyclerview.adapter?.notifyDataSetChanged()
                                }
                            })
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
            fragmentView.loadingProNotesView.apply {
                visibility = VISIBLE
                setViewColor(R.color.colorOrange)
                startAnim()
            }
        } else {
            fragmentView.loadingProNotesView.apply {
                stopAnim()
                visibility = INVISIBLE
            }
        }
    }

    override fun proNoteClick(
        foldingCell: FoldingCell?,
        btnEdit: View?,
        btnDel: View?,
        id: Int?,
        position: Int
    ) {
        when (id) {
            R.id.proNoteFoldingCell -> {
                foldingCell?.toggle(false)
            }

            R.id.deleteProNote -> {
                AlertDialog.Builder(requireContext(), 3).apply {
                    setTitle("Sure to delete")
                    setPositiveButton("Yes") { _, _ ->
                        loadingView(true)

                        btnEdit?.isEnabled = false
                        btnDel?.isEnabled = false

                        userDatabase?.document("users/$userUID/proNotes/${fetchedProNotes[position].docId}")
                            ?.delete()
                            ?.addOnCompleteListener {
                                toast("deleted successfully !")
                                getProNotes()
                            }
                    }
                    setNegativeButton("No") { _, _ ->
                        /* cancel the dialog */
                    }
                }.create().show()
            }
            R.id.editProNote -> {
                val currentTitle = fetchedProNotes[position].title
                val currentBody = fetchedProNotes[position].body
                val docId = fetchedProNotes[position].docId

                var newTitle: String
                var newBody: String

                val bottomSheetLay = layoutInflater.inflate(R.layout.raw_create_pro_note, null)
                BottomSheetDialog(requireContext()).apply {
                    setContentView(bottomSheetLay)
                    bottomSheetLay.apply {
                        this.newProNoteTitle.setText(currentTitle)
                        this.newProNoteBody.setText(currentBody)

                        this.saveNewProNote.setOnClickListener {
                            newTitle = bottomSheetLay.newProNoteTitle.text.toString().trim()
                            newBody = bottomSheetLay.newProNoteBody.text.toString().trim()

                            if (newTitle.isNotEmpty() && newBody.isNotEmpty()) {

                                loadingView(true)
                                userDatabase?.document("users/$userUID/proNotes/$docId")?.update(
                                    hashMapOf(
                                        "title" to newTitle,
                                        "body" to newBody
                                    ) as Map<String, Any>
                                )?.addOnCompleteListener {
                                    dismiss()
                                    getProNotes()
                                }

                            } else if (newTitle.isEmpty()) {
                                bottomSheetLay.newProNoteTitle.error =
                                    bottomSheetLay.newProNoteTitle.hint.toString() + " is required"
                            } else {
                                bottomSheetLay.newProNoteBody.error =
                                    bottomSheetLay.newProNoteBody.hint.toString() + " is required"
                            }
                        }
                    }
                }.show()
            }
        }
    }
}