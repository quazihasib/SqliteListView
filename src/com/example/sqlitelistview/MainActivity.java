package com.example.sqlitelistview;

import java.util.ArrayList;

import SqliteDB.ContactListAdapter;
import SqliteDB.ContactListItems;
import SqliteDB.SqlHandler;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	SqlHandler sqlHandler;
	ListView lvCustomList;
	EditText etName, etPhone;
	Button btnsubmit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lvCustomList = (ListView) findViewById(R.id.lv_custom_list);
		etName = (EditText) findViewById(R.id.et_name);
		etPhone = (EditText) findViewById(R.id.et_phone);
		btnsubmit = (Button) findViewById(R.id.btn_submit);
		sqlHandler = new SqlHandler(this);
		showList();
		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = etName.getText().toString();
				String phoneNo = etPhone.getText().toString();

				String query = "INSERT INTO PHONE_CONTACTS(name,phone) values ('"
						+ name + "','" + phoneNo + "')";
				sqlHandler.executeQuery(query);
				showList();
				etName.setText("");
				etPhone.setText("");

			}
		});

	}

	private void showList() {

		ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
		contactList.clear();
		String query = "SELECT * FROM PHONE_CONTACTS ";
		Cursor c1 = sqlHandler.selectQuery(query);
		if (c1 != null && c1.getCount() != 0) {
			if (c1.moveToFirst()) {
				do {
					ContactListItems contactListItems = new ContactListItems();

					contactListItems.setSlno(c1.getString(c1
							.getColumnIndex("slno")));
					contactListItems.setName(c1.getString(c1
							.getColumnIndex("name")));
					contactListItems.setPhone(c1.getString(c1
							.getColumnIndex("phone")));
					contactList.add(contactListItems);

				} while (c1.moveToNext());
			}
		}
		c1.close();

		ContactListAdapter contactListAdapter = new ContactListAdapter(
				MainActivity.this, contactList);
		lvCustomList.setAdapter(contactListAdapter);
		
		lvCustomList.setOnItemClickListener(new OnItemClickListener()
		{
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) 
	        {

	        	Object o = lvCustomList.getItemAtPosition(position);
	        	ContactListItems ci = (ContactListItems)o;
	        	
	        	Toast.makeText(getBaseContext(),"pos:"+position+" "+ci
	        			,Toast.LENGTH_SHORT).show();
	        }
	    });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}