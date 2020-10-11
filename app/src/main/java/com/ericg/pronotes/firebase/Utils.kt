package com.ericg.pronotes.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 * @author eric
 * @date 10/2/20
 */

object Utils {
    val userDatabase: FirebaseFirestore? = FirebaseFirestore.getInstance()
    val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth?.currentUser
    val userUID: String? = firebaseUser?.uid
}