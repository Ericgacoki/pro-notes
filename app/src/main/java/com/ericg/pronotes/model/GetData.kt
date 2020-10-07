package com.ericg.pronotes.model

import com.ericg.pronotes.firebase.Utils.mUser
import com.ericg.pronotes.firebase.Utils.userDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

/**
 * @author eric
 * @date 10/2/20
 */

class GetData(private val type: String) {
    fun getData(): Task<QuerySnapshot>? {
        return if (type == "proNote") {
            userDatabase?.collection("users/${mUser?.uid}/proNotes")?.orderBy("timeStamp")?.get()
        } else {
            userDatabase?.collection("users/${mUser?.uid}/todo")?.orderBy("timeStamp")?.get()
        }
    }
}