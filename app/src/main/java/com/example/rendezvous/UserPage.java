package com.example.rendezvous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.CircleOfFriends;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPage extends AppCompatActivity {
    private User activeUser;
    private List<Circle> circleList;
    private  TextInputEditText name ;
    private TextInputEditText surname;
    private final List<Circle> selectedCircles = new ArrayList<>();
    private List<String> alreadyMember;
    private final List<CheckBox> checkBoxList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page_layout);
        name  = (TextInputEditText) findViewById(R.id.name_active_user_edit);
        surname = (TextInputEditText) findViewById(R.id.surname_active_user_edit);

        RendezVousDB db = RendezVousDB.getInstance(UserPage.this.getBaseContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                activeUser = db.databaseDAO().getActiveUser();
                TextView username = (TextView) findViewById(R.id.usernamePersonalPage);
                username.setText(activeUser.getUserName());
                if(activeUser.getURIavatar()!= null){
                    Uri imageUri = Uri.parse(activeUser.getURIavatar().toString());
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageView imageView = (ImageView)findViewById(R.id.avatar_image);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                // Animators may only be run on Looper threads problem resolved
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                if(activeUser.getUserName() != null){
                    name.setText(activeUser.getNome());
                }
                if(activeUser.getCognome() != null){
                    surname.setText(activeUser.getCognome());
                }

                    }
                });
                circleList = db.databaseDAO().getCircles();
                alreadyMember = db.databaseDAO().getUserCircles(db.databaseDAO().getUID(activeUser.getUserName()));
                ScrollView scrollView = findViewById(R.id.circle_checkbox);
                LinearLayout layout = (LinearLayout) scrollView.getChildAt(0);

                for (Circle c: circleList) {
                    CheckBox box = new CheckBox(UserPage.this.getBaseContext());
                    box.setText(c.getC_name());
                    box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                selectedCircles.add(c);
                            } else {
                                selectedCircles.remove(c);
                            }
                        }
                    });
                    if(alreadyMember.contains(c.getC_name())){
                        box.setChecked(true); //non testato
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            layout.addView(box);
                            checkBoxList.add(box);
                        }
                    });
                }
            }
        });




        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done_insert_info);

        done.setOnClickListener(view -> {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    activeUser = db.databaseDAO().getActiveUser();
                    db.databaseDAO().updateUser(db.databaseDAO().getUID(activeUser.getUserName()), name.getText().toString(), surname.getText().toString());
                    for (Circle circleToEnter: selectedCircles
                         ) {
                    db.databaseDAO().insertCircleOfFriends(new CircleOfFriends(circleToEnter.getC_name(), db.databaseDAO().getUID(activeUser.getUserName())));
                    }
                    for (CheckBox box: checkBoxList
                         ) {
                       if(!box.isChecked()){
                            db.databaseDAO().removeFromCircle(db.databaseDAO().getUID(activeUser.getUserName()), box.getText().toString());
                       }
                    }
                }
            });
            this.finish();
        });

        FloatingActionButton photoUpload = (FloatingActionButton) findViewById(R.id.add_photo_button);

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();



//                            System.out.println("NDO mi chiamano?");

                            try {
                                final Uri imageUri = data.getData();
                                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                //reverse Uri.parse(stringUri);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.databaseDAO().updateUserAvatar(imageUri.toString(), db.databaseDAO().getActiveUser().getUserName());
                                    }
                                });
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                ImageView imageView = (ImageView)findViewById(R.id.avatar_image);
                                imageView.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(UserPage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(UserPage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        photoUpload.setOnClickListener(view -> {
//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//
//            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

//            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

//           photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);

            photoPickerIntent.setType("image/*");
//            Intent intent = new Intent(this.getBaseContext(), UserPage.class);
            someActivityResultLauncher.launch(photoPickerIntent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}
