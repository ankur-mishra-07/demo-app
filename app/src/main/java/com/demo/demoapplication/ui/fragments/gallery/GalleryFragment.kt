package com.demo.demoapplication.ui.fragments.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.demo.demoapplication.ui.fragments.gallery.GalleryViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.demo.demoapplication.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private var binding: FragmentGalleryBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val galleryViewModel = ViewModelProvider(this).get(
            GalleryViewModel::class.java
        )
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) { text: String? -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}