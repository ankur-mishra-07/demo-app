package com.demo.demoapplication.ui.fragments.search;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.demo.demoapplication.R;
import com.demo.demoapplication.databinding.FragmentSearchBinding;
import com.demo.demoapplication.ui.adapters.AdapterListUserInfo;
import com.demo.demoapplication.ui.models.SearchListModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {

    private AdapterListUserInfo mAdapter;
    private FragmentSearchBinding binding;
    private List<SearchListModel> items = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel galleryViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!items.isEmpty()) {
            items = new ArrayList<>();
        }
        items.add(new SearchListModel("Asim","aseem8384@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Punit","puneet225@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Vinay","vinay97@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Raashi","raashi84@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Saumya","saumya84@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Adnan","adnan83@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Saurabh","saurabh@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Anjali","anjali@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Sandeep","sandeep@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Namrata","namrata@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Shikha","shikha@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Abhilasha","abhilasha@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Tanvi","tanvi@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Mradul","mradul@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Piyush","piyush@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Amrata","amrata@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Ajeeta","ajeeta@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Gargi","gargi@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Nisha","nisha@gmail.com",R.drawable.user));
        items.add(new SearchListModel("Ankita","ankita@gmail.com",R.drawable.user));


        binding.searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        init();
        return root;
    }
    public void init() {

        setAdapter(items);
    }

    private void setAdapter(List<SearchListModel> items) {
        //set data and list adapter
        mAdapter = new AdapterListUserInfo(getActivity(), items, 2);
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // on item list clicked
       mAdapter.setOnItemClickListener((view, name, email, obj, position) -> {
           showDialogImage(name,email,obj);
       });
    }

    private void showDialogImage(String name,String email, Integer imageUrl) {
        final Dialog dialog = new Dialog(getActivity());
        ImageView imageBack;
        CircleImageView imageDp;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_user_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView mName = dialog.findViewById(R.id.name);
        TextView mEmail = dialog.findViewById(R.id.email);
        imageDp = dialog.findViewById(R.id.image);
        imageBack = dialog.findViewById(R.id.image_back);
        mName.setText(name);
        mEmail.setText(email);
        Glide.with(this).load(imageUrl).into(imageDp);
        Glide.with(this).load(imageUrl).into(imageBack);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}