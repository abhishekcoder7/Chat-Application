package com.stp.chitchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.stp.chitchat.databinding.FragmentUserDataBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UserData : Fragment() {

//    val args: UserDataArgs by navArgs()

    private var _binding: FragmentUserDataBinding? = null
    private val binding get() = _binding
//
//    private lateinit var userName: String
//    private lateinit var userStatus: String
//    private lateinit var userNumber: String
//    private lateinit var userAuthId: String
//    private var userProfilePicture: Uri? = null
//    private var userProfilePictureUrl: String? = null
//
//    private var auth: FirebaseAuth? = null
//    private var databaseReference: DatabaseReference? = null
//    private var storageReference: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)

//        userNumber = args.userPhoneNumber
//        userAuthId = args.userId
//
//        binding!!.confirmDataBtn.setOnClickListener {
//            if(checkData()) {
//                uploadData(userName, userStatus/*, userProfilePicture!!*/)
//            }
//        }
//
//        binding!!.addPhotoBtn.setOnClickListener {
////            if(checkStoragePermission()) {
////                pickImage()
////            }
////            else storageRequestPermission()
//            Toast.makeText(requireContext(), "Under Progress", Toast.LENGTH_SHORT).show()
//        }
//
        return binding?.root
    }

//    private fun checkData(): Boolean {
//        userName = binding!!.userName.text.toString().trim()
//        userStatus = binding!!.userStatus.text.toString().trim()
//
//        when {
//            userName.isEmpty() -> {
//                binding!!.userNameLayout.apply {
//                    error = "Field is required!!!"
//                    requestFocus()
//                }
//                return false
//            }
//            userStatus.isEmpty() -> {
//                binding!!.statusLayout.apply {
//                    error = "Do tell people about yourself"
//                    requestFocus()
//                }
//                return false
//            }
////            userProfilePicture == null -> {
////                Toast.makeText(requireContext(), "Set profile picture", Toast.LENGTH_SHORT).show()
////                return false
////            }
//            else -> return true
//        }
//    }

//    private fun uploadData(name: String, status: String/*, image: Uri*/) = kotlin.run {
//        Toast.makeText(requireContext(), "1. abhi update nahi hua", Toast.LENGTH_SHORT).show()
//        storageReference?.child(auth!!.uid!!)?.putFile(image)
//            ?.addOnSuccessListener {
//                Toast.makeText(requireContext(), "2. abhi update nahi hua", Toast.LENGTH_SHORT).show()
//                val task = it.storage.downloadUrl
//                task.addOnCompleteListener { uri ->
//                    Toast.makeText(requireContext(), "3. abhi update nahi hua", Toast.LENGTH_SHORT).show()
//                    userProfilePictureUrl = uri.result.toString()
//                    Toast.makeText(requireContext(), "4. abhi update nahi hua, map se just pehle", Toast.LENGTH_SHORT).show()
////                    val map = mapOf(
////                        "userName" to name,
////                        "userStatus" to status,
////                        "profilePhotoUrl" to userProfilePictureUrl
////                    )
//                    val userInfo = UserModel(
//                        userNumber,
//                        name,
////                        userProfilePictureUrl!!,
////                        status,
//                        userAuthId)
//                    Toast.makeText(requireContext(), "5. abhi update nahi hua, map to ban gaya", Toast.LENGTH_SHORT).show()
//                    databaseReference?.child(userAuthId)?.setValue(userInfo)
//                    Toast.makeText(requireContext(), "6. ab update ho gaya", Toast.LENGTH_SHORT).show()
//                    val action = UserDataDirections.actionUserDataToDashboard()
//                    findNavController().navigate(action)
//                }
//            }
//    }
//
//    private fun checkStoragePermission(): Boolean {
//        return ContextCompat.checkSelfPermission(requireContext(),
//            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun storageRequestPermission() =
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ), 1000
//        )
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when(requestCode) {
//            1000 ->
//                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) pickImage()
//                else Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }

    //    private val permissionRequestLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()) {
//        if(it) pickImage()
//        else Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when(requestCode) {
//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                val result = CropImage.getActivityResult(data)
//                if(resultCode == Activity.RESULT_OK) {
//                    userProfilePicture = result.uri
//                    binding!!.profileImage.setImageURI(userProfilePicture)
//                }
//            }
//        }
//    }
//
//    private fun pickImage() {
//        CropImage.activity()
//            .setCropShape(CropImageView.CropShape.OVAL)
//            .start(requireContext(), this)
//    }

}