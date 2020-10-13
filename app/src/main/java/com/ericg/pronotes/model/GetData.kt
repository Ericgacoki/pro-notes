package com.ericg.pronotes.model

import com.ericg.pronotes.firebase.Utils.firebaseUser
import com.ericg.pronotes.firebase.Utils.userDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

/**
 * @author eric
 * @date 10/2/20
 */

class GetData(private val type: DataType) {
    fun getData(): Task<QuerySnapshot>? {
        return if (type == DataType.PRO_NOTE) {
            userDatabase?.collection("users/${firebaseUser?.uid}/proNotes")?.orderBy("title")
                ?.get()
        } else {
            userDatabase?.collection("users/${firebaseUser?.uid}/todo")?.orderBy("title")?.get()
        }
    }
}