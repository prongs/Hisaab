//package pack.pack;
//
//import android.app.Activity;
//import android.app.ListActivity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.ContactsContract.Contacts;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//public class NewEventActivity extends ListActivity implements OnClickListener {
//	int event_id,trip_id, clickedPos;
//	public static final int NEW_CONTACT = 1;
//	public static final int EDIT_CONTACT = 2;
//	public ArrayList<PersonInfo> unsavedPeopleList;
//	public ArrayList<PersonInfo> peopleList;
//
//	class MyTextWatcher implements TextWatcher {
//		@Override
//		public void afterTextChanged(Editable arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//		};
//
//		@Override
//		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//			int[] tag = (int[]) editText.getTag();
//			int position=tag[0];
//			int spent=tag[1]; //1 if spent, 0 if share
//			PersonInfo p = getPerson(position);
//			if(spent==1)//spent
//			{
//				int newSpent=Integer.parseInt(editText.getText().toString());
//				if(p.spent!=newSpent)
//				{
//					p.spent=newSpent;
//				}
//			}
//			else if(spent==0)
//			{
//				int newShare=Integer.parseInt(editText.getText().toString());
//				if(p.share!=newShare)
//				{
//					p.share=newShare;
//				}
//			}
//		}
//
//		EditText editText;
//
//		public MyTextWatcher(EditText editText) {
//			// TODO Auto-generated constructor stub
//			this.editText = editText;
//		}
//	}
//
//	private class PeopleListAdapter extends BaseAdapter implements
//			OnClickListener, TextWatcher {
//		private LayoutInflater mInflater;
//		public Cursor cursor;
//		public ArrayList<PersonInfo> peopleList;
//		public ArrayList<PersonInfo> unsavedPeopleList;
//
//		public PeopleListAdapter(Context context, Cursor cursor) {
//			// Cache the LayoutInflate to avoid asking for a new one each time.
//			mInflater = LayoutInflater.from(context);
//			this.cursor = cursor;
//			peopleList = new ArrayList<PersonInfo>(cursor.getCount());
//		}
//
//		public PeopleListAdapter(Context context, Cursor cursor,
//				ArrayList<PersonInfo> peopleList,
//				ArrayList<PersonInfo> unsavedList) {
//			mInflater = LayoutInflater.from(context);
//			this.cursor = cursor;
//			this.peopleList = peopleList;
//			this.unsavedPeopleList = unsavedList;
//		}
//
//		public int getCount() {
//			return peopleList.size() + unsavedPeopleList.size();
//		}
//
//		public Object getItem(int position) {
//			return position;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.list_item_person_info,
//						null);
//				holder = new ViewHolder();
//				holder.name = (TextView) convertView
//						.findViewById(R.id.textview_name);
//				holder.name.setOnClickListener(this);
//				holder.share = (EditText) convertView
//						.findViewById(R.id.edittext_share);
//				holder.spent = (EditText) convertView
//						.findViewById(R.id.edittext_spent);
//				convertView.setTag(holder);
//
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			PersonInfo p = getPersonInfo(position);
//			holder.name.setText(p.name);
//			holder.name.setTag(position);
//			holder.share.setText(p.share + "");
//			holder.share
//					.addTextChangedListener(new MyTextWatcher(holder.share));
//			holder.share.setTag(new int[] { position, 0 });
//			// holder.share.setTag(p.lookupUri);
//			holder.spent.setText(p.spent + "");
//			holder.spent.addTextChangedListener(this);
//			holder.spent.setTag(new int[] { position, 1 });
//			holder.spent
//					.addTextChangedListener(new MyTextWatcher(holder.spent));
//			return convertView;
//		}
//
//		PersonInfo getPersonInfo(int position) {
//			if (position < peopleList.size()) {
//				PersonInfo pInfo = peopleList.get(position);
//				if (pInfo == null) {
//					pInfo = new PersonInfo();
//					cursor.moveToPosition(position);
//					pInfo.lookupUri = Uri.parse(cursor.getString(1));
//					Cursor c = NewEventActivity.this.getContentResolver()
//							.query(pInfo.lookupUri,
//									new String[] { Contacts._ID,
//											Contacts.DISPLAY_NAME }, null,
//									null, null);
//					pInfo.share = cursor.getInt(2);
//					pInfo.spent = cursor.getInt(3);
//					pInfo._id = cursor.getInt(0);
//					try {
//						if (c.moveToFirst()) {
//							pInfo.name = c.getString(1);
//						}
//					} finally {
//						cursor.close();
//					}
//					peopleList.set(position, pInfo);
//
//				}
//
//				return pInfo;
//			} else {
//				return unsavedPeopleList.get(position - peopleList.size());
//			}
//		}
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			int position = (Integer) arg0.getTag();
//			TextView tv = (TextView) arg0;
//			Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
//			i.putExtra("position", position);
//			clickedPos = position;
//			startActivityForResult(i, EDIT_CONTACT);
//
//		}
//
//		class ViewHolder {
//			TextView name;
//			EditText share;
//			EditText spent;
//		}
//
//		@Override
//		public void afterTextChanged(Editable arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.newevent);
//		Intent i = getIntent();
//		trip_id = i.getIntExtra("trip_id", -1);
//		event_id=i.getIntExtra("event_id", -1);
//		Button b = (Button) findViewById(R.id.button_done);
//		ListView lv = getListView();
//		b.setOnClickListener(this);
//		DBHelper dbHelper = new DBHelper(this);
//		unsavedPeopleList = new ArrayList<PersonInfo>();
//		Cursor c = dbHelper.getMoneyList(event_id);
//		peopleList = new ArrayList<PersonInfo>(c.getCount());
//		setListAdapter(new PeopleListAdapter(this, c, peopleList,
//				unsavedPeopleList));
//		Button button_add_person = (Button) findViewById(R.id.button_add_person);
//		button_add_person.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				PersonInfo p = new PersonInfo();
//				Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
//				startActivityForResult(i, NEW_CONTACT);
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		DBHelper dbHelper = new DBHelper(this);
//		TextView t1 = (TextView) findViewById(R.id.name_event);
//		TextView t2 = (TextView) findViewById(R.id.description_event);
//		dbHelper.insertOrUpdateEvent(trip_id, event_id, t1.getText().toString(), t2.getText()
//				.toString());
//		for(PersonInfo p: peopleList)
//		{
//			if( p.modified)
//			{
//				//dbHelper.updatePersonInfo(p,id);
//			}
//		}
//		for(PersonInfo p:unsavedPeopleList)
//		{
//			//dbHelper.insertPersonInfo(p,id);
//		}
//		dbHelper.close();
//		// Intent i=getIntent();
//		setResult(Activity.RESULT_OK);
//		finish();
//	}
//
//	PersonInfo getPerson(int position) {
//		if(position<peopleList.size())
//			return peopleList.get(position);
//		return unsavedPeopleList.get(position-peopleList.size());
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == NEW_CONTACT && resultCode == Activity.RESULT_OK) {
//			PersonInfo p = new PersonInfo();
//			Uri u = data.getData();
//			Toast.makeText(this, u.toString(), Toast.LENGTH_LONG).show();
//			Log.d("NewEventActivity", u.toString());
//			ContentResolver resolver = getContentResolver();
//			Cursor cursor = resolver.query(u, new String[] { Contacts._ID,
//					Contacts.DISPLAY_NAME }, null, null, null);
//			if (cursor.moveToFirst()) {
//				p.lookupUri = u;
//				p.name = cursor.getString(1);
//				unsavedPeopleList.add(p);
//				getListView().refreshDrawableState();
//				cursor.close();
//			}
//
//		} else if (requestCode == EDIT_CONTACT && resultCode == RESULT_OK) {
//			int position = data.getIntExtra("position", -1);
//			position = clickedPos;
//			Toast.makeText(this, "hi,position=:" + position, Toast.LENGTH_SHORT)
//					.show();
//			if (position != -1) {
//				Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//				PersonInfo p;
//				if (position < peopleList.size()) {
//					p = peopleList.get(position);
//				} else {
//					p = unsavedPeopleList.get(position - peopleList.size());
//				}
//				Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//				Uri u = data.getData();
//				Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//				Toast.makeText(this, u.toString(), Toast.LENGTH_LONG).show();
//				Log.d("NewEventActivity", u.toString());
//				Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//				ContentResolver resolver = getContentResolver();
//				Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//				Cursor cursor = resolver.query(u, new String[] { Contacts._ID,
//						Contacts.DISPLAY_NAME }, null, null, null);
//				if (cursor.moveToFirst()) {
//					p.lookupUri = u;
//					p.name = cursor.getString(1);
//					Toast.makeText(this, p.name, Toast.LENGTH_LONG).show();
//					p.modified = true;
//					p.tv.setText(p.name);
//					getListView().refreshDrawableState();
//					cursor.close();
//				}
//
//			}
//
//		}
//	}
//}
