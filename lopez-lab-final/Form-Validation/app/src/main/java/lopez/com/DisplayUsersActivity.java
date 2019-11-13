package lopez.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayUsersActivity extends AppCompatActivity {

    ListView lvUsers;
    DbHelper db;

    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> all_users;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        db = new DbHelper(this);
        sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE);
        lvUsers = findViewById(R.id.lv_users);
        fetch_users();
    }

    private void fetch_users() {
        all_users = db.getAllUsers();

        adapter = new ListViewAdapter(getApplicationContext(), R.layout.adapter_users, all_users);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);
    }

    private class ListViewAdapter extends ArrayAdapter {
        LayoutInflater inflater;
        TextView tvName, tvUserName;
        ImageButton btnEdit, btnDelete;

        ArrayList<HashMap<String, String>> objects;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, objects);

            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.adapter_users, parent, false);

            tvName = convertView.findViewById(R.id.tvfullname);
            tvUserName = convertView.findViewById(R.id.tvusername);
            btnEdit = convertView.findViewById(R.id.ivedit);
            btnDelete = convertView.findViewById(R.id.ivdelete);

            tvName.setText(objects.get(position).get(db.TBL_USER_FULLNAME));
            tvUserName.setText(objects.get(position).get(db.TBL_USER_USERNAME));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));
                    db.deleteUser(userID);
                    Toast.makeText(DisplayUsersActivity.this, "User successfully deleted!", Toast.LENGTH_SHORT).show();
                    fetch_users();
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));

                    Intent intent = new Intent(getContext(), EditUserActivity.class);
                    intent.putExtra(db.TBL_USER_ID, userID);
                    startActivity(intent);
                }
            });
            return  convertView;
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnLogout:

                new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPref.edit().remove(db.TBL_USER_ID).commit();
                        Toast.makeText(DisplayUsersActivity.this, "Sucessfully logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        DisplayUsersActivity.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onResume() {

        fetch_users();
        if(!sharedPref.contains(db.TBL_USER_ID)) {
            this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onResume();
    }
}
