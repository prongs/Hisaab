package hisaab.hisaab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;
			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}
		final CharSequence[] items = { "Red", "Green", "Blue" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a color");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), items[item],
						Toast.LENGTH_SHORT).show();
			}
		});
		setContentView(R.layout.new_layout);
		
		AlertDialog alert = builder.create();
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

	 wmlp.gravity = Gravity.TOP | Gravity.LEFT;
	 wmlp.x = 00;   //x position
	 wmlp.y = 00;   //y position
	 alert.show();
	 
	 ListView lv = (ListView)findViewById(R.id.listView1);
//	 lv.setAdapter(new ListAdapter() {
//		
//		@Override
//		public void unregisterDataSetObserver(DataSetObserver observer) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void registerDataSetObserver(DataSetObserver observer) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public boolean isEmpty() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		
//		@Override
//		public boolean hasStableIds() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		
//		@Override
//		public int getViewTypeCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public int getItemViewType(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		
//		@Override
//		public boolean isEnabled(int position) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		
//		@Override
//		public boolean areAllItemsEnabled() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//	});
			 
	 
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
