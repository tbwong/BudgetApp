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
import android.widget.EditText;
import android.widget.Toast;


public class BudgetEntryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Activity thisActivity = this;

        Button budgetEntryDone = (Button) findViewById(R.id.budgetEntryDone);
        budgetEntryDone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    EditText categoryName = (EditText) findViewById(R.id.enterBudgetCategory);
                    EditText categoryExpected = (EditText) findViewById(R.id.enterBudgetExpected);
                    String nameInput = categoryName.getText().toString();
                    double expectedInput = Double.parseDouble(categoryExpected.getText().toString());

                    BudgetCategory entry = new BudgetCategory(nameInput, expectedInput);
                    DBHandler dbHandler = new DBHandler(thisActivity);
                    dbHandler.addBudgetEntry(entry);

                    Intent intent = new Intent(v.getContext(), BudgetActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_budget_entry, menu);
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
