package uk.ac.stir.cs.insulinpredictorapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_Entries = "Entries";
    private static final String TABLE_Predictions = "Predictions";
    private static final String TABLE_Settings = "Settings";

    public DBHelper(Context context) {
        super(context, "userdata.db", null, 1);
    }

    /**
     * Creates the databases if not created already
     */
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table IF NOT EXISTS Entries(id INTEGER primary key AUTOINCREMENT, BSbefore DOUBLE, Carbohydrates INTEGER, Insulin INTEGER)");
        DB.execSQL("create Table IF NOT EXISTS Predictions(id INTEGER primary key AUTOINCREMENT, Date STRING, " +
                "BSbefore DOUBLE, Carbohydrates INTEGER, Prediction DOUBLE, BSafter DOUBLE)");
        DB.execSQL("create Table IF NOT EXISTS Settings(id INTEGER primary key AUTOINCREMENT, Hyper Double, HighBS DOUBLE, LowBS DOUBLE, Hypo DOUBLE)");
    }
    /**
     * If the database version is larger than the current version on the app then the tables get dropped
     */
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Entries");
        DB.execSQL("drop Table if exists Predictions");
        DB.execSQL("drop Table if exists Settings");
    }
    /**
     * Inserts blood sugar before, carbohydrates and insulin into the the entries table
     */
    public Boolean InsertData(double BSbefore, int Carbohydrates, int Insulin) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BSbefore", BSbefore);
        contentValues.put("Carbohydrates", Carbohydrates);
        contentValues.put("Insulin", Insulin);
        long result = DB.insert("Entries", null, contentValues);
        return result != -1;
    }
    /**
     * Inserts date, blood sugar before, carbohydrates and prediction into the predictions table
     */
    public void InsertPredictions(String date, double BSbefore, int Carbohydrates, double Prediction) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("BSbefore", BSbefore);
        contentValues.put("Carbohydrates", Carbohydrates);
        contentValues.put("Prediction", Prediction);

        DB.insert("Predictions", null, contentValues);
    }
    /**
     * Updates the prediction table
     * Where the ID is true
     */
    public void InsertBSAfter(String date, double BSbefore, int Carbohydrates, double Prediction, double BSafter, String id) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("BSbefore", BSbefore);
        contentValues.put("Carbohydrates", Carbohydrates);
        contentValues.put("Prediction", Prediction);
        contentValues.put("BSafter", BSafter);
        DB.update("Predictions", contentValues,"ID = ?", new String[]{id});
    }
    /**
     * Inserts hyperglycemia, high blood sugar, low blood sugar and hypoglycemia values into the setting table
     */
    public void InsertSettings(double Hyper, double HighBS, double LowBS, double Hypo)
    {
            SQLiteDatabase DB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Hyper", Hyper);
            contentValues.put("HighBS", HighBS);
            contentValues.put("LowBS", LowBS);
            contentValues.put("Hypo", Hypo);
            DB.insert("Settings", null, contentValues);
    }

    /**
     * Fetches the data from the entries table
     * returns a list of InputModel
     */
    public List<InputModel> getData() {
        List<InputModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_Entries;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                double bsBefore = cursor.getInt(1);
                int carbohydrates = cursor.getInt(2);
                int insulin = cursor.getInt(3);
                InputModel newInput = new InputModel(id, bsBefore, carbohydrates, insulin);
                returnList.add(newInput);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;


    }
    /**
     * Fetches the predictions from the predictions table
     * returns a list of PredictionModels
     */
    public List<PredictionModel> getPredictions() {
        List<PredictionModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_Predictions;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                double bsBefore = cursor.getDouble(2);
                int carbohydrates = cursor.getInt(3);
                double prediction = cursor.getInt(4);
                double bsAfter = cursor.getDouble(5);
                PredictionModel newInput = new PredictionModel(id, date, bsBefore, carbohydrates, prediction, bsAfter);
                returnList.add(newInput);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    /**
     * Fetches the settings from the settings table
     * Returns a list of SettingsModel
     */
    public List<SettingsModel> getSettings() {
        List<SettingsModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_Settings;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                double Hyper = cursor.getDouble(1);
                double HighBS = cursor.getDouble(2);
                double LowBS = cursor.getDouble(3);
                double Hypo = cursor.getDouble(4);
                SettingsModel newInput = new SettingsModel(Hyper, HighBS, LowBS, Hypo);
                returnList.add(newInput);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    /**
     * Updates the settings table
     *
     */
    public void updateSettings(double Hyper, double HighBS, double LowBS, double Hypo){
        SQLiteDatabase DB = this.getWritableDatabase();
        String id = "1"; //Only one field in the settings table
        ContentValues contentValues = new ContentValues();
        contentValues.put("Hyper", Hyper);
        contentValues.put("HighBS", HighBS);
        contentValues.put("LowBS", LowBS);
        contentValues.put("Hypo", Hypo);
        DB.update("Settings", contentValues,"ID = ?", new String[]{id});
    }
    /**
     * Deletes data from the entries table
     * where ID is equal to the integer id
     */
    public void deleteData (Integer id){
        String idString = id.toString();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Entries, "ID = ?", new String[]{idString});
    }
    /**
     * Deletes prediction from the predictions table
     * where ID is equal to the integer id
     */
    public void deletePredictions (Integer id){
        String idString = id.toString();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Predictions, "ID = ?", new String[]{idString});
    }
    /**
     * Deletes all data from the entries table
     */
    public void deleteAllData(){
        SQLiteDatabase DB =this.getWritableDatabase();
        DB.execSQL("DELETE FROM " + TABLE_Entries);
        DB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_Entries + "'");

    }
    /**
     * Deletes all predictions from the predictions table
     */
    public void deleteAllPredictions(){
        SQLiteDatabase DB =this.getWritableDatabase();
        DB.execSQL("DELETE FROM " + TABLE_Predictions);
        DB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_Predictions + "'");
    }
    /**
     * Searches the prediction table
     * by date inputted by the user
     * returns a list of predictions that contain the inputted date
     */
    public List<PredictionModel> searchPredictionsByDate(String searchDate){
        String datecol = "date";
        List<PredictionModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Predictions + " where " + datecol + " like ?", new String[] { "%" + searchDate + "%" });
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                double bsBefore = cursor.getDouble(2);
                int carbohydrates = cursor.getInt(3);
                double prediction = cursor.getInt(4);
                double bsAfter = cursor.getDouble(5);
                PredictionModel newInput = new PredictionModel(id, date, bsBefore, carbohydrates, prediction, bsAfter);
                returnList.add(newInput);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;


    }

}

