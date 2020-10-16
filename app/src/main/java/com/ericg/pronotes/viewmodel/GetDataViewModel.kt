package com.ericg.pronotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericg.pronotes.model.DataType
import com.ericg.pronotes.model.GetDataRepository
import com.ericg.pronotes.model.ProNoteData
import com.ericg.pronotes.model.TodoData

open class GetDataViewModel : ViewModel() {
    private val proNotesQuerySnapshot = GetDataRepository(DataType.PRO_NOTE).taskQuerySnapshot
    private val todoQuerySnapshot = GetDataRepository(DataType.TODO).taskQuerySnapshot

    private val mutableDone = MutableLiveData(false)
    val done = mutableDone as LiveData<Boolean>

    fun getProNotesList(): List<ProNoteData> {
        var proNotesList = listOf<ProNoteData>()

        proNotesQuerySnapshot?.addOnCompleteListener {
            mutableDone.value = false

            if (it.isSuccessful) {
                mutableDone.value = true
                proNotesList = it.result!!.toObjects(ProNoteData::class.java)
            } else {
                /*done but unsuccessful*/
                mutableDone.value = true
            }
        }
        return proNotesList
    }

    fun getTodoList(): List<TodoData> {
        var todoList = listOf<TodoData>()

        todoQuerySnapshot?.addOnCompleteListener {
            mutableDone.value = false

            if (it.isSuccessful) {
                mutableDone.value = true
                todoList = it.result!!.toObjects(TodoData::class.java)
            } else {
                /*done but unsuccessful*/
                mutableDone.value = true
            }
        }
        return todoList
    }
}