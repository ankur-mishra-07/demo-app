package com.demo.demoapplication.ui.fragments.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.demoapplication.R;
import com.demo.demoapplication.databinding.FragmentHomeBinding;
import com.demo.demoapplication.ui.utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final int REQUEST_CODE = 2021;
    private final String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.editCurrentPass.addTextChangedListener(textWatcher);
        binding.editNewPass.addTextChangedListener(textWatcher);
        binding.editConfirmPass.addTextChangedListener(textWatcher);

        binding.uploadImage.setOnClickListener(v -> {
            selectImage();
        });

        return root;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isValidPassword(String.valueOf(s))) {
                if (binding.editCurrentPass.getText().hashCode() == s.hashCode() && !binding.editCurrentPass.getText().toString().isEmpty()) {
                    binding.editConfirmPass.setBackground(getResources().getDrawable(R.drawable.edit_border_red));
                    binding.editCurrentPass.setError(getResources().getString(R.string.change_pass_below));
                    binding.editCurrentPass.requestFocus();
                }
                if (binding.editNewPass.getText().hashCode() == s.hashCode() && !binding.editNewPass.getText().toString().isEmpty()) {
                    binding.editNewPass.setBackground(getResources().getDrawable(R.drawable.edit_border_red));
                    binding.editNewPass.setError(getResources().getString(R.string.change_pass_below));
                    binding.editNewPass.requestFocus();
                }
                if (binding.editConfirmPass.getText().hashCode() == s.hashCode() && !binding.editConfirmPass.getText().toString().isEmpty()) {
                    binding.editConfirmPass.setBackground(getResources().getDrawable(R.drawable.edit_border_red));
                    binding.editConfirmPass.setError(getResources().getString(R.string.change_pass_below));
                    binding.editConfirmPass.requestFocus();
                }
            } else {
                if (binding.editCurrentPass.getText().hashCode() == s.hashCode()) {
                    binding.editConfirmPass.setBackground(getResources().getDrawable(R.drawable.edit_border));
                } else if (binding.editNewPass.getText().hashCode() == s.hashCode()) {
                    binding.editNewPass.setBackground(getResources().getDrawable(R.drawable.edit_border));
                } else if (binding.editConfirmPass.getText().hashCode() == s.hashCode() && isValidate()) {
                    binding.editConfirmPass.setBackground(getResources().getDrawable(R.drawable.edit_border));
                    Toast.makeText(getActivity(), "Password successfully changed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    boolean isValidate(){
        if (!binding.editNewPass.getText().toString().equals(binding.editConfirmPass.getText().toString())){
            binding.editConfirmPass.setError("Password doesn't match");
            return false;
        }
        return true;
    }

    private void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_capture_image_layout,null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        LinearLayout cameraLL = dialogView.findViewById(R.id.LL_Camera);
        LinearLayout galleryFilesLL = dialogView.findViewById(R.id.LL_FilesGallery);

        AlertDialog alertDialogPicture = builder.create();
        alertDialogPicture.show();

        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(getActivity())){
                    takePicFromCam();
                    alertDialogPicture.dismiss();
                }
            }
        });

        galleryFilesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(getActivity())){
                    takePicFromFilesGallery();
                    alertDialogPicture.dismiss();
                }
            }
        });

    }

    // Capture Pic from Gallery/Files
    private void takePicFromFilesGallery() {
        try {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK);
            pickPhoto.setType("image/*");
            startActivityForResult(Intent.createChooser(pickPhoto,"Select Image"),101);
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e+ " @ takePicFromCam", Toast.LENGTH_SHORT).show();
            Log.e("Tag takePicFromCam",e.getMessage());
        }
    }

    // Capture Pic from Camera
    private void takePicFromCam() {
        try {
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicture.resolveActivity(getActivity().getPackageManager()) != null){
                startActivityForResult(takePicture, 202);
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e+ " @ takePicFromCam", Toast.LENGTH_SHORT).show();
            Log.e("Tag takePicFromCam",e.getMessage());
        }
    }

    // function to check permission
    public boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int RExtstorePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (RExtstorePermission != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE);
            return false;
        }
        return true;
    }
    // Handled permission Result

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "It Requires Access to Camara.", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "It Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();
                } else {
                    selectImage();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode)
            {
                case 101:     // Capture Pic from Gallery
                    if(resultCode == RESULT_OK){
                        Uri uri = data.getData();
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                            String encodedImage = ImageUtil.encodeImage64(bitmap);
                            Bitmap decode = ImageUtil.decodeImage64(encodedImage);
                            binding.userImage.setImageBitmap(decode);
                        }
                        catch (Exception e){
                            Log.e("Category exception "," Image upload:  "+e.getMessage());
                        }
                    }
                    break;
                case 202:     // Capture Pic from Camera
                    if(resultCode == RESULT_OK){
                        try {
                            Bundle bundle = data.getExtras();
                            Bitmap bitmapImage = (Bitmap) bundle.get("data");
                            String encodedImage = ImageUtil.encodeImage64(bitmapImage);
                            Bitmap decode = ImageUtil.decodeImage64(encodedImage);
                            binding.userImage.setImageBitmap(decode);
                        }
                        catch (Exception e){
                            Log.e("Category C Image upload",e.getMessage());
                        }
                    }
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(password_regex);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.editCurrentPass.setText("");
        binding.editNewPass.setText("");
        binding.editConfirmPass.setText("");
    }
}