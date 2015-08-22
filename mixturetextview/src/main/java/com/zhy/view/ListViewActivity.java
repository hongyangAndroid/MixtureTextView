package com.zhy.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends AppCompatActivity
{

    private ListView mListView;
    private LayoutInflater mInflater ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mInflater = LayoutInflater.from(this);

        mListView = (ListView) findViewById(R.id.id_listview);

        mListView.setAdapter(new ArrayAdapter<String>(this, -1, getResources().getStringArray(R.array.texts))
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if(convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.item_category, parent, false);
                }

                MixtureTextView mixtureTextView = (MixtureTextView) convertView.findViewById(R.id.id_mixtureTextview);
                mixtureTextView.setText(getItem(position));
                return convertView;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ListViewActivity.this,ArticleActivity.class);
                startActivity(intent);
            }
        });
    }

}
