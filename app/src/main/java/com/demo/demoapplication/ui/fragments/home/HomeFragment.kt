package com.demo.demoapplication.ui.fragments.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.demoapplication.R
import com.demo.demoapplication.databinding.FragmentHomeBinding
import com.demo.demoapplication.ui.utils.ImageUtil
import com.demo.demoapplication.ui.utils.ImageUtil.decodeImage64
import com.demo.demoapplication.ui.utils.SharePrefrancClass
import java.util.regex.Matcher
import java.util.regex.Pattern


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val REQUEST_CODE = 2021
    private var encodeImage: String? = null
    private  var stateStr: String? = null
    private val password_regex =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    var countryArray = arrayOf("USA")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        stateStr = SharePrefrancClass.getInstance(activity)!!.getPref("stateStr")
        encodeImage = SharePrefrancClass.getInstance(activity)!!.getPref("encodeImage")
        if (encodeImage != null) {
            val decode = decodeImage64(encodeImage)
            binding!!.userImage.setImageBitmap(decode)
        }


        binding!!.editCurrentPass.addTextChangedListener(textWatcher)
        binding!!.editNewPass.addTextChangedListener(textWatcher)
        binding!!.editConfirmPass.addTextChangedListener(textWatcher)
        binding!!.uploadImage.setOnClickListener { v: View? -> selectImage() }

        getStateSpinner(resources.getStringArray(R.array.states))
        getCountrySpinner(countryArray)
        return root
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!isValidPassword(s.toString())) {
                if (binding!!.editCurrentPass.text.hashCode() == s.hashCode() && !binding!!.editCurrentPass.text.toString()
                        .isEmpty()
                ) {
                    binding!!.editConfirmPass.background =
                        resources.getDrawable(R.drawable.edit_border_red)
                    binding!!.editCurrentPass.error =
                        resources.getString(R.string.change_pass_below)
                    binding!!.editCurrentPass.requestFocus()
                }
                if (binding!!.editNewPass.text.hashCode() == s.hashCode() && !binding!!.editNewPass.text.toString()
                        .isEmpty()
                ) {
                    binding!!.editNewPass.background =
                        resources.getDrawable(R.drawable.edit_border_red)
                    binding!!.editNewPass.error = resources.getString(R.string.change_pass_below)
                    binding!!.editNewPass.requestFocus()
                }
                if (binding!!.editConfirmPass.text.hashCode() == s.hashCode() && !binding!!.editConfirmPass.text.toString()
                        .isEmpty()
                ) {
                    binding!!.editConfirmPass.background =
                        resources.getDrawable(R.drawable.edit_border_red)
                    binding!!.editConfirmPass.error =
                        resources.getString(R.string.change_pass_below)
                    binding!!.editConfirmPass.requestFocus()
                }
            } else {
                if (binding!!.editCurrentPass.text.hashCode() == s.hashCode()) {
                    binding!!.editConfirmPass.background =
                        resources.getDrawable(R.drawable.edit_border)
                } else if (binding!!.editNewPass.text.hashCode() == s.hashCode()) {
                    binding!!.editNewPass.background = resources.getDrawable(R.drawable.edit_border)
                } else if (binding!!.editConfirmPass.text.hashCode() == s.hashCode() && isValidate) {
                    binding!!.editConfirmPass.background =
                        resources.getDrawable(R.drawable.edit_border)
                    Toast.makeText(activity, "Password successfully changed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    val isValidate: Boolean
        get() {
            if (binding!!.editNewPass.text.toString() != binding!!.editConfirmPass.text.toString()) {
                binding!!.editConfirmPass.error = "Password doesn't match"
                return false
            }
            return true
        }

    private fun selectImage() {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val layoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.dialog_capture_image_layout, null)
        builder.setCancelable(true)
        builder.setView(dialogView)
        val cameraLL = dialogView.findViewById<LinearLayout>(R.id.LL_Camera)
        val galleryFilesLL = dialogView.findViewById<LinearLayout>(R.id.LL_FilesGallery)
        val alertDialogPicture = builder.create()
        alertDialogPicture.show()
        cameraLL.setOnClickListener {
            if (checkAndRequestPermissions(activity)) {
                takePicFromCam()
                alertDialogPicture.dismiss()
            }
        }
        galleryFilesLL.setOnClickListener {
            if (checkAndRequestPermissions(activity)) {
                takePicFromFilesGallery()
                alertDialogPicture.dismiss()
            }
        }
    }

    // Capture Pic from Gallery/Files
    private fun takePicFromFilesGallery() {
        try {
            val pickPhoto = Intent(Intent.ACTION_PICK)
            pickPhoto.type = "image/*"
            startActivityForResult(Intent.createChooser(pickPhoto, "Select Image"), 101)
        } catch (e: Exception) {
            Toast.makeText(activity, "$e @ takePicFromCam", Toast.LENGTH_SHORT).show()
            Log.e("Tag takePicFromCam", e.message!!)
        }
    }

    // Capture Pic from Camera
    private fun takePicFromCam() {
        try {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePicture.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePicture, 202)
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "$e @ takePicFromCam", Toast.LENGTH_SHORT).show()
            Log.e("Tag takePicFromCam", e.message!!)
        }
    }

    // function to check permission
    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val RExtstorePermission =
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) }
        val cameraPermission =
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (RExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            if (context != null) {
                ActivityCompat.requestPermissions(
                    context,
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_CODE
                )
            }
            return false
        }
        return true
    }

    // Handled permission Result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "It Requires Access to Camara.", Toast.LENGTH_SHORT).show()
            } else if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "It Requires Access to Your Storage.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                selectImage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                101 -> if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                        val encodedImage = ImageUtil.encodeImage64(bitmap)
                        SharePrefrancClass.getInstance(activity)!!
                            .savePref("encodeImage", encodedImage)

                        val decode = ImageUtil.decodeImage64(encodedImage)
                        binding!!.userImage.setImageBitmap(decode)
                    } catch (e: Exception) {
                        Log.e("Category exception ", " Image upload:  " + e.message)
                    }
                }
                202 -> if (resultCode == Activity.RESULT_OK) {
                    try {
                        val bundle = data!!.extras
                        val bitmapImage = bundle!!["data"] as Bitmap?
                        val encodedImage = bitmapImage?.let { ImageUtil.encodeImage64(it) }
                        SharePrefrancClass.getInstance(activity)!!
                            .savePref("encodeImage", encodedImage)

                        val decode = ImageUtil.decodeImage64(encodedImage)
                        binding!!.userImage.setImageBitmap(decode)
                    } catch (e: Exception) {
                        Log.e("Category C Image upload", e.message!!)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        pattern = Pattern.compile(password_regex)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
    fun getStateSpinner(arrlist: Array<String>) {
        binding?.editStateSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                    val item = parent?.getItemAtPosition(position).toString()
                    SharePrefrancClass.getInstance(activity)!!.savePref("stateStr", item)
                    stateStr = item
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val dataAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, arrlist)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.editStateSpinner?.setAdapter(dataAdapter)
        if (stateStr != null) {
            SharePrefrancClass.getInstance(activity)!!.savePref("stateStr", stateStr)
            val spinnerPosition1 = dataAdapter.getPosition(stateStr)
            binding?.editStateSpinner?.setSelection(spinnerPosition1)
        }
    }

    fun getCountrySpinner(arrlist: Array<String>) {
        val dataAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, arrlist)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.editCountrySpinner?.setAdapter(dataAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        SharePrefrancClass.getInstance(activity)!!.clearPref("encodeImage")
        SharePrefrancClass.getInstance(activity)!!.clearPref("stateStr")
    }

    override fun onResume() {
        super.onResume()
        binding!!.editCurrentPass.setText("")
        binding!!.editNewPass.setText("")
        binding!!.editConfirmPass.setText("")
    }
}