package com.stp.chitchat

import com.google.firebase.auth.FirebaseAuth

class AppUtil {

    public fun getUid(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }

}