package com.stp.chitchat

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stp.chitchat.databinding.FragmentUserProfileBinding

class UserProfile : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        profileViewModel = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)

        profileViewModel.getUser().observe(viewLifecycleOwner, Observer {
            binding!!.apply {
                userDataModel = it
                name.text = it.userName
                bio.text = "Working on this......"
                phone.text = it.phoneNumber
            }
        })

        binding!!.name.setOnClickListener {
            getUserNameEditDialog()
        }

        return binding?.root
    }

    private fun getUserNameEditDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setTitle("Default Alert Dialog!!")
        builder.setMessage("Enter your name here")
        builder.setPositiveButton("Confirm") { dialog, _ ->
            if(input.text.isNotEmpty()) {
                profileViewModel.updateUserName(input.text.toString())
            }
            Toast.makeText(requireContext(), "Username changed to ${input.text}!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }


        //to avoid removal of dialog box on clicking outside the dialog UI
        builder.setCancelable(false)

        val defaultDialog = builder.create()
        defaultDialog.show()

    }

}