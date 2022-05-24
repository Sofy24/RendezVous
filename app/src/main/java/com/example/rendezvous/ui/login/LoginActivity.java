package com.example.rendezvous.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rendezvous.DB.Circle;
import com.example.rendezvous.DB.Converters;
import com.example.rendezvous.DB.Info;
import com.example.rendezvous.DB.RendezVous;
import com.example.rendezvous.DB.RendezVousDB;
import com.example.rendezvous.DB.User;
import com.example.rendezvous.R;
import com.example.rendezvous.HomeActivity;
import com.example.rendezvous.databinding.ActivityLoginBinding;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

//
//        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(materialToolbar);
//
//        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dLayout.openDrawer(Gravity.LEFT);
//            }
//        });
//
//        setNavigationDrawer();
        /*RendezVousDB db = RendezVousDB.getInstance(this);
        db.databaseDAO().insertUser(123123, "cacca", "popo");*/

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final ImageButton loginButton = findViewById(R.id.login);
        //final ImageButton loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                // Here the intent is passed on and next activity is homecalendar
                Intent openHome = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(openHome);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
        String welcome = getString(R.string.welcome) + binding.username.getText().toString();


        RendezVousDB db = RendezVousDB.getInstance(LoginActivity.this.getBaseContext());

        if(binding.username.getText().toString().equals("insertData") ){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    User mega = new User("Matteo", "Santoro", "Mega", "00000", null);
                    User sofy = new User("Sofia", "Tosi", "Sofy24", "123456", null);
                    User luis = new User("Luis", "Mi chiamo", "Lu1g1", "Ciao_sono_Luis", null);
                    User michi = new User("Michi", "Ferdinardo", "Clown", "Mi_piace_la_carne", null);
                    User klevis = new User("Klevis", "Duka", "Duca", "Veloce_come_uno_squalo", null);
                    User fede = new User("Federico", "Cobianchi", "F3d3", "passw0rd", null);
                    db.databaseDAO().insertUser(mega, sofy, luis, michi, klevis);
                    Circle progetto = new Circle("Gruppo progetto", "Rosso");
                    Circle gym = new Circle("Gym", "Black");
                    Circle uni = new Circle("University", "Pink");
                    db.databaseDAO().insertCircle(progetto, gym, uni);

                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(sofy.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(luis.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(michi.getUserName()));

                    db.databaseDAO().insertCircleOfFriends(progetto.getC_name(), db.databaseDAO().getUID(michi.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(progetto.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(progetto.getC_name(), db.databaseDAO().getUID(luis.getUserName()));

                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(klevis.getUserName()));

//                    RendezVous rendezVous = new RendezVous(coraggiosi.getC_name(), Converters.dateToTimestamp(new Date()), Converters.dateToTimestamp(new Date()), 2);
//                    db.databaseDAO().insertRendezvous(rendezVous);
//                    RendezVous rendezVous2 = new RendezVous(coraggiosi.getC_name(), Converters.dateToTimestamp(new Date()), Converters.dateToTimestamp(new Date()), 24);
//                    db.databaseDAO().insertRendezvous(rendezVous2);
//                    Circle cc = new Circle("cc", "Pink");
//                    db.databaseDAO().insertCircle(cc);
//                    db.databaseDAO().insertRendezvous(new RendezVous("cc", 1000,1000, 45));

                }
            });
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                User logged = new User(null, null, binding.username.getText().toString(),
                        binding.password.getText().toString(), null);
                db.databaseDAO().insertUser(logged);
                db.databaseDAO().setUserActive(db.databaseDAO().getUID(logged.getUserName()));

            }
        });
            // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}