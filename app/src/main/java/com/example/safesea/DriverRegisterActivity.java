package com.example.safesea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverRegisterActivity extends AppCompatActivity {

    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(DriverRegisterActivity.this , R.color.textDescription));

        mFullName        = findViewById(R.id.fullName);
        mEmail           = findViewById(R.id.Email);
        mPassword        = findViewById(R.id.password);
        mPhone           = findViewById(R.id.phone);
        mRegisterBtn     = findViewById(R.id.loginBtn);
        mLoginBtn        = findViewById(R.id.createText);

        fAuth            = FirebaseAuth.getInstance();
        progressBar      = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), DriverMapActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("L'e-mail est requis");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Mot de passe requis");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Le mot de passe doit être >= 6 caractères");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DriverRegisterActivity.this, "Compte crée", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DriverMapActivity.class));
                            String user_id = fAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Utilisateurs").child("Garde nationale").child(user_id);
                            current_user_db.setValue(true);
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(DriverRegisterActivity.this, "Erreur !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DriverLoginActivity.class));
            }
        });
    }
}