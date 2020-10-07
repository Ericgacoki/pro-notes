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
    val mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    val mUser: FirebaseUser? = mAuth?.currentUser
    val userUID: String? = mUser?.uid
}