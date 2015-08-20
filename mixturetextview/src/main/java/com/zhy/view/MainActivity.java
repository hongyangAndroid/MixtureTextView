package com.zhy.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    MixtureTextView mixtureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mixtureTextView = (MixtureTextView) findViewById(R.id.id_mixtureTextview);
        //mixtureTextView.setTextColor(0xffff6f2d);
        //  mixtureTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        //mixtureTextView.setText(getString(R.string.text1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_size_add:
                mixtureTextView.setTextSize(mixtureTextView.getTextSize() + 2);
                break;
            case R.id.action_size_del:
                mixtureTextView.setTextSize(mixtureTextView.getTextSize() - 2);

                break;
            case R.id.action_toggleText:
                String text = mixtureTextView.getText();
                if (text.equals(getString(R.string.text1)))
                {
                    mixtureTextView.setText(getString(R.string.text2));
                } else
                {
                    mixtureTextView.setText(getString(R.string.text1));
                }


                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
