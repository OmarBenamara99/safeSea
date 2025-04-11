package com.example.safesea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerSettingsActivity extends AppCompatActivity  {

    private Button mBack,mConfirm;
    private EditText mNmbrField,mProbField,mTimeeField;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userID;

    private String mNmbr;
    private String mProb;
    private String mTimee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);

        mNmbrField                   = (EditText)  findViewById(R.id.Nombre_de_personnes);
        mProbField                   = (EditText)  findViewById(R.id.Type_de_problème);
        mTimeeField                  = (EditText)  findViewById(R.id.Durée);
        mBack                        = (Button)        findViewById(R.id.back);
        mConfirm                     = (Button)        findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Utilisateurs").child("Utilisateur standard").child(userID);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo(){
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists() && snapshot.getChildrenCount()>0){
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                if (map.get("Nombre_de_personnes") != null){
                    mNmbr = map.get("Nombre_de_personnes").toString();
                    mNmbrField.setText(mNmbr);
                }
                if (map.get("Type_de_problème") != null){
                    mProb = map.get("Type_de_problème").toString();
                    mProbField.setText(mProb);
                }
                if (map.get("Durée") != null){
                    mTimee = map.get("Durée").toString();
                    mTimeeField.setText(mTimee);
                }
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveUserInformation (){
    mNmbr        = mNmbrField.getText().toString();
    mTimee       = mTimeeField.getText().toString();
    mProb        = mProbField.getText().toString();

    Map userInfo = new HashMap();
    userInfo.put("Nombre_de_personnes" , mNmbr);
    userInfo.put("Durée" , mTimee);
    userInfo.put("Type_de_problème" , mProb);
    mCustomerDatabase.updateChildren(userInfo);
    finish();
    }
}