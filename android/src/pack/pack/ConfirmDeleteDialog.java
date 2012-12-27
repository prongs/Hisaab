package pack.pack;

import android.content.DialogInterface;

public abstract class ConfirmDeleteDialog implements DialogInterface.OnClickListener {
	int _id;
	String title;

	public ConfirmDeleteDialog(int id) {
		_id = id;
	}
	public abstract void delete();

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			// Yes button clicked
			delete();
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			// No button clicked
			break;
		}
	}
}