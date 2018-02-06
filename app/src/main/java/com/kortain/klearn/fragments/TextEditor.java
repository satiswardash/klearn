package com.kortain.klearn.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kortain.klearn.CreateNewFeedActivity;
import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.NetworkUtility;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by satiswardash on 01/02/18.
 */

public class TextEditor extends Fragment {

    private static final String TAG = TextEditor.class.toString();
    private CreateNewFeedActivity mActivity;
    private FloatingActionButton mTextEditorFab;
    private ImageView mCloseTextEditorView;
    private EditText mEditor;
    private CircleImageView mImageView;
    private ProgressBar mProgressBar;

    public TextEditor() {
    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewFeedActivity) {
            mActivity = (CreateNewFeedActivity) context;
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_editor, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mTextEditorFab = view.findViewById(R.id.text_editor_done_fab);
        mCloseTextEditorView = view.findViewById(R.id.text_editor_close);
        mImageView = view.findViewById(R.id.text_editor_user_image);
        mEditor = view.findViewById(R.id.text_editor_editText);
        mProgressBar = view.findViewById(R.id.text_editor_progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mCloseTextEditorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        });

        switch (mActivity.selectedFeedType) {

            case "Regular Article": {
                mEditor.setHint("Share your thoughts here...");
                mTextEditorFab.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_send_black));
                break;
            }

            case "Image Article": {
                mEditor.setHint("Share your thoughts here...");
                mTextEditorFab.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_send_black));
                mTextEditorFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.feedDescription = mEditor.getText().toString();
                        postImageFeedToFirebase();
                    }
                });
                break;
            }

            case "Web Article": {
                mEditor.setHint("Share your thoughts here...");
                mTextEditorFab.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_send_black));
                break;
            }

            case "Objective": {
                mEditor.setHint("Type your question here...");
                mTextEditorFab.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_arrow_forward_black));
                break;
            }
        }

        loadUserImage();
    }

    /**
     * Post Image feed to firestore db
     */
    private void postImageFeedToFirebase() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextEditorFab.setEnabled(false);
        uploadBitmapImageToFirebase(mActivity.imageURI);
        /*if (NetworkUtility.hasNetworkAccess(getContext())) {
        } else {
            Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.text_editor_root), "You are offline, make sure you have active network connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/
    }

    /**
     * Load image drawable icon to fab
     */
    private void loadUserImage() {
        //TODO To be fetched from Firestore user document using userId
        //mImageView.setImageResource(R.mipmap.branding);
        mImageView.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_branding_logo));
    }

    /**
     * Upload image to firebase
     */
    private void uploadBitmapImageToFirebase(Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance(Constants.FEED_IMAGES_BUCKET);
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "<file_name>.jpg"
        StorageReference imageRef = storageRef.child("feed_images/" + UUID.randomUUID().toString() + ".jpg");

        // Get the bitmap data using ContentResolver as bytes
        Bitmap bitmap = null;
        try {
            InputStream ims = getContext().getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "uploadBitmapImageToFirebase: ", e.getCause());
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Compress the image before uploading for better performance
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);

        mProgressBar.setVisibility(View.VISIBLE);
        mTextEditorFab.setEnabled(true);
        //Redirect to main activity
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivity(intent);

        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mActivity.imageURI = taskSnapshot.getDownloadUrl();
                        //TODO
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection(Constants.COLLECTION_FEEDS)
                                .document()
                                .set(createImageFeed(), SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //TODO
                                    }
                                });
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
     * Create Image Feed Map<String, Object> object
     *
     * @return
     */
    private Map<String, Object> createImageFeed() {
        Map<String, Object> feed = new HashMap<>();
        List<String> images = new ArrayList<String>();
        images.add(mActivity.imageURI.toString());

        feed.put(Constants.FEED_ANALYTICS, 0);
        feed.put(Constants.FEED_CATEGORY, mActivity.feedCategory);
        feed.put(Constants.FEED_TIMESTAMP, new Date());
        feed.put(Constants.FEED_DESCRIPTION, mActivity.feedDescription);
        feed.put(Constants.FEED_IMAGES, images);
        feed.put(Constants.FEED_LIKES, 0);
        feed.put(Constants.FEED_TITLE, mActivity.feedTitle);
        feed.put(Constants.FEED_TYPE, mActivity.selectedFeedType);
        feed.put(Constants.FEED_OWNER, FirebaseAuth.getInstance().getCurrentUser().getUid());

        return feed;
    }
}
