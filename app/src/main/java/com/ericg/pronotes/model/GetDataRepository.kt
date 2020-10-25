package com.ericg.pronotes.model

import com.ericg.pronotes.firebase.Utils.userDatabase
import com.ericg.pronotes.firebase.Utils.userUID
import com.google.firebase.firestore.Query

/**
 * @author eric
 * @date 10/15/20
 */

open class GetDataRepository(type: DataType) {
   val taskQuerySnapshot = if (type == DataType.PRO_NOTE){
       userDatabase?.collection("users/$userUID/proNotes")
           ?.orderBy("timeStamp", Query.Direction.DESCENDING)
           ?.get()
   } else {
       userDatabase?.collection("users/$userUID/todo")
           ?.orderBy("timeStamp", Query.Direction.DESCENDING)
           ?.get()
   }
}