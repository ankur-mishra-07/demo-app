package com.demo.demoapplication.ui.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.demo.demoapplication.ui.fragments.main.NotificationViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.demo.demoapplication.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private var binding: FragmentNotificationBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val galleryViewModel = ViewModelProvider(this).get(
            NotificationViewModel::class.java
        )
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textNotification
        galleryViewModel.text.observe(viewLifecycleOwner) { text: String? -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}