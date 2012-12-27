package pack.pack;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class TripSummaryActivity extends ListActivity {
	int trip_id;
	Cursor moneyListCursor;
	int total_spent=0, total_share=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		trip_id = i.getIntExtra("trip_id", -1);
		DBHelper db = new DBHelper(this);
		moneyListCursor = db.getTripSummary(trip_id);
		while(moneyListCursor.moveToNext())
		{
			total_share+=moneyListCursor.getInt(1);
			total_spent+=moneyListCursor.getInt(2);
		}
		setContentView(R.layout.tripsummary);
		((TextView)findViewById(R.id.textview_total_summary)).setText("share = "+total_share+", spent = "+total_spent);
		setListAdapter(new MoneyListAdapter(moneyListCursor, this));
	}

	private class MoneyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public Cursor cursor;
		PersonInfo[] peopleArray;
		public ArrayList<PersonInfo> unsavedPeopleList;

		public MoneyListAdapter(Cursor cursor, Context context) {
			mInflater = LayoutInflater.from(context);
			this.cursor = cursor;
		}

		public int getCount() {
			return cursor.getCount();
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
				convertView = mInflater.inflate(
						R.layout.list_item_person_summary, null);
				cursor.moveToPosition(position);
				String personLookupUri = cursor.getString(0);
				String personName = DBHelper.getNameFromLookupUri(TripSummaryActivity.this, Uri
						.parse(personLookupUri));
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.textview_name);
				holder.name.setText(personName);
				holder.name.setTag(position);
				holder.summary = (TextView) convertView
						.findViewById(R.id.textview_summary);
				int total_share = cursor.getInt(1);
				int total_spent = cursor.getInt(2);
				String s;
				if (total_share == total_spent) {
					s = "is Neutral";
				} else if (total_share > total_spent) {
					s = "pays " + (total_share - total_spent);
				} else {
					s = "gets " + (total_spent - total_share);
				}
				holder.summary.setText(s);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView summary;
		}
	}

}
