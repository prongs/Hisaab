package hisaab.hisaab;

import java.util.ArrayList;
import java.util.Calendar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class EventListAdapter extends BaseExpandableListAdapter {

	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return monthsToDisplayInTopLevel.size();
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class Month{
		int id;
		String name;
		int year;
		public Month(int year, int month) {
			// TODO Auto-generated constructor stub
			this.year = year;
			this.id = month;
			Calendar c = Calendar.getInstance();
			name = Utils.MONTH_NAMES[c.get(Calendar.MONTH)];
		}
		public Month prevMonth()
		{
			return (id == 0)? (new Month(year-1, 12)) : (new Month(year, id-1));
		}
		public Month prevMonth(int n)
		{
			return (id-n < 0) ? (new Month(year-1, (id-n)%12)) : (new Month(year, id-n));
		}
		public static Month currentMonth()
		{
			Calendar c = Calendar.getInstance();
			int cur_month_int = c.get(Calendar.MONTH); 
			return new Month(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
			
		}
		public int get_unique_id()
		{
			return year * 100 + id;
		}
	}
	ArrayList<Month> monthsToDisplayInTopLevel;
	public EventListAdapter() {
		// TODO Auto-generated constructor stub
		Month cur_month = Month.currentMonth();
		Month prev_month = cur_month.prevMonth();
		Month prev2_month = prev_month.prevMonth();
		monthsToDisplayInTopLevel = new ArrayList<EventListAdapter.Month>();
		monthsToDisplayInTopLevel.add(cur_month);
		monthsToDisplayInTopLevel.add(prev_month);
		monthsToDisplayInTopLevel.add(prev2_month);
		//inside getgroupview, fetch and expand. Till then show loading 
	}
}
