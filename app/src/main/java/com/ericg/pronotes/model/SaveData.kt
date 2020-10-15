package com.ericg.pronotes.model

import com.ericg.pronotes.firebase.Utils.userDatabase
import com.ericg.pronotes.firebase.Utils.userUID

/**
 * @author eric
 * @date 10/2/20
 */

class SaveData(
    private val type: DataType,
    private val docID: String,
    private val proNoteData: ProNoteData?,
    private val todoData: TodoData?
) {
    fun saveData() {
        if (type == DataType.PRO_NOTE) {
            userDatabase?.document("users/$userUID/proNotes/$docID")?.set(proNoteData!!)
        } else {
            userDatabase?.document("users/$userUID/todo/$docID")?.set(todoData!!)
        }
    }
}