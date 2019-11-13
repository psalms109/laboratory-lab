package lopez.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TBL_USERS = "users",
        TBL_USER_ID = "id",
        TBL_USER_USERNAME = "username",
        TBL_USER_PASSWORD = "password",
        TBL_USER_FULLNAME = "fullname";
    SQLiteDatabase dbReadable = getReadableDatabase();
    SQLiteDatabase dbWritable = getWritableDatabase();

    public DbHelper(Context context) {
        super(context,"STIDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_create_users_table = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_USERS, TBL_USER_ID, TBL_USER_USERNAME, TBL_USER_PASSWORD, TBL_USER_FULLNAME);
        db.execSQL(sql_create_users_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int createUser(HashMap<String, String> map_user) {
        int userid = 0;

        String sql_check_user = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TBL_USERS, TBL_USER_USERNAME, map_user.get(TBL_USER_USERNAME));

        Cursor cursor = dbReadable.rawQuery(sql_check_user, null);
        if(cursor.moveToNext()) {
            userid = cursor.getInt(cursor.getColumnIndex(TBL_USER_ID));
        } else {
            ContentValues val = new ContentValues();
            val.put(TBL_USER_USERNAME, map_user.get(TBL_USER_USERNAME));
            val.put(TBL_USER_PASSWORD, map_user.get(TBL_USER_PASSWORD));
            val.put(TBL_USER_FULLNAME, map_user.get(TBL_USER_FULLNAME));
            dbWritable.insert(TBL_USERS, null, val);
        }

        return userid;
    }

    public int checkUser(HashMap<String, String> map_user) {
        String hisUsername = map_user.get(TBL_USER_USERNAME);
        String hisPassword = map_user.get(TBL_USER_PASSWORD);
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s' and %s = '%s'",
                TBL_USERS, TBL_USER_USERNAME, hisUsername, TBL_USER_PASSWORD, hisPassword);
        Cursor cur = dbReadable.rawQuery(sql, null);
        int userid = 0;

        if (cur.moveToNext()) {
            userid = cur.getInt(cur.getColumnIndex(TBL_USER_ID));
        }
        return userid;
    }

    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> all_users = new ArrayList();
        String sql_get_all_users = String.format("SELECT * FROM %s ORDER BY %s ASC", TBL_USERS, TBL_USER_FULLNAME);
        Cursor cur = dbReadable.rawQuery(sql_get_all_users, null);

        while (cur.moveToNext()){
            HashMap<String,String> map_user = new HashMap();
            map_user.put(TBL_USER_ID, cur.getString(cur.getColumnIndex(TBL_USER_ID)));
            map_user.put(TBL_USER_FULLNAME, cur.getString(cur.getColumnIndex(TBL_USER_FULLNAME)));
            map_user.put(TBL_USER_USERNAME, cur.getString(cur.getColumnIndex(TBL_USER_USERNAME)));
            all_users.add(map_user);
        }
        cur.close();
        return all_users;
    }

    public ArrayList<HashMap<String, String>> getSelectedUserData(int userid) {
        String sql_SelectedUser = String.format("SELECT * FROM %s WHERE %s = %s", TBL_USERS, TBL_USER_ID, userid);

        Cursor cur = dbReadable.rawQuery(sql_SelectedUser, null);
        ArrayList<HashMap<String, String>> selectedUser = new ArrayList();

        while (cur.moveToNext()){
            HashMap<String, String> map_user = new HashMap();
            map_user.put(TBL_USER_USERNAME, cur.getString(cur.getColumnIndex(TBL_USER_USERNAME)));
            map_user.put(TBL_USER_PASSWORD, cur.getString(cur.getColumnIndex(TBL_USER_PASSWORD)));
            map_user.put(TBL_USER_FULLNAME, cur.getString(cur.getColumnIndex(TBL_USER_FULLNAME)));
            selectedUser.add(map_user);
        }
        cur.close();
        return selectedUser;
    }

    public void deleteUser(int userID) {
        dbWritable.delete(TBL_USERS, TBL_USER_ID + "=" + userID, null);
    }

    public void updateUser(HashMap<String, String> map_user) {
        ContentValues val = new ContentValues();
        val.put(TBL_USER_USERNAME, map_user.get(TBL_USER_USERNAME));
        val.put(TBL_USER_PASSWORD, map_user.get(TBL_USER_PASSWORD));
        val.put(TBL_USER_FULLNAME, map_user.get(TBL_USER_FULLNAME));
        dbWritable.update(TBL_USERS, val, TBL_USER_ID + "=" + map_user.get(TBL_USER_ID), null);
    }
}
