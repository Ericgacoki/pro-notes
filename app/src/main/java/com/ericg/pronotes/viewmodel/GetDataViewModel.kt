package com.ericg.pronotes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericg.pronotes.model.GetData
import com.ericg.pronotes.model.ProNoteData
import com.ericg.pronotes.model.TodoData

/**
 * @author eric
 * @date 10/7/20
 */

class GetDataViewModel : ViewModel() {
    val done: MutableLiveData<Boolean> = MutableLiveData(false)

    var proNotesList: List<ProNoteData>? = listOf()
    private var proNotesQuerySnapshot = GetData("proNote").getData()?.addOnCompleteListener { get ->
        done.value = false

        if (get.isSuccessful) {
            proNotesList = get.result?.toObjects(ProNoteData::class.java)
            done.value = true
        }
    }


    var todoList: List<TodoData>? = listOf()
    private var todoQuerySnapshot = GetData("todo").getData()?.addOnCompleteListener { get ->
        done.value = false
        if (get.isSuccessful) {
            todoList = get.result?.toObjects(TodoData::class.java)
            done.value = true
        }
    }
}