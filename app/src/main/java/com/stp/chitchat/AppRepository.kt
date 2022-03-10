package com.stp.chitchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class AppRepository {

    private var mutableLiveData: MutableLiveData<UserModel>? = null
    private lateinit var databaseReference: DatabaseReference
    private val appUtil = AppUtil()

    object StaticFunction {
        private var instance: AppRepository? = null
        fun getInstance(): AppRepository {
            if(instance == null)
                instance = AppRepository()
            return instance!!
        }
    }

    fun getUser(): LiveData<UserModel> {
        if(mutableLiveData == null)
            mutableLiveData = MutableLiveData()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(appUtil.getUid()!!)
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    mutableLiveData!!.postValue(userModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return mutableLiveData!!
    }

    fun updateUserName(name: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(appUtil.getUid()!!)

        val map = mapOf("userName" to name)
        databaseReference.updateChildren(map)
    }

}