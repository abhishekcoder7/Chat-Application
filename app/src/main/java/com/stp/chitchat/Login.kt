package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.stp.chitchat.databinding.FragmentLoginBinding
import java.util.concurrent.TimeUnit

class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth
    private var number: String? = null
    private lateinit var name: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String? = null
    private lateinit var pin: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding!!.phoneNumLayout.requestFocus()
        binding!!.sendOtp.setOnClickListener {
            if(checkData()) {
                val phone = "+91$number"
                sendCode(phone)
            }
        }

        binding!!.loginBtn.setOnClickListener {
            if(checkPin()){
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, pin)
                signInWithPhoneAuthCredential(credential)
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //gets called when either the phone number is instantly verified without the need to send or verify otp
                //Or google play services automatically detect incoming otp and perform verification
//                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                //gets called when there is an invalid request for verification
                //for instance, if the phone number format is not valid
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseTooManyRequestsException -> {
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                //gets called when the SMS verification code has been sent to the provided phone number,
                //we now need to ask user to enter the code and then construct a credential by combining
                //the code with a verification ID.
                storedVerificationId = verificationId
                binding!!.otpLayout.requestFocus()
            }
        }


        return binding?.root
    }


    private fun checkData() : Boolean {
        number = binding!!.phoneNumber.text.toString().trim()
        name = binding!!.userName.text.toString()

        when {
            number!!.isEmpty() -> {
                binding!!.phoneNumLayout.apply {
                    error = "Field is required!!!"
                    requestFocus()
                }
                return false
            }
            number!!.length < 10 -> {
                binding!!.phoneNumLayout.apply {
                    error = "Valid phone number is required!!!"
                    requestFocus()
                }
                return false
            }
            name.isEmpty() -> {
                binding!!.userNameLayout.apply {
                    error = "Field is required"
                    requestFocus()
                }
                return false
            }
            else -> return true
        }
    }

    private fun sendCode(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)     //Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)   //Timeout and unit
            .setActivity(this.requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun checkPin() : Boolean {
        pin = binding!!.otp.text.toString()
        if(pin.isEmpty()) {
            binding!!.otpLayout.error = "Field is required!!!"
            binding!!.otpLayout.requestFocus()
            return false
        }
        else if(pin.length < 6) {
            binding!!.otpLayout.error = "Enter valid OTP!!!"
            binding!!.otpLayout.requestFocus()
            return false
        }
        return true
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = UserModel(
                        auth.currentUser!!.phoneNumber!!,
                        name,
//                        "",
//                        "",
                        auth.uid!!
                    )
                    databaseReference.child(auth.uid!!).setValue(user)
//                    val userNum = auth.currentUser!!.phoneNumber!!
//                    val authId = auth.uid
                    val action = LoginDirections.actionLoginToDashboard()
                    findNavController().navigate(action)
                }
                else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this.requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this.requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

}