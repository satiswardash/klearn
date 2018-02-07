package com.kortain.klearn;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.CreateFeedUtility;
import com.kortain.klearn.Utility.FileUtility;
import com.kortain.klearn.Utility.ProgressLoaderUtility;
import com.kortain.klearn.fragments.AttachFeedImages;
import com.kortain.klearn.fragments.ChooseFeedType;
import com.kortain.klearn.fragments.InputOptions;
import com.kortain.klearn.fragments.MessageDialog;
import com.kortain.klearn.fragments.InputArticleFeedUrl;
import com.kortain.klearn.fragments.TextEditor;
import com.kortain.klearn.fragments.TextInputDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateNewFeedActivity extends AppCompatActivity
        implements MessageDialog.CreateFeedDialogListeners, TextInputDialog.TextInputDialogListener {

    private static final String TAG = CreateNewFeedActivity.class.toString();
    public final String[] FEED_TYPES = {"Regular Feed", "Image Feed", "Web Article", "Objective Question"};

    public final int IMAGE_ATTACHMENT_INTENT = 1;
    public final int REQUEST_CAMERA_PERMISSION = 11;
    public final int REQUEST_GALLERY_PERMISSION = 22;

    public int mCameraPermission;
    public int mExtRStoragePermission;
    public int mExtWStoragePermission;

    //Feed data members
    public String selectedFeedType = Constants.FEED_CATEGORY_REGULAR;
    public String feedCategory = "";
    public String feedTitle = "";
    public String feedDescription = "";
    public String question = "";
    public int answer = -1;
    public List<String> options = new ArrayList<>();
    public String information = "";
    public Uri imageURI;
    public String articleUrl;

    public ProgressLoaderUtility mProgressLoaderUtility;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_feed);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.anf_frame_layout, new ChooseFeedType(), ChooseFeedType.class.toString())
                .commit();

        mProgressLoaderUtility = ProgressLoaderUtility.getInstance(R.id.anf_frame_layout, getSupportFragmentManager());
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        mCameraPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        mExtRStoragePermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        mExtWStoragePermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     *
     */
    public void onCameraOptionSelected() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = new FileUtility().createExternalImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "onCameraOptionSelected: ", ex.getCause());
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, IMAGE_ATTACHMENT_INTENT);
            }
        }
    }

    /**
     *
     */
    public void onGalleryOptionSelected() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        //galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), IMAGE_ATTACHMENT_INTENT);
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CAMERA_PERMISSION: {
                onCameraOptionSelected();
                break;
            }
            case REQUEST_GALLERY_PERMISSION: {
                onGalleryOptionSelected();
                break;
            }
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case IMAGE_ATTACHMENT_INTENT: {
                AttachFeedImages fragment = (AttachFeedImages) getSupportFragmentManager().findFragmentByTag(AttachFeedImages.class.toString());
                if (fragment != null) {
                    if (data != null && data.getData() != null) {
                        imageURI = data.getData();
                    }
                    fragment.bindImage(imageURI.toString());
                }
                break;
            }
        }
    }

    /**
     *
     */
    @Override
    public void onPositiveClickHandler() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.get(fragments.size() - 2) instanceof InputArticleFeedUrl) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.anf_frame_layout, new TextEditor(), TextEditor.class.toString())
                    .addToBackStack(TextEditor.class.toString())
                    .commit();
            return;
        }
        if (fragments.get(fragments.size() - 2) instanceof InputOptions) {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setCancelable(true);
            inputDialog.show(getSupportFragmentManager(), TextInputDialog.class.toString());
            return;
        }
    }

    /**
     *
     */
    @Override
    public void onNegativeClickHandler() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments.get(fragments.size() - 2) instanceof InputArticleFeedUrl) {
            Map<String, Object> webFeed = null;
            webFeed = CreateFeedUtility.getInstance()
                    .createNewFeed(
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),   //UserId
                            selectedFeedType,                                       //Feed type
                            feedCategory,                                           //Feed category
                            feedTitle,                                              //Feed title
                            null,
                            0,
                            0,
                            null,
                            articleUrl,                                            //Feed web url
                            null,
                            null,
                            0);
            publishFeed(webFeed);
        }
        if (fragments.get(fragments.size() - 2) instanceof InputOptions) {
            Map<String, Object> questionFeed = null;
            questionFeed = CreateFeedUtility.getInstance()
                    .createNewFeed(
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),   //UserId
                            selectedFeedType,                                       //Feed type
                            feedCategory,                                           //Feed category
                            question,                                              //Feed question
                            null,
                            0,
                            0,
                            null,
                            null,
                            options,                                                //Feed options
                            null,                                        //Feed extra information
                            answer);                                                //Feed answer index
            publishFeed(questionFeed);
        }
    }

    /**
     *
     */
    @Override
    public void submit(String text) {
        information = text;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments.get(fragments.size() - 2) instanceof InputOptions) {
            Map<String, Object> questionFeed = null;
            questionFeed = CreateFeedUtility.getInstance()
                    .createNewFeed(
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),   //UserId
                            selectedFeedType,                                       //Feed type
                            feedCategory,                                           //Feed category
                            question,                                              //Feed question
                            null,
                            0,
                            0,
                            null,
                            null,
                            options,                                                //Feed options
                            information,                                            //Feed extra information
                            answer);                                                //Feed answer index
            publishFeed(questionFeed);
        }
    }

    /**
     *
     * @param feed
     */
    private void publishFeed(Map<String, Object> feed) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_FEEDS)
                .document()
                .set(feed, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Navigate back to Main Activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO
                    }
                });
    }
}
