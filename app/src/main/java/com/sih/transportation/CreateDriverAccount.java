package com.sih.transportation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDriverAccount extends AppCompatActivity {

    public void signUp(String email, String password) {

        String role = "driver";

        MainActivity.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // Sign up success
                        Toast.makeText(CreateDriverAccount.this, "Account created.", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = MainActivity.mAuth.getCurrentUser();

                        // Create a reference to the user's data in the Firebase Realtime Database
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                        // Create a User object that includes email and role
                        User newUser = new User(email, role);

                        // Store the new user's data in the database
                        userRef.setValue(newUser).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("Firebase", "User data added successfully.");
                            } else {
                                Log.e("Firebase", "Failed to add user data.", task1.getException());
                            }
                        });

                        // Navigate to the appropriate dashboard based on role
                        Intent intent;
                        if ("driver".equals(role)) {
                            intent = new Intent(CreateDriverAccount.this, DriverDashboard.class);
                        } else {
                            intent = new Intent(CreateDriverAccount.this, EmployeeDashboard.class);
                        }

                        startActivity(intent);
                        // Handle the signed-in user
                        finish();

                    } else {
                        // Sign up failure
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(CreateDriverAccount.this, "Weak password.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(CreateDriverAccount.this, "Invalid email.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(CreateDriverAccount.this, "Account already exists.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(CreateDriverAccount.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText email_field = findViewById(R.id.email);
        EditText password_field = findViewById(R.id.password);
        EditText re_enter_password_field = findViewById(R.id.re_enter_password);
        Button create_driver_account = findViewById(R.id.create_driver_account);

        create_driver_account.setOnClickListener(v -> {

            String email = email_field.getText().toString();
            String password = password_field.getText().toString();
            String re_enter_password = re_enter_password_field.getText().toString();

            if(!password.equals(re_enter_password))
                Toast.makeText(CreateDriverAccount.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            else if(email.isEmpty() || password.isEmpty() || re_enter_password.isEmpty())
                Toast.makeText(this, "Email or password cant be empty", Toast.LENGTH_SHORT).show();
            else
                signUp(email, password);

            signUp("user@example.com", "password123");

        });


    }
}