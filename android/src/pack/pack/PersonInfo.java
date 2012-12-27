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
	Integer share;
	Integer spent;
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
		if (et_share.getText().toString().isEmpty()||et_spent.getText().toString().isEmpty())
			return false;
		return !(name==tv_name.getText().toString()&&share==((et_share.getText().toString().trim().isEmpty())?0:Integer.parseInt(et_share.getText().toString()))&&spent==((et_spent.getText().toString().isEmpty())?0:Integer.parseInt(et_spent.getText().toString())));
	}
	boolean isModified(LinearLayout ll)
	{
		this.ll=ll;
		return isModified();
	}
	int getNewShare()
	{
		EditText share = (EditText)ll.getChildAt(1);
		if (share.getText().toString().trim().isEmpty())
			return 0;
		return Integer.parseInt(share.getText().toString());
	}
	int getNewSpent()
	{
		EditText spent = (EditText)ll.getChildAt(2);
		if (spent.getText().toString().trim().isEmpty())
			return 0;
		return Integer.parseInt(spent.getText().toString());
	}
	Uri getNewLookupUri()
	{
		EditText share = (EditText)ll.getChildAt(1);
		return (Uri)share.getTag();
	}
}
