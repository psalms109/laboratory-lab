package lopez.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String username, password;
    EditText etUsername, etPassword;
    int formSuccess = 0;
    SharedPreferences sharedPref;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
        sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login_button);
        TextView registerTextView = findViewById(R.id.register_textView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                formSuccess = 2;

                if (username.equals("")) {
                    etUsername.setError("This field is required");
                    formSuccess--;
                }

                // validate password
                if (password.equals("")) {
                    etPassword.setError("This field is required");
                    formSuccess--;
                }

                // form successfully validated
                if (formSuccess == 2) {
                    HashMap<String, String> map_user = new HashMap();
                    map_user.put(db.TBL_USER_USERNAME, username);
                    map_user.put(db.TBL_USER_PASSWORD, password);
                    int userID = db.checkUser(map_user);
                    if (userID > 0) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(db.TBL_USER_ID, userID).commit();
                        startActivity(new Intent(getApplicationContext(), DisplayUsersActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected  void onResume() {

        SharedPreferences sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE);
        if(sharedPref.contains(db.TBL_USER_ID)) {
            this.finish();
            startActivity(new Intent(this, DisplayUsersActivity.class));
        }
        super.onResume();
    }
}
