package com.ericg.pronotes.model

import com.ericg.pronotes.firebase.Utils.userDatabase
import com.ericg.pronotes.firebase.Utils.userUID

/**
 * @author eric
 * @date 10/15/20
 */

open class GetDataRepository(type: DataType) {
   val taskQuerySnapshot = if (type == DataType.PRO_NOTE){
       userDatabase?.collection("users/$userUID/proNotes")
           ?.orderBy("title")
           ?.get()
   } else {
       userDatabase?.collection("users/$userUID/todo")
           ?.orderBy("title")
           ?.get()
   }
}