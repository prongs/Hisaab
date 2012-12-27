package pack.pack;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends ListActivity implements OnClickListener {
	int event_id, trip_id, clickedPos;
	public static final int NEW_CONTACT = 1;
	public static final int EDIT_CONTACT = 2;
	public ArrayList<PersonInfo> unsavedPeopleList;
	PersonInfo[] peopleArray;
	boolean isNew = true;
	int total_share = 0, total_spent = 0;
	PeopleListAdapter peopleListAdapter;

	private class PeopleListAdapter extends BaseAdapter implements
			OnClickListener, TextWatcher {
		private LayoutInflater mInflater;
		public Cursor cursor;
		PersonInfo[] peopleArray;
		public ArrayList<PersonInfo> unsavedPeopleList;

		public PeopleListAdapter(Context context, Cursor cursor,
				PersonInfo[] pArray, ArrayList<PersonInfo> unsavedList) {
			mInflater = LayoutInflater.from(context);
			this.cursor = cursor;
			this.peopleArray = pArray;
			this.unsavedPeopleList = unsavedList;
		}

		public int getCount() {
			int count = peopleArray.length + unsavedPeopleList.size();
			Toast.makeText(EventActivity.this, "getcount returning " + count,
					Toast.LENGTH_SHORT).show();
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_person_info,
						null);
				PersonInfo p = getPersonInfo(position);
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.textview_name);
				holder.name.setText(p.name);
				holder.name.setTag(position);
				holder.name.setOnClickListener(this);
				holder.share = (EditText) convertView
						.findViewById(R.id.edittext_share);
				if (p.share > 0||position<peopleArray.length)
					holder.share.setText(p.share + "");
				holder.share.setTag(p.lookupUri);
				holder.spent = (EditText) convertView
						.findViewById(R.id.edittext_spent);
				if (p.spent > 0||position<peopleArray.length)
					holder.spent.setText(p.spent + "");
				// holder.spent.setTag(new int[] { position, 1 });
				convertView.setTag(holder);
				// holder.share
				// .addTextChangedListener(new MyTextWatcher(holder.share));
				// holder.share.setTag(p.lookupUri);
				// holder.spent.addTextChangedListener(this);
				// holder.spent
				// .addTextChangedListener(new MyTextWatcher(holder.spent));

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		PersonInfo getPersonInfo(int position) {
			Toast.makeText(EventActivity.this,
					"getPersonInfo for position=" + position,
					Toast.LENGTH_SHORT).show();
			if (position < peopleArray.length) {
				if (peopleArray[position] == null) {
					peopleArray[position] = new PersonInfo(
							(LinearLayout) EventActivity.this.getListView()
									.getChildAt(position));
					cursor.moveToPosition(position);
					peopleArray[position].lookupUri = Uri.parse(cursor
							.getString(1));
					Cursor c = EventActivity.this.getContentResolver()
							.query(peopleArray[position].lookupUri,
									new String[] { Contacts._ID,
											Contacts.DISPLAY_NAME }, null,
									null, null);
					c.moveToFirst();
					peopleArray[position].share = cursor.getInt(2);
					peopleArray[position].spent = cursor.getInt(3);
					peopleArray[position]._id = cursor.getInt(0);
					peopleArray[position].name = c.getString(1);
				}

				return peopleArray[position];
			} else {
				return unsavedPeopleList.get(position - peopleArray.length);
			}
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int position = (Integer) arg0.getTag();
			TextView tv = (TextView) arg0;
			// getPersonInfo(position).tv = tv;
			Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			clickedPos = position;
			startActivityForResult(i, EDIT_CONTACT);

		}

		class ViewHolder {
			TextView name;
			EditText share;
			EditText spent;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newevent);
		Intent i = getIntent();
		trip_id = i.getIntExtra("trip_id", -1);
		event_id = i.getIntExtra("event_id", -1);
		Toast.makeText(this, "event_id: " + event_id + ", trip_id: " + trip_id,
				Toast.LENGTH_LONG).show();
		if (i.getIntExtra("requestCode", -1) == OneTripActivity.EDIT_EVENT) {
			EditText et1 = (EditText) findViewById(R.id.name_event);
			EditText et2 = (EditText) findViewById(R.id.description_event);
			et1.setText(i.getStringExtra("name"));
			et2.setText(i.getStringExtra("description"));
			isNew = false;
		}
		Button b = (Button) findViewById(R.id.button_done);
		Button divide_equal = (Button) findViewById(R.id.button_equal_share);
		divide_equal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListView lv = EventActivity.this.getListView();
				int[] shares;
				int total_share = 0, total_spent = 0;
				int no_children = lv.getChildCount();
				shares = new int[no_children];
				for (int i = 0; i < no_children; i++) {
					LinearLayout ll = (LinearLayout) lv.getChildAt(i);
					EditText et_spent = (EditText) ll.getChildAt(2);
					String s = et_spent.getText().toString().trim();
					int j=0;
					if (!s.isEmpty())
						j=Integer.parseInt(s);
					total_spent += j;
				}
				int eq_share = total_spent / no_children;
				int remaining = total_spent % no_children;
				for (int i = 0; i < no_children; i++) {
					shares[i] = eq_share;
				}
				Random r = new Random(System.currentTimeMillis());
				while (remaining > 0) {
					int pos=r.nextInt(no_children);
					shares[pos]+=1;
					remaining--;
				}
				for (int i = 0; i < no_children; i++) {
					LinearLayout ll = (LinearLayout) lv.getChildAt(i);
					EditText et_share = (EditText) ll.getChildAt(1);
					et_share.setText(shares[i]+"");
				}
				
			}
		});
		Button button_verify_sum = (Button) findViewById(R.id.button_verify_sum);
		button_verify_sum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventActivity.this.verifySum();
			}
		});
		ListView lv = getListView();
		b.setOnClickListener(this);
		DBHelper dbHelper = new DBHelper(this);
		unsavedPeopleList = new ArrayList<PersonInfo>();
		Cursor c = dbHelper.getMoneyList(event_id);
		peopleArray = new PersonInfo[c.getCount()];
		Toast.makeText(
				EventActivity.this,
				"cursor count=" + c.getCount() + ", list length = "
						+ peopleArray.length, Toast.LENGTH_SHORT).show();
		peopleListAdapter=new PeopleListAdapter(this, c, peopleArray,
				unsavedPeopleList);
		setListAdapter(peopleListAdapter);
		Button button_add_person = (Button) findViewById(R.id.button_add_person);
		button_add_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PersonInfo p = new PersonInfo();
				Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				startActivityForResult(i, NEW_CONTACT);
			}
		});
	}

	void verifySum() {
		int sum_share = 0, sum_spent = 0;
		ListView lv = getListView();
		int child_count = lv.getChildCount();
		for (int i = 0; i < child_count; i++) {
			LinearLayout ll = (LinearLayout) lv.getChildAt(i);
			EditText tv_share = (EditText) ll.getChildAt(1);
			EditText tv_spent = (EditText) ll.getChildAt(2);
			sum_share += Integer.parseInt(tv_share.getText().toString());
			sum_spent += Integer.parseInt(tv_spent.getText().toString());
		}
		total_share = sum_share;
		total_spent = sum_spent;
		TextView sum_share_view = (TextView) findViewById(R.id.tv_total_share);
		TextView sum_spent_view = (TextView) findViewById(R.id.tv_total_spent);
		sum_share_view.setText("share= " + sum_share);
		sum_spent_view.setText("spent= " + sum_spent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		verifySum();
		DBHelper dbHelper = new DBHelper(this);
		TextView t1 = (TextView) findViewById(R.id.name_event);
		TextView t2 = (TextView) findViewById(R.id.description_event);
		Toast.makeText(this, "Event_id before: " + event_id, Toast.LENGTH_LONG)
				.show();
		event_id = (int) dbHelper.insertOrUpdateEvent(trip_id, event_id, t1
				.getText().toString(), t2.getText().toString(), total_share,
				total_spent);
		Toast.makeText(this, "Starting saving money list, eventid = "+event_id, Toast.LENGTH_SHORT)
				.show();
		for (int i = 0; i < peopleArray.length; i++) {
			Toast.makeText(this, "i=" + i, Toast.LENGTH_SHORT).show();
			PersonInfo p = peopleArray[i];
			if (p.isModified((LinearLayout) getListView().getChildAt(i))) {
				Toast.makeText(this, "modified true", Toast.LENGTH_LONG).show();
				dbHelper.updatePersonInfo(p, event_id);
			}
		}
		Toast.makeText(this, "unsaved starging", Toast.LENGTH_SHORT).show();
		for (int i=0;i<unsavedPeopleList.size();i++) {
			Toast.makeText(this, "one of the unsaved", Toast.LENGTH_SHORT)
					.show();
			PersonInfo p = unsavedPeopleList.get(i);
			p.ll=(LinearLayout)getListView().getChildAt(peopleArray.length+i);
			dbHelper.insertPersonInfo(p, event_id, trip_id);
		}
		dbHelper.close();
		// Intent i=getIntent();
		setResult(Activity.RESULT_OK);
		finish();
	}

	PersonInfo getPerson(int position) {
		if (position < peopleArray.length)
			return peopleArray[position];
		return unsavedPeopleList.get(position - peopleArray.length);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NEW_CONTACT && resultCode == Activity.RESULT_OK) {
			PersonInfo p = new PersonInfo();
			Uri u = data.getData();
			Toast.makeText(this, u.toString(), Toast.LENGTH_LONG).show();
			Log.d("NewEventActivity", u.toString());
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(u, new String[] { Contacts._ID,
					Contacts.DISPLAY_NAME }, null, null, null);
			if (cursor.moveToFirst()) {
				p.lookupUri = u;
				p.name = cursor.getString(1);
				unsavedPeopleList.add(p);
				getListView().refreshDrawableState();
				peopleListAdapter.notifyDataSetChanged();
				cursor.close();
			}

		} else if (requestCode == EDIT_CONTACT && resultCode == RESULT_OK) {
			int position;
			// position = data.getIntExtra("position", -1);
			position = clickedPos;
			Toast.makeText(this, "hi,position=:" + position, Toast.LENGTH_SHORT)
					.show();
			if (position != -1) {
				LinearLayout ll = (LinearLayout) getListView().getChildAt(
						position);
				TextView name = (TextView) ll.getChildAt(0);
				EditText share = (EditText) ll.getChildAt(1);
				Uri u = data.getData();
				ContentResolver resolver = getContentResolver();
				Cursor cursor = resolver.query(u, new String[] { Contacts._ID,
						Contacts.DISPLAY_NAME }, null, null, null);
				if (cursor.moveToFirst()) {
					// p.name = cursor.getString(1);
					name.setText(cursor.getString(1));
					name.setTag(position);
					share.setTag(u);
					// p.modified = true;
					// p.tv.setText(p.name);
					getListView().refreshDrawableState();
					peopleListAdapter.notifyDataSetChanged();
					cursor.close();
				}

			}

		}
	}
}
