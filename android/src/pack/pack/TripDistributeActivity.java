package pack.pack;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TripDistributeActivity extends ListActivity {
	int trip_id,total,no_connections;
	Cursor summaryCursor;
	HashMap<String, Integer> unsortedSummaryMap;
	Distributor mDistributor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		trip_id = i.getIntExtra("trip_id", -1);
		DBHelper db = new DBHelper(this);
		summaryCursor = db.getTripSummary(trip_id);
		unsortedSummaryMap = getSummaryAsMap(summaryCursor);
		this.mDistributor = Utils.chooseDistributor(i.getIntExtra("distributor_id", -1), unsortedSummaryMap);
		HashMap<Utils.Couple, Integer> result = distribute();
		total=0;
		for (Utils.Couple key : result.keySet()) {
			total+=result.get(key);
		}
		no_connections=result.size();
		setContentView(R.layout.tripsummary);
		((TextView)findViewById(R.id.textview_total_summary)).setText("Money Transferred = "+total+" via "+no_connections+" channels");
		showDistribution(result);
	}
	HashMap<String, Integer> getSummaryAsMap(Cursor summary) {
		HashMap<String, Integer> hm = new HashMap<String, Integer>(
				summary.getCount());
		for (int i = 0; i < summary.getCount(); i++) {
			summary.moveToPosition(i);
			String lookupUri = summary.getString(0);
			int share = summary.getInt(1);
			int spent = summary.getInt(2);
			hm.put(DBHelper.getNameFromLookupUri(this, lookupUri), share
					- spent);
		}
		return hm;
	}
	void showDistribution(HashMap<Utils.Couple, Integer> result)
	{
		ArrayList<HashMap<String, String>> l = new ArrayList<HashMap<String, String>>(result.size());
		for(Utils.Couple cpl:result.keySet())
		{
			HashMap<String, String> h = new HashMap<String, String>(2);
			h.put("title", cpl.toString());
			h.put("subtitle", result.get(cpl)+"");
			l.add(h);
		}
		setListAdapter(new SimpleAdapter(this, l, android.R.layout.two_line_list_item, new String[]{"title", "subtitle"}, new int[]{android.R.id.text1, android.R.id.text2}));
	}


	public HashMap<Utils.Couple, Integer> distribute() {
		return distribute(mDistributor);
	}

	public HashMap<Utils.Couple, Integer> distribute(Distributor d) {
		return d.distribute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AlertDialog.Builder prefDialog=new AlertDialog.Builder(this).setCancelable(false).setTitle("Set Preferences").setMessage("Select").setView(LayoutInflater.from(this).inflate(R.layout.distribution_preferences, (ViewGroup)findViewById(R.id.distribution_preferences_root))).setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		prefDialog.create().show();
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("Add distribution Preferences");
		return true;
	}

}
