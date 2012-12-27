package pack.pack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HisaabActivity extends ListActivity implements
		android.view.View.OnClickListener, OnItemClickListener {
	static final int NEW_TRIP = 1;
	static final int EDIT_TRIP = 2;
	public static final String[] CONTEXT_MENU_ITEMS = new String[] { "Open",
			"Edit", "Delete" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		refreshSummary();
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(this);
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(this, TripActivity.class);
		startActivityForResult(myIntent, NEW_TRIP);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		openTrip(arg1);
	}

	public void openTrip(View clicked) {
		Intent myIntent = new Intent(this, OneTripActivity.class);
		myIntent.putExtra("trip_id", (Integer) clicked.getTag());
		startActivityForResult(myIntent, NEW_TRIP);

	}

	public void editTrip(View clicked) {
		Intent myIntent = new Intent(this, TripActivity.class);
		LinearLayout ll = (LinearLayout) clicked;
		TextView name = (TextView) ll.getChildAt(0);
		TextView desc = (TextView) ll.getChildAt(1);
		myIntent.putExtra("name", name.getText().toString());
		myIntent.putExtra("description", desc.getText().toString());
		myIntent.putExtra("trip_id", (Integer) ll.getTag());
		startActivityForResult(myIntent, EDIT_TRIP);
	}

	public void deleteTrip(View clicked) {
		LinearLayout ll = (LinearLayout) clicked;
		ConfirmDeleteDialog myDialog = new ConfirmDeleteDialog((Integer)ll.getTag()) {
			@Override
			public void delete() {
				// TODO Auto-generated method stub
				DBHelper dbHelper = new DBHelper(HisaabActivity.this);
				dbHelper.deleteTrip(_id);
				HisaabActivity.this.refreshSummary();

			}
		};
		TextView title = (TextView) ll.getChildAt(0);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete the trip "+title.getText()+" ?")
				.setPositiveButton("Yes", myDialog)
				.setNegativeButton("No", myDialog).show();
	}



	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			LinearLayout targetLl = (LinearLayout) info.targetView;
			TextView tv = (TextView) targetLl.getChildAt(0);
			menu.setHeaderTitle(tv.getText().toString());
			for (int i = 0; i < CONTEXT_MENU_ITEMS.length; i++) {
				menu.add(Menu.NONE, i, i, CONTEXT_MENU_ITEMS[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		switch (menuItemIndex) {
		case 0:
			openTrip(info.targetView);
			break;
		case 1:
			editTrip(info.targetView);
			break;
		case 2:
			deleteTrip(info.targetView);

		}
		// Toast.makeText(this,
		// "item.getId(): "+menuItemIndex+", info.position: "+info.position,
		// Toast.LENGTH_LONG).show();
		// String menuItemName = menuItems[menuItemIndex];
		// String listItemName = Countries[info.position];
		// TextView text = (TextView) findViewById(R.id.footer);
		// text.setText(String.format("Selected %s for item %s", menuItemName,
		// listItemName));
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == NEW_TRIP||requestCode==EDIT_TRIP) {
			if (resultCode == Activity.RESULT_OK) {
				refreshSummary();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void refreshSummary() {
		// TODO Auto-generated method stub
		ListView l = getListView();
		DBHelper dbHelper = new DBHelper(this);

		Cursor summaryCur = dbHelper.getAllSummary();
		// l.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_expandable_list_item_1, new
		// String[]{"r","k"}));
		l.setAdapter(new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, summaryCur, new String[] {
						"name", "description" }, new int[] {
						android.R.id.text1, android.R.id.text2 }) {
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				Cursor c = this.getCursor();
				int oldPos = c.getPosition();
				c.moveToPosition(position);
				v.setTag(c.getInt(0));
				c.moveToPosition(oldPos);
				return v;
			};
		});
		dbHelper.close();
	}
}