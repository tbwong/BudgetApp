package com.budgetapp.budgetapp;

/**
 * Created by Tiffany on 1/19/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "budgetDB.db";

    private static final String TABLE_BUDGET = "budget";
    public static final String COLUMN_BUDGETCATEGORY = "budgetcategory";
    public static final String COLUMN_BUDGETEXPECTED = "budgetexpected";

    private static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSESNAME = "expensesname";
    public static final String COLUMN_EXPENSESCATEGORY = "expensescategory";
    public static final String COLUMN_EXPENSESAMOUNT = "expensesamount";
    public static final String COLUMN_EXPENSESPAYMENT = "expensespayment";

    private static final String TABLE_PAYMENTS = "payments";
    public static final String COLUMN_PAYMENTSNAME = "paymentsname";
    public static final String COLUMN_PAYMENTSBALANCE = "paymentsbalance";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BUDGET_TABLE =
                "CREATE TABLE " + TABLE_BUDGET + "("
                + COLUMN_BUDGETCATEGORY + " TEXT, "
                + COLUMN_BUDGETEXPECTED + " REAL" + ")";
        db.execSQL(CREATE_BUDGET_TABLE);

        String CREATE_EXPENSES_TABLE =
                "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_EXPENSESNAME + " TEXT, "
                + COLUMN_EXPENSESCATEGORY + " TEXT, "
                + COLUMN_EXPENSESAMOUNT + " REAL, "
                + COLUMN_EXPENSESPAYMENT + " TEXT" + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);

        String CREATE_PAYMENTS_TABLE =
                "CREATE TABLE " + TABLE_PAYMENTS + "("
                + COLUMN_PAYMENTSNAME + " TEXT, "
                + COLUMN_PAYMENTSBALANCE + " REAL" + ")";
        db.execSQL(CREATE_PAYMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }

    public void addBudgetEntry(BudgetCategory budgetEntry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGETCATEGORY, budgetEntry.getCategoryName());
        values.put(COLUMN_BUDGETEXPECTED, budgetEntry.getExpected());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_BUDGET, null, values);
        db.close();
    }

    public void addExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSESNAME, expense.getName());
        values.put(COLUMN_EXPENSESCATEGORY, expense.getCategory());
        values.put(COLUMN_EXPENSESAMOUNT, expense.getAmount());
        values.put(COLUMN_EXPENSESPAYMENT, expense.getPayment());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXPENSES, null, values);
        db.close();

        this.changePaymentBalance(expense.getPayment(), expense.getAmount() * -1);
    }

    public void addPaymentType(String paymentType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENTSNAME, paymentType);
        values.put(COLUMN_PAYMENTSBALANCE, 0);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PAYMENTS, null, values);
        db.close();
    }

    public void changePaymentBalance(String paymentType, double change) {
        ContentValues values = new ContentValues();

        String query = "Select * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTSNAME + " =  \"" + paymentType + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double currentBalance = 0;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            currentBalance = Double.parseDouble(cursor.getString(1));
        }
        values.put(COLUMN_PAYMENTSNAME, paymentType);
        values.put(COLUMN_PAYMENTSBALANCE, currentBalance + change);
        db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENTSNAME + " =  \"" + paymentType + "\"", null);

        cursor.close();
        db.close();
    }

    public BudgetCategory findBudgetEntry(String category) {
        String query = "Select * FROM " + TABLE_BUDGET + " WHERE " + COLUMN_BUDGETCATEGORY + " =  \"" + category + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        BudgetCategory budgetEntry = null;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String categoryName = cursor.getString(0);
            double expectedAmount = Double.parseDouble(cursor.getString(1));
            cursor.close();
            budgetEntry = new BudgetCategory(categoryName, expectedAmount);
        }
        db.close();
        return budgetEntry;
    }

    public ArrayList<BudgetCategory> findAllBudgetEntries() {
        String query = "Select * FROM " + TABLE_BUDGET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<BudgetCategory> budgetEntries = new ArrayList<BudgetCategory>();

        while (cursor.moveToNext()) {
            String categoryName = cursor.getString(0);
            double expectedAmount = Double.parseDouble(cursor.getString(1));
            budgetEntries.add(new BudgetCategory(categoryName, expectedAmount));
        }
        cursor.close();
        db.close();
        return budgetEntries;
    }

    public ArrayList<String> findAllBudgetCategories() {
        String query = "Select * FROM " + TABLE_BUDGET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> budgetCategories = new ArrayList<String>();

        while (cursor.moveToNext()) {
            budgetCategories.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        return budgetCategories;
    }

    public ArrayList<Expense> findAllExpenses() {
        String query = "Select * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Expense> expenseEntries = new ArrayList<Expense>();

        while (cursor.moveToNext()) {
            String expenseName = cursor.getString(0);
            String expenseCategory = cursor.getString(1);
            double expenseAmount = Double.parseDouble(cursor.getString(2));
            String expensePayment = cursor.getString(3);
            expenseEntries.add(new Expense(expenseName, expenseCategory, expenseAmount, expensePayment));
        }
        cursor.close();
        db.close();

        return expenseEntries;
    }

    public double findTotalCategoryExpense(String category) {
        String query = "Select * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_EXPENSESCATEGORY + " =  \"" + category + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double total = 0;

        while (cursor.moveToNext()) {
            total += Double.parseDouble(cursor.getString(2));
        }
        cursor.close();
        db.close();

        return total;
    }

    public ArrayList<String> findAllPaymentMethods() {
        String query = "Select * FROM " + TABLE_PAYMENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> paymentMethods = new ArrayList<String>();

        while (cursor.moveToNext()) {
            paymentMethods.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        Log.e("DBHandler", paymentMethods.size() +"");
        return paymentMethods;
    }

    public double findPaymentBalance(String paymentType) {
        String query = "Select * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTSNAME + " =  \"" + paymentType + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double total = 0;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            total = Double.parseDouble(cursor.getString(1));
        }
        cursor.close();
        db.close();

        return total;
    }
}