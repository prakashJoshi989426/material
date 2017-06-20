package com.example.uVite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.uvite.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.TimeZone;

public class MIDatabaseHandler extends SQLiteOpenHelper {

    static MIDatabaseHandler db = null;

    public MIDatabaseHandler(Context context) {
        super(context, context.getString(R.string.app_name), null, 1);
        db = this;
    }

    public static MIDatabaseHandler getDB(Context context) {
        if (db == null) {
            db = new MIDatabaseHandler(context);
        }
        return db;
    }

    public static void deleteData(String TableName, ArrayList<String> arrData,
                                  ArrayList<String> arrVals) {

        String arrDel = "";

        for (int i = 0; i < arrData.size(); i++) {

            if (i == 0) {
                arrDel = arrData.get(i) + " = ? ";
            } else {
                arrDel = arrDel + " AND " + arrData.get(i) + " = ? ";
            }

        }

        System.out.println(TableName+""+db.getWritableDatabase().delete(TableName, arrDel,
                arrVals.toArray(new String[arrVals.size()])));

    }

    public static void editMultipleData(Context context, String tableName,
                                        ArrayList<String> data, ArrayList<String> whereData) {
        try {

            if (data != null && whereData != null) {
                String query = "UPDATE " + tableName + " SET ";

                for (int i = 0; i < data.size(); i++) {
                    if (i == 0) {
                        query = query + " " + data.get(i);
                    } else {
                        query = query + " , " + data.get(i);
                    }
                }

                for (int i = 0; i < whereData.size(); i++) {
                    if (i == 0) {
                        query = query + " WHERE " + whereData.get(i);
                    } else {
                        query = query + whereData.get(i);
                    }
                }



                getDB(context).getWritableDatabase().execSQL(query);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editMultipleData(Context context, String tableName,
                                        Hashtable<String,String> data, HashMap<String,String> whereData) {
        try {

            if (data != null && whereData != null) {
                String query = "UPDATE " + tableName + " SET ";

                int i=0;

                for (String key : data.keySet())
                {
                    if (i == 0) {
                        query = query + " " + key+"='"+data.get(key)+"'";
                    } else {
                        query = query + " , " + key+"='"+data.get(key)+"'";
                    }
                    i++;
                }

                i=0;

                for (String key : whereData.keySet())
                {
                    if (i == 0) {
                        query = query + " WHERE " +key+"="+whereData.get(key);
                    } else {
                        query = query + " AND " +key+"="+whereData.get(key);
                    }
                    i++;
                }

                /*for (int i = 0; i < data.size(); i++) {
                    if (i == 0) {
                        query = query + " " + data.get(i);
                    } else {
                        query = query + " , " + data.get(i);
                    }
                }

                for (int i = 0; i < whereData.size(); i++) {
                    if (i == 0) {
                        query = query + " WHERE " + whereData.get(i);
                    } else {
                        query = query + whereData.get(i);
                    }
                }*/



                getDB(context).getWritableDatabase().execSQL(query);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteMultipleData(String TableName,
                                          ArrayList<String> arrWhere) {

        String query = "DELETE FROM " + TableName, whereCons = "";

        if (arrWhere != null) {

            if (arrWhere.size() > 0) {
                for (int i = 0; i < arrWhere.size(); i++) {
                    whereCons = whereCons + " " + arrWhere.get(i);
                }
            }
        }

        if (!whereCons.equalsIgnoreCase("")) {

            query = query + " where " + whereCons;
            //System.out.println("DEl===" + query);
            db.getWritableDatabase().execSQL(query);

        }

    }

    public static Cursor getTableDataAsCursor(Context context,
                                              String tableName, HashMap<String, String> getColumns,
                                              ArrayList<String> whereData, String orderByColumn,
                                              String orderByType) {

        String query = "SELECT DISTINCT ", params = " * ", whereCondition = "";
        int i = 0;
        if (getColumns != null) {
            if (getColumns.size() > 0) {
                Set<String> keys = getColumns.keySet();
                if (keys.size() > 0) {
                    for (String key : keys) {
                        if (params.length() == 0) {
                            params = key;
                        } else {
                            params = params + "," + key;
                        }

                    }
                } else {

                }
            }
        }

        query = query + " " + params + " FROM " + tableName;

        if (whereData != null) {
            if (whereData.size() > 0) {
                for (int j = 0; j < whereData.size(); j++) {
                    whereCondition = whereCondition + " " + whereData.get(j);
                }
            }
            if (whereData != null) {
                whereCondition = "  where " + whereCondition;
            }
        }

        query = query + whereCondition;

        if (orderByColumn != null && orderByType != null) {
            query = query + " ORDER BY " + orderByColumn + " " + orderByType;
        }

        query = query.trim();

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);

        return cursor;

    }


    public static Cursor getTableDataAsCursor_new(Context context
                                              ) {

        //String Query_new = "SELECT a.* FROM (SELECT * FROM contact_list_from_phone WHERE contact_mail_id NOT IN (SELECT DISTINCT(email) from contactlist_table WHERE email != \"\") GROUP BY contact_mail_id, contact_number) a WHERE a.contact_number NOT IN (SELECT mobile FROM contactlist_table WHERE mobile != \"\") AND NOT (a.contact_mail_id = \"\" AND a.contact_number = \"\")";


        String Query_new = "INSERT INTO `contactlist_table` (`first_name`, `email`, `mobile`) SELECT `contact_name`, `contact_mail_id`, `contact_number` FROM `contact_list_from_phone` WHERE `contact_name` NOT IN (SELECT `first_name` FROM `contactlist_table`);";

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(Query_new, null);
        //System.out.println("Query_new"+Query_new);
        return cursor;

    }

    public static ArrayList<HashMap<String, String>> getTableDataContactList(
            Context context, String tableName,
            HashMap<String, String> getColumns, ArrayList<String> whereData,
            String orderByColumn, String Column_block, String Column_hide,
            String orderByType) {

        ArrayList<HashMap<String, String>> arrResult = new ArrayList<HashMap<String, String>>();

        String query = "SELECT DISTINCT ", params = " * ", whereCondition = "";
        int i = 0;
        if (getColumns != null) {
            if (getColumns.size() > 0) {
                Set<String> keys = getColumns.keySet();
                if (keys.size() > 0) {
                    for (String key : keys) {
                        if (params.length() == 0) {
                            params = key;
                        } else {
                            params = params + "," + key;
                        }
                    }
                } else {

                }
            }
        }

        query = query + " " + params + " FROM " + tableName;

        if (whereData != null) {
            if (whereData.size() > 0) {
                whereCondition = " where";
                for (int j = 0; j < whereData.size(); j++) {
                    whereCondition = whereCondition + " " + whereData.get(j);
                }
            }
        }

        // else 0 end
        query = query + whereCondition;

        query = query.trim();

        query = query + " GROUP BY " + Constant_ContactList.email+","+Constant_ContactList.mobile;

        if (orderByColumn != null && orderByType != null) {

            query = query + " ORDER BY CASE WHEN " + Column_block
                    + " is 'yes' then 2 else 0 end, " + "CASE WHEN "
                    + Column_hide + " is 'yes' then 1 else 0 end, "
                    + orderByColumn + " COLLATE NOCASE ASC;";

            //System.out.println("query>>>>>>" + query);
            // query = query + " ORDER BY " +
            // orderByColumn+" BETWEEN 'a' AND 'zzzzz' THEN "+
            // orderByColumn+" ELSE '~' || "+
            // orderByColumn+" END COLLATE NOCASE;";
        }

		/*
         * SELECT * FROM contacts ORDER BY CASE WHEN name COLLATE NOCASE BETWEEN
		 * 'a' AND 'zzzzz' THEN name ELSE '~' || name END COLLATE NOCASE;
		 */

        query = query.trim();

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    HashMap<String, String> map = new HashMap<String, String>();

                    if (getColumns != null) {
                        if (getColumns.size() > 0) {
                            Set<String> keys = getColumns.keySet();
                            if (keys.size() > 0) {
                                for (String key : keys) {
                                    if (cursor.getColumnIndex(key) != -1) {
                                        map.put(key, cursor.getString(cursor
                                                .getColumnIndex(key)));

                                    }
                                }
                            } else {

                            }
                        }
                    } else {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            map.put(cursor.getColumnName(j), cursor
                                    .getString(cursor.getColumnIndex(cursor
                                            .getColumnName(j))));
                        }
                    }

                    arrResult.add(map);

                } while (cursor.moveToNext());
            }
        }

        return arrResult;

    }

    public static JSONArray getPhoneContactsToRequestAPI(
            Context context, String tableName,
            HashMap<String, String> getColumns, ArrayList<String> whereData,
            String orderByColumn, String orderByType, String groupByColName) {

        JSONArray arrResult = new JSONArray();

        String query = "SELECT DISTINCT ", params = " * ", whereCondition = "";
        int i = 0;
        if (getColumns != null) {
            if (getColumns.size() > 0) {
                Set<String> keys = getColumns.keySet();
                if (keys.size() > 0) {
                    for (String key : keys) {
                        if (params.equalsIgnoreCase(" * ")) {
                            params = key;
                        } else {
                            params = params + "," + key;
                        }
                    }
                } else {

                }
            }
        }

        query = query + " " + params + " FROM " + tableName;

        if (whereData != null) {
            if (whereData.size() > 0) {
                whereCondition = " where";
                for (int j = 0; j < whereData.size(); j++) {
                    whereCondition = whereCondition + " " + whereData.get(j);
                }
            }
        }

        query = query + whereCondition;

        query = query.trim();

        if (groupByColName != null) {
            if (!groupByColName.trim().equalsIgnoreCase("")) {
                query = query + " GROUP BY " + groupByColName;
            }
        }

        if (orderByColumn != null && orderByType != null) {
            // query = query + " ORDER BY CASE WHEN " + orderByColumn
            // + " is 'Unknown' then 1 else 0 end, " + orderByColumn
            // + " COLLATE NOCASE ASC;";

            query = query + " ORDER BY " + orderByColumn

                    + " COLLATE NOCASE " + orderByType;

        }
        else
        {
            //query = query + " COLLATE NOCASE " ;
        }

        query = query + " ;";

        //System.out.println("query====" + query);

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);

        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        JSONObject job=new JSONObject();

                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            job.put("name", new String(cursor
                                    .getString(cursor.getColumnIndex(Constant_ContactList.first_name))+" "+
                                    cursor
                                            .getString(cursor.getColumnIndex(Constant_ContactList.last_name))).trim());
                            job.put("email", cursor
                                    .getString(cursor.getColumnIndex(Constant_ContactList.email))==null?"":cursor
                                    .getString(cursor.getColumnIndex(Constant_ContactList.email)).trim());
                            job.put("number", cursor
                                    .getString(cursor.getColumnIndex(Constant_ContactList.mobile))==null?"":cursor
                                    .getString(cursor.getColumnIndex(Constant_ContactList.mobile)).replaceAll(" ","").toString().replace("+","").toString().replace("(","").toString().replace(")","").replace("-","").trim());
                        }

                        arrResult.put(job);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return arrResult;
    }


    public static ArrayList<HashMap<String, String>> getTableData(
            Context context, String tableName,
            HashMap<String, String> getColumns, ArrayList<String> whereData,
            String orderByColumn, String orderByType, String groupByColName) {

        ArrayList<HashMap<String, String>> arrResult = new ArrayList<HashMap<String, String>>();

        String query = "SELECT DISTINCT ", params = " * ", whereCondition = "";
        int i = 0;
        if (getColumns != null) {
            if (getColumns.size() > 0) {
                Set<String> keys = getColumns.keySet();
                if (keys.size() > 0) {
                    for (String key : keys) {
                        if (params.equalsIgnoreCase(" * ")) {
                            params = key;
                        } else {
                            params = params + "," + key;
                        }
                    }
                } else {

                }
            }
        }

        query = query + " " + params + " FROM " + tableName;

        if (whereData != null) {
            if (whereData.size() > 0) {
                whereCondition = " where";
                for (int j = 0; j < whereData.size(); j++) {
                    whereCondition = whereCondition + " " + whereData.get(j);
                }
            }
        }

        query = query + whereCondition;

        query = query.trim();

        if (groupByColName != null) {
            if (!groupByColName.trim().equalsIgnoreCase("")) {
                query = query + " GROUP BY " + groupByColName;
            }
        }

        if (orderByColumn != null && orderByType != null) {
            // query = query + " ORDER BY CASE WHEN " + orderByColumn
            // + " is 'Unknown' then 1 else 0 end, " + orderByColumn
            // + " COLLATE NOCASE ASC;";

            query = query + " ORDER BY " + orderByColumn

                    + " COLLATE NOCASE " + orderByType;

        }
        else
        {
            //query = query + " COLLATE NOCASE " ;
        }

        query = query + " ;";

        //System.out.println("query====" + query);

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    HashMap<String, String> map = new HashMap<String, String>();

                    for (int j = 0; j < cursor.getColumnCount(); j++) {
                        map.put(cursor.getColumnName(j), cursor
                                .getString(cursor.getColumnIndex(cursor
                                        .getColumnName(j))));
                    }

                    arrResult.add(map);
                } while (cursor.moveToNext());
            }
        }
        return arrResult;
    }

    public static int rowCount(Context context, String TableName,
                               HashMap<String, String> map) {

        String DATABASE_COMPARE = "select count(*) from " + TableName;


        if (map != null) {

            String whrData = null;
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (whrData == null) {
                    whrData = " where " + key + "=" + map.get(key);
                } else {
                    whrData = whrData + " and " + key + "=" + map.get(key);
                }

            }

            if (whrData != null) {
                DATABASE_COMPARE = DATABASE_COMPARE + " " + whrData;
            }
        }


        int sometotal = (int) DatabaseUtils.longForQuery(getDB(context)
                .getWritableDatabase(), DATABASE_COMPARE, null);

        return sometotal;
    }

    public static int rowCount(Context context,ArrayList<String> map ,String TableName) {

        String DATABASE_COMPARE = "select count(*) from " + TableName;
        String whrData = null;

        if (map != null) {

            for (int i = 0; i <map.size() ; i++) {
                if(i==0)
                {
                    whrData = " where " + map.get(i);
                }
                else
                {
                    whrData = whrData +  map.get(i);
                }

            }


            /*Set<String> keys = map.keySet();
            for (String key : keys) {
                if (whrData == null) {
                    whrData = " where " + key + "=" + map.get(key);
                } else {
                    whrData = whrData + " and " + key + "=" + map.get(key);
                }

            }*/

            if (whrData != null) {
                DATABASE_COMPARE = DATABASE_COMPARE + " " + whrData;
            }
        }


        int sometotal = (int) DatabaseUtils.longForQuery(getDB(context)
                .getWritableDatabase(), DATABASE_COMPARE, null);

        return sometotal;
    }


    // TABLE_MYUVITE_TEMPLATE

    @Override
    public void onCreate(SQLiteDatabase db) {

        userProfile(db);
        createCountryTable(db);
        createConatctGroup(db);
        createuViteList(db);
        createPhoneContactTable(db);
        createChatlistTable(db);
        createUviteNonuviteFreindsList(db);
        createNotificationList(db);
        Create_Notification_count(db);
        timeZone(db);

    }



    public void createChatlistTable(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();

        hashtable.put(Constant_Chatlist.user_id, "TEXT");
        hashtable.put(Constant_Chatlist.event_chat_id, "TEXT");
        hashtable.put(Constant_Chatlist.event_id, "TEXT");
        hashtable.put(Constant_Chatlist.message, "TEXT");
        hashtable.put(Constant_Chatlist.photo, "TEXT");
        hashtable.put(Constant_Chatlist.added, "TEXT");
        hashtable.put(Constant_Chatlist.type, "TEXT");
        hashtable.put(Constant_Chatlist.read_status, "TEXT");
        hashtable.put(Constant_Chatlist.profile_image, "TEXT");
        hashtable.put(Constant_Chatlist.username, "TEXT");
        hashtable.put(Constant_Chatlist.read, "TEXT");
        hashtable.put(Constant_Chatlist.thumb_image, "TEXT");
        hashtable.put(Constant_Chatlist.thumb_photo, "TEXT");
        hashtable.put(Constant_Chatlist.new_checked_timestamp, "TEXT");

        createTable(db, DbParams.TABLE_CHAT_LISTING, hashtable);

    }

    private void timeZone(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(DbParams.TIMEZONE_GMT, "TEXT");
        hashtable.put(DbParams.TIMEZONE_NAME, "TEXT");

        createTable(db,DbParams.TABLE_TIMEZONE, hashtable);
    }

    private void userProfile(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();

        hashtable.put(Constant_User_Profile.user_id, "TEXT");
        hashtable.put(Constant_User_Profile.user_name, "TEXT");
        hashtable.put(Constant_User_Profile.first_name, "TEXT");
        hashtable.put(Constant_User_Profile.last_name, "TEXT");
        hashtable.put(Constant_User_Profile.mobile, "TEXT");
        hashtable.put(Constant_User_Profile.country_code, "TEXT");
        hashtable.put(Constant_User_Profile.gender, "TEXT");
        hashtable.put(Constant_User_Profile.email, "TEXT");

        hashtable.put(Constant_User_Profile.facebook_id, "TEXT");


        hashtable.put(Constant_User_Profile.added, "TEXT");
        hashtable.put(Constant_User_Profile.app_invite_email, "TEXT");
        hashtable.put(Constant_User_Profile.app_invite_sms, "TEXT");

        hashtable.put(Constant_User_Profile.event_off, "TEXT");
        hashtable.put(Constant_User_Profile.event_will_expire, "TEXT");
        hashtable.put(Constant_User_Profile.event_will_expire_hours, "TEXT");
        hashtable.put(Constant_User_Profile.event_will_start, "TEXT");
        hashtable.put(Constant_User_Profile.event_will_start_hours, "TEXT");
        hashtable.put(Constant_User_Profile.event_on, "TEXT");


        hashtable.put(Constant_User_Profile.last_name, "TEXT");

        hashtable.put(Constant_User_Profile.newu_vite, "TEXT");
        hashtable.put(Constant_User_Profile.profile_image, "TEXT");
        hashtable.put(Constant_User_Profile.received_newchat, "TEXT");
        hashtable.put(Constant_User_Profile.social, "TEXT");
        hashtable.put(Constant_User_Profile.some_one_response, "TEXT");
        hashtable.put(Constant_User_Profile.status, "TEXT");
        hashtable.put(Constant_User_Profile.thumb_image, "TEXT");
        hashtable.put(Constant_User_Profile.time_zone, "TEXT");
        hashtable.put(Constant_User_Profile.type, "TEXT");
        hashtable.put(Constant_User_Profile.verify, "TEXT");

        hashtable.put(Constant_User_Profile.vite_invite_email, "TEXT");
        hashtable.put(Constant_User_Profile.vite_invite_sms, "TEXT");


        hashtable.put(Constant_User_Profile.profile_setup,"TEXT");



        createTable(db, DbParams.TABLE_USER_PROFILE, hashtable);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





    public void Create_Notification_count(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();

        hashtable.put(Constant_Notification_Message_count.total_unread_notifications_count, "VARCHAR not null unique");
        hashtable.put(Constant_Notification_Message_count.total_unread_message_count, "TEXT");
        createTable(db, DbParams.TABLE_NOTIFICATION_MESSAGE_COUNT, hashtable);
    }

    public static ArrayList<String> get_Notification_count(Context context) {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add(0,"0");
        arr.add(1,"0");

        try {
            String query = "SELECT * FROM " + DbParams.TABLE_NOTIFICATION_MESSAGE_COUNT;

            SQLiteDatabase database = getDB(context).getWritableDatabase();

            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                if(cursor.getString(cursor.getColumnIndex(Constant_Notification_Message_count.total_unread_message_count))!=null)
                {
                    arr.add(0,cursor.getString(cursor.getColumnIndex(Constant_Notification_Message_count.total_unread_message_count)));
                }
                else
                {
                    arr.add(0,"0");
                }

                if(cursor.getString(cursor.getColumnIndex(Constant_Notification_Message_count.total_unread_notifications_count))!=null)
                {
                    arr.add(1,cursor.getString(cursor.getColumnIndex(Constant_Notification_Message_count.total_unread_notifications_count)));
                }
                else
                {
                    arr.add(1,"0");
                }
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }







    public void createuViteList(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(Constant_uViteList.event_id, "VARCHAR");
        hashtable.put(Constant_uViteList.user_id, "TEXT");
        hashtable.put(Constant_uViteList.event_name, "TEXT");
        hashtable.put(Constant_uViteList.event_address, "TEXT");
        hashtable.put(Constant_uViteList.event_photo, "TEXT");
        hashtable.put(Constant_uViteList.event_min_member, "TEXT");
        hashtable.put(Constant_uViteList.event_max_member, "TEXT");
        hashtable.put(Constant_uViteList.invitation_expire, "TEXT");
        hashtable.put(Constant_uViteList.guest_list_visible, "TEXT");
        hashtable.put(Constant_uViteList.guest_can_invite_other, "TEXT");
        hashtable.put(Constant_uViteList.guest_group_chat, "TEXT");
        hashtable.put(Constant_uViteList.event_description, "TEXT");
        hashtable.put(Constant_uViteList.event_from_time, "TEXT");
        hashtable.put(Constant_uViteList.event_to_time, "TEXT");
        hashtable.put(Constant_uViteList.sent_on_date, "TEXT");
        hashtable.put(Constant_uViteList.status, "TEXT");
        hashtable.put(Constant_uViteList.added, "TEXT");
        hashtable.put(Constant_uViteList.invited_id, "TEXT");
        hashtable.put(Constant_uViteList.type, "TEXT");
        hashtable.put(Constant_uViteList.hosted_by, "TEXT");
        hashtable.put(Constant_uViteList.event_cover_photo, "TEXT");
        hashtable.put(Constant_uViteList.event_thumb_photo, "TEXT");
        hashtable.put(Constant_uViteList.event_status, "TEXT");
        hashtable.put(Constant_uViteList.color, "TEXT");
        hashtable.put(Constant_uViteList.checked, "TEXT");
        hashtable.put(Constant_uViteList.end_date_optional, "TEXT");
        hashtable.put(Constant_uViteList.event_full, "TEXT");
        hashtable.put(Constant_uViteList.event_cancel_reason, "TEXT");
        hashtable.put(Constant_uViteList.notify_status, "TEXT");
        //Here templates four columns added
        hashtable.put(Constant_template.first_name, "TEXT");
        hashtable.put(Constant_template.last_name, "TEXT");
        hashtable.put(Constant_template.profile_image, "TEXT");
        hashtable.put(Constant_template.thumb_image, "TEXT");
        hashtable.put(Constant_template.new_checked_timestamp, "TEXT");

        hashtable.put(Constant_template.profile_image, "TEXT");
        hashtable.put(Constant_template.thumb_image, "TEXT");

        createTable(db, DbParams.TABLE_UVITE_LIST, hashtable);

    }

    public void createUviteNonuviteFreindsList(SQLiteDatabase db) {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.event_id, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.email, "TEXT");
        hashtable
                .put(Constant_Uvite_Nonuvite_Freinds_List.event_status, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.first_name, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.friend_id, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.last_name, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.mobile, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.profile_image,
                "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.r_status, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.thumb_image, "TEXT");
        hashtable.put(Constant_Uvite_Nonuvite_Freinds_List.type, "TEXT");
        hashtable.put(
                Constant_Uvite_Nonuvite_Freinds_List.uvite_nonuvite_status,
                "TEXT");
        createTable(db, DbParams.TABLE_UVITE_NONUVITE_FREINDS_LIST, hashtable);
    }



    public void createConatctGroup(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(Constant_ContactGroupList.group_id, "TEXT");
        hashtable.put(Constant_ContactGroupList.group_name, "TEXT");
        hashtable.put(Constant_ContactGroupList.user_id, "TEXT");
        // hashtable.put(Constant_ContactGroupList.first_name, "TEXT");
        // hashtable.put(Constant_ContactGroupList.last_name, "TEXT");
        // hashtable.put(Constant_ContactGroupList.email, "TEXT");
        // hashtable.put(Constant_ContactGroupList.mobile, "TEXT");
        // hashtable.put(Constant_ContactGroupList.gender, "TEXT");
        // hashtable.put(Constant_ContactGroupList.social, "TEXT");
        // hashtable.put(Constant_ContactGroupList.status, "TEXT");
        // hashtable.put(Constant_ContactGroupList.type, "TEXT");
        // hashtable.put(Constant_ContactGroupList.block, "TEXT");
        // hashtable.put(Constant_ContactGroupList.hide, "TEXT");
        // hashtable.put(Constant_ContactGroupList.profile_image, "TEXT");
        // hashtable.put(Constant_ContactGroupList.thumb_image, "TEXT");
        createTable(db, DbParams.TABLE_CONTACTGROUP_LIST, hashtable);

    }



    public void createCountryTable(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(Constant_Country.country_id, "VARCHAR not null unique");
        hashtable.put(Constant_Country.country_short_code, "TEXT");
        hashtable.put(Constant_Country.country_name, "TEXT");
        hashtable.put(Constant_Country.added, "TEXT");
        hashtable.put(Constant_Country.status, "TEXT");
        hashtable.put(Constant_Country.country_code, "TEXT");
        createTable(db, DbParams.TABLE_COUNTRY, hashtable);

    }




    public void createPhoneContactTable(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();

        hashtable.put(Constant_ContactList.contact_image_uri, "TEXT");
        hashtable.put(Constant_ContactList.user_id, "TEXT");
        hashtable.put(Constant_ContactList.first_name, "TEXT");
        hashtable.put(Constant_ContactList.last_name, "TEXT");
        hashtable.put(Constant_ContactList.email, "TEXT");
        hashtable.put(Constant_ContactList.mobile, "TEXT");
        hashtable.put(Constant_ContactList.gender, "TEXT");
        hashtable.put(Constant_ContactList.social, "TEXT");
        hashtable.put(Constant_ContactList.status, "TEXT");
        hashtable.put(Constant_ContactList.type, "TEXT");
        hashtable.put(Constant_ContactList.block, "TEXT");
        hashtable.put(Constant_ContactList.hide, "TEXT");
        hashtable.put(Constant_ContactList.profile_image, "TEXT");
        hashtable.put(Constant_ContactList.thumb_image, "TEXT");
        hashtable.put(Constant_ContactList.phone_type, "TEXT");
        hashtable.put(Constant_ContactList.phone_type, "TEXT");
        hashtable.put(Constant_ContactList.mine, "TEXT");

        createTable(db, DbParams.TABLE_CONTACT_FROM_PHONE, hashtable);

    }

    public void createTable(SQLiteDatabase database, String tableName,
                            Hashtable<String, String> tmp1) {
        String CREATE_TABLE = "create table if not exists " + tableName + "(";
        for (String key : tmp1.keySet()) {
            CREATE_TABLE = CREATE_TABLE + key + " " + tmp1.get(key) + ",";
        }

        int len = CREATE_TABLE.length();
        CREATE_TABLE = CREATE_TABLE.substring(0, len - 1) + ")";

        database.execSQL(CREATE_TABLE);
    }

    public void insertData(Hashtable<String, String> queryValues,
                           String TableName) {
        try {

            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
            }
            database.insert(TableName, null, values);
            // System.out.println(TableName + " INSERTED = "
            // + database.insert(TableName, null, values));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertMultipleData(Context context,
                                   ArrayList<Hashtable<String, String>> queryValues, String TableName) {

        try {

            if (queryValues != null && TableName != null) {

                if (queryValues.get(0) != null) {

                    String columns = "";

                    Set<String> keys = queryValues.get(0).keySet();
                    if (keys.size() > 0) {
                        for (String key : keys) {
                            if (columns.length() == 0) {
                                columns = "'" + key + "'";
                            } else {
                                columns = columns + " , " + "'" + key + "'";
                            }

                        }
                    }

                    if (columns.trim().length() > 0) {
                        columns = " ( " + columns + " ) values ";

                        String query = "INSERT INTO " + TableName + columns;
                        String allRowsData = "";

                        for (int i = 0; i < queryValues.size(); i++) {
                            Set<String> key1 = queryValues.get(i).keySet();
                            if (key1.size() > 0) {
                                String rowData = "";
                                for (String key : key1) {
                                    if (rowData.length() == 0) {

                                        rowData = "'"
                                                + queryValues.get(i).get(key)
                                                .replaceAll("'", "''")
                                                + "'";

                                    } else {

                                        rowData = rowData
                                                + ","
                                                + "'"
                                                + queryValues.get(i).get(key)
                                                .replaceAll("'", "''")
                                                + "'";
                                    }
                                }

                                if (rowData.trim().length() > 0) {

                                    rowData = "(" + rowData + ")";

                                    if (allRowsData.length() == 0) {
                                        allRowsData = rowData;
                                    } else {
                                        allRowsData = allRowsData + ", "
                                                + rowData;
                                    }

                                }

                            }
                        }

                        query = query + " " + allRowsData+";";

                       // System.out.println("INSERTquery==" + query);

                        SQLiteDatabase database = getDB(context)
                                .getWritableDatabase();
                        database.execSQL(query);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAll(String TableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, null, null);
        // System.out.println("DELETED==" + db.delete(TableName, null, null) +
        // " "
        // + TableName);

    }

    public void deleteAll(ArrayList<String> TableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < TableName.size(); i++) {
            db.delete(TableName.get(i), null, null);
        }

    }

    public void delete_condiction_wise1(String TABLE_NAME,
                                        Hashtable<String, String> queryValues) {
        int i = 0;

        SQLiteDatabase database = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE ";

        for (String key : queryValues.keySet()) {

            if (i == queryValues.size() - 1) {
                query = query + " " + key + "=" + queryValues.get(key);
            } else {
                query = query + " " + key + "=" + queryValues.get(key) + " "
                        + " AND ";
            }

            i++;
        }

        database.rawQuery(query, null);
        // database.rawQuery(query, null);

    }

    public void editData(Hashtable<String, String> queryValues, String Key,
                         String value, String TABLE_NAME) {
        try {

            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            for (String key : queryValues.keySet()) {
                values.put(key, queryValues.get(key));
                // //System.out.println("VALUES=="+key+"    "+queryValues.get(key));
            }

            int ab = database.update(TABLE_NAME, values, Key + "=" + "'"+value+"'",
                    null);

            /*System.out.println("Here is code for edition>>>>" + TABLE_NAME
                    + " EDITED " + TABLE_NAME + "==" + ab + "  key " + Key
                    + " value" + value);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createNotificationList(SQLiteDatabase db) {

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put(Constant_notification_list.message, "TEXT");
        hashtable.put(Constant_notification_list.thumb_image, "TEXT");
        hashtable.put(Constant_notification_list.added, "TEXT");
        hashtable.put(Constant_notification_list.status, "TEXT");
        hashtable.put(Constant_notification_list.profile_image, "TEXT");
        hashtable.put(Constant_notification_list.sender_id, "TEXT");
        hashtable.put(Constant_notification_list.read, "TEXT");
        hashtable.put(Constant_notification_list.notification_id, "TEXT");
        hashtable.put(Constant_notification_list.type, "TEXT");
        hashtable.put(Constant_notification_list.receiver_id, "TEXT");
        hashtable.put(Constant_notification_list.virtual_id, "TEXT");

        createTable(db, DbParams.TABLE_NOTIFICATION_LIST_TABLE, hashtable);
    }

    public Cursor getSearchData(Context context, String TABLE_NAME,
                                String ColumnName, String keyword) {

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ColumnName
                + " LIKE " + "'%" + keyword + "%'" + ";";

        //System.out.println("query in getsearch" + query);

        SQLiteDatabase database = getDB(context).getWritableDatabase();

        Cursor cursor = database.rawQuery(query, null);

        return cursor;
    }




    public static String gettimezone(Context context,String userid)
    {
        String timezone="";
        try {
            String query = "SELECT time_zone FROM " + DbParams.TABLE_USER_PROFILE + " where user_id="+userid;
            SQLiteDatabase database = getDB(context).getWritableDatabase();
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst())
            {
                do {

                    timezone=cursor.getString(cursor.getColumnIndex(Constant_User_Profile.time_zone));
                } while (cursor.moveToNext());
            }
            if(timezone=="")
            {
                TimeZone tz = TimeZone.getDefault();
                timezone = tz.getID().toString();
                System.out.println("in if============"+timezone);
            }
            System.out.println("============"+timezone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timezone;
    }



    public static Cursor getsorttimezone(Context context)
    {

        Cursor cursor=null;
        try {
            String query = "SELECT * FROM timezone order by SUBSTR(gmt,1,3), CASE WHEN gmt LIKE 'GMT%' THEN CAST( SUBSTR(gmt, 4) AS INTEGER) ELSE gmt END, name";
            SQLiteDatabase database = getDB(context).getWritableDatabase();
             cursor= database.rawQuery(query, null);
            if(cursor!=null) {
                if (cursor.moveToFirst()) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }



    //



    public static ArrayList<String> getuserdata(Context context,String userid)
    {


        ArrayList<String> arr=new ArrayList<String>();
        arr.add(0,"0");
        arr.add(1,"1");
        try {
            String query = "SELECT * FROM " + DbParams.TABLE_USER_PROFILE + " where user_id="+userid;
            SQLiteDatabase database = getDB(context).getWritableDatabase();
            Cursor cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst())
            {
                do {
                    arr.add(0,cursor.getString(cursor.getColumnIndex(Constant_User_Profile.first_name))+" "+cursor.getString(cursor.getColumnIndex(Constant_User_Profile.last_name)));
                    arr.add(1,cursor.getString(cursor.getColumnIndex(Constant_User_Profile.thumb_image)));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }



 public static void delete_condition_product_image(Context context, String productid, String product_stockid) {
        SQLiteDatabase db = getDB(context).getWritableDatabase();
        try {

            String query = "delete from " + Dbparam.TBL_Product_image + " where " + Dbparam.product_image.product_id + " = "
                    + "'" + productid + "'" + " and " + Dbparam.product_image.product_stock_id + " = " + "'" + product_stockid + "'";

            Cursor cursor = db.rawQuery(query, null);
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // db.close();
        }
    }





}
