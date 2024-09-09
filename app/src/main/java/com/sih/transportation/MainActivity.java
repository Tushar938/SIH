package com.sih.transportation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(
                                    MainActivity.this,
                                    DriverDashboard.class);

                            startActivity(intent);
                            finish();
                            // Handle the signed-in user
                        }else
                            // Sign in failure
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        TextView create_account = findViewById(R.id.create_account);
        TextView emp_login = findViewById(R.id.emp_login);
        Button login = findViewById(R.id.login);

        login.setOnClickListener(v -> {

            String email_info = email.getText().toString();
            String pass = password.getText().toString();

            if(email_info.isEmpty() || pass.isEmpty())
                Toast.makeText(this, "Email or password cant be empty", Toast.LENGTH_SHORT).show();
            else
                signIn(email_info, pass);

        });

        create_account.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CreateDriverAccount.class);

            startActivity(intent);

        });

        emp_login.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    EmployeeLogin.class
            );

            startActivity(intent);

        });

    }
}