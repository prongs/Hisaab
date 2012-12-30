package pack.pack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	public Context context;
	public static final int VERSION = 1;
	public static final String DB_NAME = "hisaabDB";
	public static final String TABLE_SUMMARY = "summary";
	public static final String TABLE_EVENT = "event";
	public static final String TABLE_PEOPLE = "people";
	public static final String TABLE_EVENT_PEOPLE = "event_people";
	public static final String TABLE_MONEY = "money";
	// public static final String TABLE_
	// public static final String TABLE_
	public static final String TABLE_SUMMARY_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT";
	public static final String TABLE_EVENT_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, trip_id INTEGER NOT NULL, total_share INTEGER, total_spent INTEGER, FOREIGN KEY(trip_id) REFERENCES "
			+ TABLE_SUMMARY + "(_id)";
	// public static final String TABLE_EVENT_PEOPLE_SIG =
	// "event_id INTEGER NOT NULL, people_id TEXT NOT NULL, FOREIGN KEY(event_id) REFERENCES "
	// + TABLE_EVENT
	// + "(_id)";
	// public static final String TABLE_PEOPLE_SIG =
	// "_id TEXT PRIMARY KEY, name TEXT";
	public static final String TABLE_MONEY_SIG = "_id INTEGER PRIMARY KEY AUTOINCREMENT, people_id TEXT NOT NULL, share INTEGER, spent INTEGER, event_id INTEGER NOT NULL, trip_id INTEGER NOT NULL, FOREIGN KEY(event_id) REFERENCES "
			+ TABLE_EVENT
			+ "(_id), FOREIGN KEY (trip_id) REFERENCES "
			+ TABLE_SUMMARY + "(_id)";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE_SUMMARY + "(" + TABLE_SUMMARY_SIG
				+ ")");
		db.execSQL("CREATE TABLE " + TABLE_EVENT + "(" + TABLE_EVENT_SIG + ")");
		db.execSQL("CREATE TABLE " + TABLE_MONEY + "(" + TABLE_MONEY_SIG + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUMMARY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONEY);
		onCreate(db);
	}

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	Cursor getAllSummary() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_SUMMARY
				+ " ORDER BY _id ASC", new String[] {});
		return cur;
	}


	void insertOrupdateTrip(int trip_id, String name, String description) {
		Log.d("DBHelper","insertTrip: "+trip_id+", "+name+", "+description);
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("description", description);
		if (trip_id == -1) {
			db.insert(TABLE_SUMMARY, "", cv);
		} else {
			db.update(TABLE_SUMMARY, cv, "_id=?", new String[] { trip_id + "" });
		}
		db.close();
	}

	void deleteTrip(int trip_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_SUMMARY, "_id=?", new String[] { trip_id + "" });
		db.delete(TABLE_EVENT, "trip_id=?", new String[] { trip_id + "" });
		db.delete(TABLE_MONEY, "trip_id=?", new String[] { trip_id + "" });
		db.close();
	}

	void deleteEvent(int event_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_EVENT, "_id=?", new String[] { event_id + "" });
		db.delete(TABLE_MONEY, "event_id=?", new String[] { event_id + "" });
		db.close();
	}

	long insertOrUpdateEvent(int tripId, int eventId, String name,
			String description, int total_share, int total_spent) {
		long ret = eventId;
		Toast.makeText(context,
				"insert event: " + tripId + ", " + name + ", " + description,
				Toast.LENGTH_LONG).show();
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("description", description);
		cv.put("trip_id", tripId);
		cv.put("total_spent", total_spent);
		cv.put("total_share", total_share);
		if (eventId == -1) {
			ret = db.insert(TABLE_EVENT, "", cv);
		} else {
			db.update(TABLE_EVENT, cv, "_id=?", new String[] { eventId + "" });
		}
		db.close();
		return ret;
	}

	Cursor getTripById(int id) {
//		Toast.makeText(context, "get trip by id: " + id, Toast.LENGTH_LONG)
//				.show();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_EVENT
				+ " WHERE trip_id=? ORDER BY _id ASC",
				new String[] { (id) + "" });
		//db.close();
		return cur;
	}

	Cursor getMoneyList(int trip_id, int event_id) {
		SQLiteDatabase db = getReadableDatabase();
//		Toast.makeText(context, "inside getmoney list, id= " + id,
//				Toast.LENGTH_SHORT).show();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_MONEY
				+ " WHERE trip_id=? ORDER BY _id ASC", new String[]{trip_id+""});
//		db.close();
//		Toast.makeText(
//				context,
//				"getmoneylist returning with a cursor of length "
//						+ c.getCount(), Toast.LENGTH_SHORT).show();
		return c;
	}

	void updatePersonInfo(PersonInfo p, int event_id) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("people_id", p.getNewLookupUri().toString());
		cv.put("share", p.getNewShare());
		cv.put("spent", p.getNewSpent());
		db.update(TABLE_MONEY, cv, "event_id=? and _id=?", new String[] {
				event_id + "", p._id + "" });
	}

	void insertPersonInfo(PersonInfo p, int event_id, int trip_id) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("event_id", event_id);
		cv.put("trip_id", trip_id);
		cv.put("people_id", p.getNewLookupUri().toString());
		cv.put("share", p.getNewShare());
		cv.put("spent", p.getNewSpent());
		if(p.getNewShare()>0 && p.getNewSpent()>0)
			db.insert(TABLE_MONEY, "", cv);
	}
	Cursor getTripSummary(int trip_id)
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_MONEY, new String[]{"people_id", "sum(share)","sum(spent)"}, "trip_id=?", new String[]{trip_id+""}, "people_id", null, null);
		return c;
	}
	public static String getNameFromLookupUri(Context ctx, Uri lookupUri) {
		Cursor c = ctx.getContentResolver().query(
				lookupUri,
				new String[] { Contacts._ID, Contacts.DISPLAY_NAME }, null,
				null, null);
		c.moveToFirst();
		String s = c.getString(1);
		c.close();
		return s;

	}
	public static String getNameFromLookupUri(Context ctx, String lookupUri) {
		return getNameFromLookupUri(ctx, Uri.parse(lookupUri));
	}


}
