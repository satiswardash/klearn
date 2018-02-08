package com.kortain.klearn.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kortain.klearn.MessageActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttachFeedImages extends Fragment {

    private static final String TAG = AttachFeedImages.class.toString();
    private MessageActivity mActivity;
    private Uri mUri;

    private ImageView mBackNavigation;
    private Button mNextButton;
    private Button mAttachButton;
    private ImageView mAttachedImage;
    private LinearLayout mPreviewImageLayout;
    private ProgressBar mProgressBar;

    /**
     * Required empty public constructor
     */
    public AttachFeedImages() {
    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageActivity) {
            mActivity = (MessageActivity) context;
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attach_feed_images, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBackNavigation = view.findViewById(R.id.if_nav_back);
        mNextButton = view.findViewById(R.id.if_attach_image_next_button);
        mAttachButton = view.findViewById(R.id.if_attach_image_button);
        mAttachedImage = view.findViewById(R.id.if_attached_image);
        mPreviewImageLayout = view.findViewById(R.id.if_preview_image_layout);
        mProgressBar = view.findViewById(R.id.upload_image_progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        });

        mAttachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttachImageDialog();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Upload image file to firestore and retrieve the download url
                if (mPreviewImageLayout.getVisibility() == View.VISIBLE) {
                    /*mProgressBar.setVisibility(View.VISIBLE);
                    mNextButton.setEnabled(false);
                    uploadBitmapImageToFirebase();*/
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.anf_frame_layout, new TextEditor(), TextEditor.class.toString())
                            .addToBackStack(TextEditor.class.toString())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Attach your image first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mPreviewImageLayout.getVisibility() == View.VISIBLE) {
            mPreviewImageLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Upload image to firebase
     */
    private void uploadBitmapImageToFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance(Constants.FEED_IMAGES_BUCKET);
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference imageRef = storageRef.child("feed_images/"+ UUID.randomUUID().toString()+".jpg");

        // Get the data from an ImageView as bytes
        Bitmap bitmap = null;
        try {
            InputStream ims = getContext().getContentResolver().openInputStream(mActivity.imageURI);
            bitmap = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "uploadBitmapImageToFirebase: ", e.getCause());
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mActivity.imageURI = taskSnapshot.getDownloadUrl();

                        mNextButton.setEnabled(false);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.anf_frame_layout, new TextEditor(), TextEditor.class.toString())
                                .addToBackStack(TextEditor.class.toString())
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO
                    }
                });
    }

    /**
     * Show {@link BottomSheetDialog} for choosing image attachment options
     * --Camera
     * --Gallery
     */
    private void showAttachImageDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.layout_attach_image, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
        LinearLayout camera = sheetView.findViewById(R.id.lai_choose_camera);
        LinearLayout gallery = sheetView.findViewById(R.id.lai_choose_gallery);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(mActivity.REQUEST_CAMERA_PERMISSION);
                mBottomSheetDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(mActivity.REQUEST_GALLERY_PERMISSION);
                mBottomSheetDialog.dismiss();
            }
        });
    }

    /**
     * Ask user's approval for Dangerous permissions
     */
    private void askPermission(int requestCode) {
        if (mActivity.mCameraPermission == PackageManager.PERMISSION_GRANTED
                && mActivity.mExtWStoragePermission == PackageManager.PERMISSION_GRANTED
                && mActivity.mExtRStoragePermission == PackageManager.PERMISSION_GRANTED) {


            if (requestCode == mActivity.REQUEST_CAMERA_PERMISSION) {
                mActivity.onCameraOptionSelected();
            } else if (requestCode == mActivity.REQUEST_GALLERY_PERMISSION) {
                mActivity.onGalleryOptionSelected();
            }
        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    requestCode);
        }
    }

    /**
     * Load the image to fragment's image view for preview and
     * set the Uri to mUri member variable, so that it can be used while
     * uploading the image bitmap file to Firebase cloud storage
     *
     * @param uri
     */
    public void bindImage(String uri) {
        if (mPreviewImageLayout.getVisibility() == View.INVISIBLE) {
            mPreviewImageLayout.setVisibility(View.VISIBLE);
        }
        //mUri = uri;
        Picasso.with(mActivity.getApplicationContext())
                .load(Uri.parse(uri))
                .placeholder(mActivity.getResources().getDrawable(R.drawable.ic_placeholder_image))
                .into(mAttachedImage);

        mAttachedImage.setDrawingCacheEnabled(true);
        mAttachedImage.buildDrawingCache();
    }
}
