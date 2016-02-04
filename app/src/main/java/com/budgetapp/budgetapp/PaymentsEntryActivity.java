package com.budgetapp.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PaymentsEntryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;

        Button paymentEntryDone = (Button) findViewById(R.id.paymentEntryDone);
        paymentEntryDone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    EditText paymentName = (EditText) findViewById(R.id.enterPaymentType);
                    String nameInput = paymentName.getText().toString();

                    DBHandler dbHandler = new DBHandler(thisActivity);
                    dbHandler.addPaymentType(nameInput);

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
        getMenuInflater().inflate(R.menu.menu_payment_entry, menu);
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
