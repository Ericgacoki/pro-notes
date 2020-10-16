package com.ericg.pronotes.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings

/**
 * @author eric
 * @date 10/2/20
 */

object Utils {
    val userDatabase: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth?.currentUser
    val userUID: String? = firebaseUser?.uid

   init{
       userDatabase?.collection("users/$userUID/proNotes")?.add(hashMapOf("proNotes doc" to "is real"))
       userDatabase?.collection("users/$userUID/todo")?.add(hashMapOf("todo doc" to "is real"))

        userDatabase?.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }
}