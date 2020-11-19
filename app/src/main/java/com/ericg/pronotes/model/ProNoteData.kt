package com.ericg.pronotes.model

/**
 * @author eric
 * @date 10/2/20
 */

data class ProNoteData(
    val docId: String,
    val title: String,
    val body: String,
    val date: String,
    val timeStamp: Any?,
){
    /** for fireStore data objects */
    constructor() : this("", "", "", "",null)
}
