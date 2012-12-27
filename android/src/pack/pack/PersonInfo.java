package pack.pack;

import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonInfo {
	Uri lookupUri;
	int _id;
	String name="";
	int share;
	int spent;
	LinearLayout ll;
	public PersonInfo() {
		// TODO Auto-generated constructor stub
	}
	PersonInfo(LinearLayout ll)
	{
		this.ll=ll;
	}
	boolean isModified()
	{
		TextView tv_name = (TextView)ll.getChildAt(0);
		EditText et_share = (EditText)ll.getChildAt(1);
		EditText et_spent = (EditText)ll.getChildAt(2);
		Log.d("PersonInfo", "checking modified");
		Log.d("PersonInfo", "name= "+name);
		Log.d("PersonInfo", "tv_name text: "+tv_name.getText());
		Log.d("PersonInfo", "share = "+share);
		Log.d("PersonInfo", "et_share text: "+Integer.parseInt(et_share.getText().toString()));
		Log.d("PersonInfo", "spent = "+spent);
		Log.d("PersonInfo", "et_spent text: "+Integer.parseInt(et_spent.getText().toString()));
		return !(name==tv_name.getText().toString()&&share==Integer.parseInt(et_share.getText().toString())&&spent==Integer.parseInt(et_spent.getText().toString()));
	}
	boolean isModified(LinearLayout ll)
	{
		this.ll=ll;
		return isModified();
	}
	int getNewShare()
	{
		EditText share = (EditText)ll.getChildAt(1);
		return Integer.parseInt(share.getText().toString());
	}
	int getNewSpent()
	{
		EditText spent = (EditText)ll.getChildAt(2);
		return Integer.parseInt(spent.getText().toString());
	}
	Uri getNewLookupUri()
	{
		EditText share = (EditText)ll.getChildAt(1);
		return (Uri)share.getTag();
	}
}
