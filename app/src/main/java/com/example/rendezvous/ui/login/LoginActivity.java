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
import com.example.rendezvous.DB.Invited;
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
    private Activity login;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = this;
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


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

        Button register = (Button) findViewById(R.id.login__register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login, RegisterActivity.class));
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

                    User joe = new User("Mama", "Joe", "joeMama", "IdOnTKnOw", null);

                    db.databaseDAO().insertUser(mega, sofy, luis, michi, klevis, joe, fede);
                    Circle progetto = new Circle("Gruppo progetto", "#eb4255");
                    Circle gym = new Circle("Gym", "#000000");
                    Circle uni = new Circle("University", "#3d85c6");
                    db.databaseDAO().insertCircle(progetto, gym, uni);

                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(sofy.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(luis.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(michi.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(uni.getC_name(), db.databaseDAO().getUID(joe.getUserName()));

                    db.databaseDAO().insertCircleOfFriends(progetto.getC_name(), db.databaseDAO().getUID(sofy.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(progetto.getC_name(), db.databaseDAO().getUID(mega.getUserName()));

                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(mega.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(klevis.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(fede.getUserName()));
                    db.databaseDAO().insertCircleOfFriends(gym.getC_name(), db.databaseDAO().getUID(joe.getUserName()));


                    //OK l'ide e' di fare un uscita con i miei amici di palestra e la sofia

                    //al momento c'e' una foto di michi
                    db.databaseDAO().insertInfo(new Info("Bowling night", "After a pizza to alfredo's we ll challenge in Bowling", "content://com.android.providers.media.documents/document/image%3A25702", 0.0, 0.0));
                    db.databaseDAO().insertRendezvous(new RendezVous(gym.getC_name(), 1657152000000L, 1657411200000L, 1, 1));
                    // fino a qui va :^ )
                    db.databaseDAO().insertRendezvous(new RendezVous(progetto.getC_name(), 1657152000000L, 1657411200000L, 1, 1));
                    db.databaseDAO().insertInvited(new Invited(1, 1, "Received"));
                    db.databaseDAO().insertInvited(new Invited(1, 2, "Received"));
                    db.databaseDAO().insertInvited(new Invited(1, 5, "Received"));
                    db.databaseDAO().insertInvited(new Invited(1, 6, "Received"));
                    db.databaseDAO().insertInvited(new Invited(1, 7, "Received"));

                    db.databaseDAO().updateInvitedUser(1657152000000L, 1, 2);
                    db.databaseDAO().updateInvitedUser(1657152000000L, 1, 5);
                    db.databaseDAO().updateInvitedUser(1657152000000L, 1, 6);
                    db.databaseDAO().updateInvitedUser(1657152000000L, 1, 7);


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