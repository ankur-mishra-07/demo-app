package com.demo.demoapplication.ui.fragments.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.demo.demoapplication.ui.fragments.slideshow.SlideshowViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.demo.demoapplication.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {
    private var binding: FragmentSlideshowBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val slideshowViewModel = ViewModelProvider(this).get(
            SlideshowViewModel::class.java
        )
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) { text: String? ->
            textView.text = text
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}