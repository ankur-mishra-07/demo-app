package com.demo.demoapplication.ui.fragments.search

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.demo.demoapplication.R
import com.demo.demoapplication.databinding.FragmentSearchBinding
import com.demo.demoapplication.ui.adapters.AdapterListUserInfo
import com.demo.demoapplication.ui.models.SearchListModel
import de.hdodenhof.circleimageview.CircleImageView

class SearchFragment : Fragment() {
    private var mAdapter: AdapterListUserInfo? = null
    private var binding: FragmentSearchBinding? = null
    private var items: MutableList<SearchListModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val galleryViewModel = ViewModelProvider(this).get(
            SearchViewModel::class.java
        )
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        if (items.isNotEmpty()) {
            items = ArrayList()
        }
        items.add(SearchListModel("Asim", "aseem8384@gmail.com", R.drawable.user))
        items.add(SearchListModel("Punit", "puneet225@gmail.com", R.drawable.user))
        items.add(SearchListModel("Vinay", "vinay97@gmail.com", R.drawable.user))
        items.add(SearchListModel("Raashi", "raashi84@gmail.com", R.drawable.user))
        items.add(SearchListModel("Saumya", "saumya84@gmail.com", R.drawable.user))
        items.add(SearchListModel("Adnan", "adnan83@gmail.com", R.drawable.user))
        items.add(SearchListModel("Saurabh", "saurabh@gmail.com", R.drawable.user))
        items.add(SearchListModel("Anjali", "anjali@gmail.com", R.drawable.user))
        items.add(SearchListModel("Sandeep", "sandeep@gmail.com", R.drawable.user))
        items.add(SearchListModel("Namrata", "namrata@gmail.com", R.drawable.user))
        items.add(SearchListModel("Shikha", "shikha@gmail.com", R.drawable.user))
        items.add(SearchListModel("Abhilasha", "abhilasha@gmail.com", R.drawable.user))
        items.add(SearchListModel("Tanvi", "tanvi@gmail.com", R.drawable.user))
        items.add(SearchListModel("Mradul", "mradul@gmail.com", R.drawable.user))
        items.add(SearchListModel("Piyush", "piyush@gmail.com", R.drawable.user))
        items.add(SearchListModel("Amrata", "amrata@gmail.com", R.drawable.user))
        items.add(SearchListModel("Ajeeta", "ajeeta@gmail.com", R.drawable.user))
        items.add(SearchListModel("Gargi", "gargi@gmail.com", R.drawable.user))
        items.add(SearchListModel("Nisha", "nisha@gmail.com", R.drawable.user))
        items.add(SearchListModel("Ankita", "ankita@gmail.com", R.drawable.user))
        binding!!.searchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mAdapter!!.filter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        init()
        return root
    }

    fun init() {
        setAdapter(items)
    }

    private fun setAdapter(items: List<SearchListModel>) {
        //set data and list adapter
        mAdapter = AdapterListUserInfo(requireActivity(), items, 2)
        binding!!.recyclerView.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()

        // on item list clicked
      /*  mAdapter!!.setOnItemClickListener(AdapterListUserInfo.OnItemClickListener { view: View?, name: String, email: String, obj: Int, position: Int ->
            showDialogImage(
                name,
                email,
                obj
            )
        })*/

    }

    private fun showDialogImage(name: String, email: String, imageUrl: Int) {
        val dialog = Dialog(requireActivity())
        val imageBack: ImageView
        val imageDp: CircleImageView
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_user_image)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val mName = dialog.findViewById<TextView>(R.id.name)
        val mEmail = dialog.findViewById<TextView>(R.id.email)
        imageDp = dialog.findViewById(R.id.image)
        imageBack = dialog.findViewById(R.id.image_back)
        mName.text = name
        mEmail.text = email
        Glide.with(this).load(imageUrl).into(imageDp)
        Glide.with(this).load(imageUrl).into(imageBack)
        dialog.findViewById<View>(R.id.bt_close).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}