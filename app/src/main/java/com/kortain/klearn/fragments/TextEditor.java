package com.kortain.klearn.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;
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
import com.kortain.klearn.Utility.CreateFeedUtility;
import com.kortain.klearn.Utility.ProgressLoaderUtility;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                mTextEditorFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.feedDescription = mEditor.getText().toString();
                        postRegularFeedToFirebase();
                    }
                });
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
                mTextEditorFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.feedDescription = mEditor.getText().toString();
                        postWebFeedToFirebase();
                    }
                });
                break;
            }

            case "Objective": {
                mEditor.setHint("Type your question here...");
                mTextEditorFab.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_arrow_forward_black));
                mTextEditorFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.question = mEditor.getText().toString();
                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.anf_frame_layout, new InputOptions(), InputOptions.class.toString())
                                .addToBackStack(InputOptions.class.toString())
                                .commit();
                    }
                });
                break;
            }
        }

        loadUserImage();
    }

    /**
     * Post Web feed to firestore db
     */
    private void postWebFeedToFirebase() {

        Map<String, Object> regularFeed = null;
        regularFeed = CreateFeedUtility.getInstance()
                .createNewFeed(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(), //UserId
                        mActivity.selectedFeedType,                           //Feed type
                        mActivity.feedCategory,                               //Feed category
                        mActivity.feedTitle,                                  //Feed title
                        mActivity.feedDescription,                            //Feed description
                        0,
                        0,
                        null,
                        mActivity.articleUrl,                                 //Feed web url
                        null,
                        null,
                        0);
        updateFeedToFirestore(regularFeed);
    }

    /**
     * Post Regular feed to firestore db
     */
    private void postRegularFeedToFirebase() {

        Map<String, Object> regularFeed = null;
        regularFeed = CreateFeedUtility.getInstance()
                .createNewFeed(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(), //UserId
                        mActivity.selectedFeedType,                           //Feed type
                        mActivity.feedCategory,                               //Feed category
                        mActivity.feedTitle,                                  //Feed title
                        mActivity.feedDescription,                            //Feed description
                        0,
                        0,
                        null,
                        null,
                        null,
                        null,
                        0);
        updateFeedToFirestore(regularFeed);
    }

    /**
     * Post Image feed to firestore db
     */
    private void postImageFeedToFirebase() {
        //setProgressBar(View.VISIBLE);
        mActivity.mProgressLoaderUtility.setLoadingScreen(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance(Constants.FEED_IMAGES_BUCKET);
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to "<file_name>.jpg"
        StorageReference imageRef = storageRef.child("feed_images/" + UUID.randomUUID().toString() + ".jpg");

        // Get the bitmap data using ContentResolver as bytes
        Bitmap bitmap = null;
        try {
            InputStream ims = getContext().getContentResolver().openInputStream(mActivity.imageURI);
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
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        mActivity.imageURI = taskSnapshot.getDownloadUrl();
                        List<String> images = new ArrayList<>();
                        images.add(mActivity.imageURI.toString());

                        Map<String, Object> imageFeed = null;
                        imageFeed = CreateFeedUtility.getInstance()
                                .createNewFeed(
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(), //UserId
                                        mActivity.selectedFeedType,                           //Feed type
                                        mActivity.feedCategory,                               //Feed category
                                        mActivity.feedTitle,                                  //Feed title
                                        mActivity.feedDescription,                            //Feed description
                                        0,
                                        0,
                                        images,                                               //Feed images
                                        null,
                                        null,
                                        null,
                                        0);
                        updateFeedToFirestore(imageFeed);
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
     * @param feed
     */
    private void updateFeedToFirestore(Map<String, Object> feed) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_FEEDS)
                .document()
                .set(feed, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*if (mProgressBar.getVisibility() == View.VISIBLE)
                            setProgressBar(View.INVISIBLE);*/
                        redirectToMainActivity();
                        mActivity.mProgressLoaderUtility.setLoadingScreen(View.INVISIBLE);
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
     * @param visibility
     */
    private void setProgressBar(int visibility) {
        mProgressBar.setVisibility(visibility);
        if (visibility == View.VISIBLE)
            mTextEditorFab.setEnabled(false);
        else
            mTextEditorFab.setEnabled(true);

    }

    /**
     *
     */
    private void redirectToMainActivity() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivity(intent);
    }

    /**
     * Load image drawable icon to fab
     */
    private void loadUserImage() {
        //TODO To be fetched from Firestore user document using userId
        //mImageView.setImageResource(R.mipmap.branding);
        mImageView.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_branding_logo));
    }
}
