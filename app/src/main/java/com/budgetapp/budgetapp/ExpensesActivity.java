package com.budgetapp.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class ExpensesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHandler dbHandler = new DBHandler(this);
        final Activity thisActivity = this;
        final int textSize = (int) (getResources().getDimension(R.dimen.display_font_size) /
                                    getResources().getDisplayMetrics().density);
        final int textPadding = (int) (getResources().getDimension(R.dimen.display_font_padding) /
                                       getResources().getDisplayMetrics().density);

        LinearLayout budgetList = (LinearLayout) findViewById(R.id.expenseList);
        ArrayList<Expense> expenseEntries = dbHandler.findAllExpenses();
        while (expenseEntries.size() > 0) {
            Expense entry = expenseEntries.remove(0);

            // Create linear layout for expense entry
            LinearLayout tableEntry = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tableEntry.setLayoutParams(params);
            tableEntry.setOrientation(LinearLayout.HORIZONTAL);

            // Fill in columns
            TextView expenseName = new TextView(this);
            LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            expenseName.setText(entry.getName());
            expenseName.setTextSize(textSize);
            expenseName.setPadding(textPadding, textPadding, textPadding, textPadding);
            expenseName.setLayoutParams(columnParams);

            TextView expenseCategory = new TextView(this);
            expenseCategory.setText(entry.getCategory());
            expenseCategory.setTextSize(textSize);
            expenseCategory.setPadding(textPadding, textPadding, textPadding, textPadding);
            expenseCategory.setLayoutParams(columnParams);

            TextView expenseAmount = new TextView(this);
            NumberFormat formatter = new DecimalFormat("#0.00");
            expenseAmount.setText(formatter.format(entry.getAmount()) + "");
            expenseAmount.setTextSize(textSize);
            expenseAmount.setPadding(textPadding, textPadding, textPadding, textPadding);
            expenseAmount.setLayoutParams(columnParams);

            TextView expensePayment = new TextView(this);
            expensePayment.setText(entry.getPayment());
            expensePayment.setTextSize(textSize);
            expensePayment.setPadding(textPadding, textPadding, textPadding, textPadding);
            expensePayment.setLayoutParams(columnParams);

            // Insert all views
            tableEntry.addView(expenseName);
            tableEntry.addView(expenseCategory);
            tableEntry.addView(expenseAmount);
            tableEntry.addView(expensePayment);
            budgetList.addView(tableEntry);
        }

        Button expensetEntry = (Button) findViewById(R.id.expenseEntry);
        expensetEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExpensesEntryActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expenses, menu);
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
