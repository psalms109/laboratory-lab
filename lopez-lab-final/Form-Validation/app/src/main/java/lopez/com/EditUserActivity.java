package lopez.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etName;
    String username, password, name;
    int formSuccess, userID;
    DbHelper db;
    ArrayList<HashMap<String, String>> selected_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new DbHelper(this);

        etUsername = findViewById(R.id.ET_username);
        etPassword = findViewById(R.id.ET_password);
        etName = findViewById(R.id.ET_fullname);

        Intent intent = getIntent();
        userID = intent.getIntExtra(db.TBL_USER_ID, 0);
        selected_user = db.getSelectedUserData(userID);

        etUsername.setText(selected_user.get(0).get(db.TBL_USER_USERNAME));
        etPassword.setText(selected_user.get(0).get(db.TBL_USER_PASSWORD));
        etName.setText((selected_user.get(0).get(db.TBL_USER_FULLNAME)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnSave:

                formSuccess = 3;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                name = etName.getText().toString();

                if(username.equals("")) {
                    etUsername.setError("This field is required");
                    formSuccess--;
                }
                if(password.equals("")) {
                    etPassword.setError("This field is required");
                    formSuccess--;
                }
                if(name.equals("")) {
                    etName.setError("This field is required");
                    formSuccess--;
                }
                if(formSuccess == 3) {
                    HashMap<String, String> map_user = new HashMap();
                    map_user.put(db.TBL_USER_ID, String.valueOf(userID));
                    map_user.put(db.TBL_USER_USERNAME, username);
                    map_user.put(db.TBL_USER_PASSWORD, password);
                    map_user.put(db.TBL_USER_FULLNAME, name);
                    db.updateUser(map_user);
                    Toast.makeText(this, "Data successfully updated!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }

                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
