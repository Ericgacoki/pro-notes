package com.ericg.pronotes.model

import com.google.firebase.firestore.FieldValue

/**
 * @author eric
 * @date 10/2/20
 */

data class ProNoteData(
    val docId: String,
    val title: String,
    val body: String,
    val date: String,
    val timeStamp: FieldValue?,
){
    /** for fireStore data objects */
    constructor() : this("", "", "", "",null)
}
