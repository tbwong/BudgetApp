package com.budgetapp.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class PaymentsDepositActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_deposit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;
        final DBHandler dbHandler = new DBHandler(thisActivity);

        Spinner paymentTypeSpinner = (Spinner)findViewById(R.id.selectPaymentTypeDeposit);
        ArrayList<String> paymentTypes = dbHandler.findAllPaymentMethods();
        ArrayAdapter<String> paymentAdapter =
                new ArrayAdapter<String>(thisActivity, R.layout.spinner_item, paymentTypes);
        paymentTypeSpinner.setAdapter(paymentAdapter);

        Button paymentDepositDone = (Button) findViewById(R.id.paymentDepositDone);
        paymentDepositDone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    Spinner paymentTypeSpinner = (Spinner)findViewById(R.id.selectPaymentTypeDeposit);
                    EditText paymentDepositAmount = (EditText)findViewById(R.id.enterPaymentDepositAmount);

                    String paymentType = paymentTypeSpinner.getSelectedItem().toString();
                    double paymentAmount = Double.parseDouble(paymentDepositAmount.getText().toString());

                    DBHandler dbHandler = new DBHandler(thisActivity);
                    dbHandler.changePaymentBalance(paymentType, paymentAmount);

                    Intent intent = new Intent(v.getContext(), PaymentsActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_payments_deposit, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
