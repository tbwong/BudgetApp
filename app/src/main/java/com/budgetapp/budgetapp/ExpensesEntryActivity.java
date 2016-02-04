package com.budgetapp.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class ExpensesEntryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;
        final DBHandler dbHandler = new DBHandler(thisActivity);

        Spinner budgetCategorySpinner = (Spinner)findViewById(R.id.selectExpenseCategory);
        ArrayList<String> budgetCategories = dbHandler.findAllBudgetCategories();
        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<String>(thisActivity, R.layout.spinner_item, budgetCategories);
        budgetCategorySpinner.setAdapter(categoryAdapter);

        Spinner paymentTypeSpinner = (Spinner)findViewById(R.id.selectPaymentType);
        ArrayList<String> paymentTypes = dbHandler.findAllPaymentMethods();
        Log.e("ExpensesEntryActivity", paymentTypes.size() +"");
        ArrayAdapter<String> paymentAdapter =
                new ArrayAdapter<String>(thisActivity, R.layout.spinner_item, paymentTypes);
        paymentTypeSpinner.setAdapter(paymentAdapter);

        Button expenseEntryDone = (Button) findViewById(R.id.expenseEntryDone);
        expenseEntryDone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    EditText expenseName = (EditText) findViewById(R.id.enterExpenseName);
                    Spinner expenseCategory = (Spinner) findViewById(R.id.selectExpenseCategory);
                    EditText expenseAmount = (EditText) findViewById(R.id.enterExpenseAmount);
                    Spinner expensePayment = (Spinner) findViewById(R.id.selectPaymentType);

                    String nameInput = expenseName.getText().toString();
                    String categoryInput = expenseCategory.getSelectedItem().toString();
                    double amountInput = Double.parseDouble(expenseAmount.getText().toString());
                    String paymentInput = expensePayment.getSelectedItem().toString();

                    Expense entry = new Expense(nameInput, categoryInput, amountInput, paymentInput);
                    dbHandler.addExpense(entry);

                    Intent intent = new Intent(v.getContext(), ExpensesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(thisActivity, "Please enter a valid input", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expenses_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
