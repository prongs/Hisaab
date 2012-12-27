package pack.pack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TripActivity extends Activity implements OnClickListener {
	int trip_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtrip);
		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(this);
		Intent intent = getIntent();
		trip_id = intent.getIntExtra("trip_id", -1);
		int requestCode = intent.getIntExtra("requestCode",
				HisaabActivity.NEW_TRIP);
		if (requestCode == HisaabActivity.EDIT_TRIP) {
			TextView t1 = (TextView) findViewById(R.id.name_trip);
			TextView t2 = (TextView) findViewById(R.id.description_trip);
			t1.setText(intent.getStringExtra("name"));
			t2.setText(intent.getStringExtra("description"));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DBHelper dbHelper = new DBHelper(this);
		TextView t1 = (TextView) findViewById(R.id.name_trip);
		TextView t2 = (TextView) findViewById(R.id.description_trip);
		dbHelper.insertOrupdateTrip(trip_id, t1.getText().toString(), t2
				.getText().toString());
		dbHelper.close();
		// Intent i=getIntent();
		setResult(Activity.RESULT_OK);
		finish();
	}
}
