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


public class PaymentsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;
        DBHandler dbHandler = new DBHandler(this);
        final int textSize = (int) (getResources().getDimension(R.dimen.display_font_size) /
                getResources().getDisplayMetrics().density);
        final int textPadding = (int) (getResources().getDimension(R.dimen.display_font_padding) /
                getResources().getDisplayMetrics().density);

        LinearLayout paymentsList = (LinearLayout) findViewById(R.id.paymentsList);
        ArrayList<String> paymentEntries = dbHandler.findAllPaymentMethods();
        while (paymentEntries.size() > 0) {
            String entry = paymentEntries.remove(0);

            // Create linear layout for budget entry
            LinearLayout tableEntry = new LinearLayout(thisActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tableEntry.setLayoutParams(params);
            tableEntry.setOrientation(LinearLayout.HORIZONTAL);

            // Fill in columns
            // Name column
            TextView categoryName = new TextView(thisActivity);
            LinearLayout.LayoutParams columnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            categoryName.setText(entry);
            categoryName.setTextSize(textSize);
            categoryName.setPadding(textPadding, textPadding, textPadding, textPadding);
            categoryName.setLayoutParams(columnParams);

            // Current balance
            TextView currentBalance = new TextView(thisActivity);
            NumberFormat formatter = new DecimalFormat("#0.00");
            double currentTotal = dbHandler.findPaymentBalance(entry);
            currentBalance.setText(formatter.format(currentTotal) + "");
            currentBalance.setTextSize(textSize);
            currentBalance.setPadding(textPadding, textPadding, textPadding, textPadding);
            currentBalance.setLayoutParams(columnParams);

            // Insert all views
            tableEntry.addView(categoryName);
            tableEntry.addView(currentBalance);
            paymentsList.addView(tableEntry);
        }

        Button paymentEntry = (Button) findViewById(R.id.paymentEntry);
        paymentEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PaymentsEntryActivity.class);
                startActivity(intent);
            }
        });

        Button paymentDeposit = (Button) findViewById(R.id.paymentDeposit);
        paymentDeposit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PaymentsDepositActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payments, menu);
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
