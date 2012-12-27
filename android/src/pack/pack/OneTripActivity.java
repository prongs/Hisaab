package pack.pack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class OneTripActivity extends ListActivity implements OnClickListener,
		OnItemClickListener {
	public int trip_id;
	public static final int NEW_EVENT = 1;
	public static final int EDIT_EVENT = 2;
	public static final String[] CONTEXT_MENU_ITEMS = new String[] { "Open",
			"Delete" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetrip);
		Intent i = getIntent();
		trip_id = i.getIntExtra("trip_id", -1);
		refreshSummary();
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(this);
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		intent.putExtra("requestCode", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == NEW_EVENT) {
			if (resultCode == Activity.RESULT_OK) {
				refreshSummary();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(this, EventActivity.class);
		myIntent.putExtra("trip_id", trip_id);
		startActivityForResult(myIntent, NEW_EVENT);

	}
	public void openEvent(View arg1)
	{
		
		Intent myIntent = new Intent(this, EventActivity.class);
		myIntent.putExtra("trip_id", trip_id);
		int event_id = (Integer) arg1.getTag();
		myIntent.putExtra("event_id", event_id);
		LinearLayout ll = (LinearLayout) arg1;
		TextView name = (TextView) ll.getChildAt(0);
		TextView desc = (TextView) ll.getChildAt(1);
		myIntent.putExtra("name", name.getText().toString());
		myIntent.putExtra("description", desc.getText().toString());
		startActivityForResult(myIntent, EDIT_EVENT);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		openEvent(arg1);
	}

	private void refreshSummary() {
		// TODO Auto-generated method stub
		ListView l = getListView();
		DBHelper dbHelper = new DBHelper(this);
		Cursor summaryCur = dbHelper.getTripById(trip_id);
		// l.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_expandable_list_item_1, new
		// String[]{"r","k"}));
		l.setAdapter(new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, summaryCur, new String[] {
						"name", "description" }, new int[] {
						android.R.id.text1, android.R.id.text2 }) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				Cursor c = getCursor();
				int p = c.getPosition();
				c.moveToPosition(position);
				v.setTag(c.getInt(0));
				c.moveToPosition(p);
				return v;

			};
		});
		dbHelper.close();
	}

	private void deleteEvent(View clicked) {
		// TODO Auto-generated method stub
		LinearLayout ll = (LinearLayout) clicked;
		ConfirmDeleteDialog myDialog = new ConfirmDeleteDialog(
				(Integer) ll.getTag()) {
			@Override
			public void delete() {
				// TODO Auto-generated method stub
				DBHelper dbHelper = new DBHelper(OneTripActivity.this);
				dbHelper.deleteEvent(_id);
				OneTripActivity.this.refreshSummary();

			}
		};
		TextView title = (TextView) ll.getChildAt(0);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Are you sure you want to delete the Event " + title.getText()
						+ " ?").setPositiveButton("Yes", myDialog)
				.setNegativeButton("No", myDialog).show();

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
		else
		{
			menu.add(Menu.NONE, 0,0, "Test");
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
			openEvent(info.targetView);
			break;
		case 1:
			deleteEvent(info.targetView);
			break;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater  inflater = getMenuInflater();
		inflater.inflate(R.menu.tripmenu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent i;
		switch(item.getItemId())
		{
		case R.id.distribute:
			i = new Intent(this, TripDistributeActivity.class);
//			Intent i = new Intent(this,
			break;
		case R.id.summary:
		default:
			i = new Intent(this, TripSummaryActivity.class);
			break;
		}
		i.putExtra("trip_id", trip_id);
		startActivity(i);
		return true;
	}

}
