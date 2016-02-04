package com.budgetapp.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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


public class BudgetActivity extends ActionBarActivity {

    final int textSize = (int) (getResources().getDimension(R.dimen.display_font_size) /
            getResources().getDisplayMetrics().density);
    final int textPadding = (int) (getResources().getDimension(R.dimen.display_font_padding) /
            getResources().getDisplayMetrics().density);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;
        DBHandler dbHandler = new DBHandler(this);

        NumberFormat formatter = new DecimalFormat("#0.00");


        double totalExpected = 0;
        double totalSpending = 0;
        LinearLayout budgetList = (LinearLayout) findViewById(R.id.budgetList);
        ArrayList<BudgetCategory> budgetEntries = dbHandler.findAllBudgetEntries();


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        while (budgetEntries.size() > 0) {
            BudgetCategory entry = budgetEntries.remove(0);

            // Create linear layout for budget entry
            LinearLayout tableEntry = new LinearLayout(thisActivity);
            tableEntry.setLayoutParams(params);
            tableEntry.setOrientation(LinearLayout.HORIZONTAL);

            // Fill in columns
            // Name column
            TextView categoryName = new TextView(thisActivity);
            categoryName.setText(entry.getCategoryName());
            setTextViewAttributes(categoryName);

            // Expected amount column
            TextView expectedAmount = new TextView(thisActivity);
            expectedAmount.setText(formatter.format(entry.getExpected()) + "");
            setTextViewAttributes(expectedAmount);
            totalExpected += entry.getExpected() / 100 * 100;

            // Current amount column
            TextView currentAmount = new TextView(thisActivity);
            double currentTotal = dbHandler.findTotalCategoryExpense(entry.getCategoryName());
            currentAmount.setText(formatter.format(currentTotal) + "");
            setTextViewAttributes(currentAmount);
            if (currentTotal > entry.getExpected() / 100 * 100) {
                currentAmount.setTextColor(Color.RED);

            }
            totalSpending += currentTotal;

            // Insert all views
            tableEntry.addView(categoryName);
            tableEntry.addView(expectedAmount);
            tableEntry.addView(currentAmount);
            budgetList.addView(tableEntry);
        }





        // Create linear layout for total entry
        LinearLayout totalEntry = new LinearLayout(thisActivity);
        totalEntry.setLayoutParams(params);
        totalEntry.setOrientation(LinearLayout.HORIZONTAL);

        // Name column
        TextView categoryName = new TextView(thisActivity);
        categoryName.setText("Total");
        setTextViewAttributes(categoryName);

        // Expected amount column
        TextView expectedAmount = new TextView(thisActivity);
        expectedAmount.setText(formatter.format(totalExpected + ""));
        setTextViewAttributes(expectedAmount);

        // Current amount column
        TextView currentAmount = new TextView(thisActivity);
        currentAmount.setText(formatter.format(totalSpending) + "");
        setTextViewAttributes(currentAmount);
        if (totalSpending > totalExpected) {
            currentAmount.setTextColor(Color.RED);
        }

        // Insert all views
        totalEntry.addView(categoryName);
        totalEntry.addView(expectedAmount);
        totalEntry.addView(currentAmount);
        budgetList.addView(totalEntry);




        Button budgetEntry = (Button) findViewById(R.id.budgetEntry);
        budgetEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BudgetEntryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTextViewAttributes(TextView view) {
        LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        view.setTextSize(textSize);
        view.setPadding(textPadding, textPadding, textPadding, textPadding)
        view.setLayoutParams(columnParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
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
