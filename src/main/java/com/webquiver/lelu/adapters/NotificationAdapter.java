package com.webquiver.lelu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webquiver.lelu.R;
import com.webquiver.lelu.classes.NotificationItem;
import java.util.ArrayList;

/**
 * Created by WebQuiver 04 on 10/21/2016.
 */

public class NotificationAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<NotificationItem> notifList;

    public NotificationAdapter(Activity context, ArrayList<NotificationItem> notifList) {

        this.notifList = notifList;
        this.context = context;


    }

    @Override
    public int getCount() {
        return notifList.size();
    }

    @Override
    public String getItem(int position) {
        return notifList.get(position).notifDate;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.notif_list_item_lyt, null, true);

        TextView textView_memoDate = (TextView) listViewItem.findViewById(R.id.textView_memoDate);
        TextView textView_memoSubject = (TextView) listViewItem.findViewById(R.id.textView_memoSubject);
        TextView textView_memoDesc = (TextView) listViewItem.findViewById(R.id.textView_memoDesc);

        NotificationItem meM = notifList.get(position);

        LinearLayout attendanceListBack = (LinearLayout) listViewItem.findViewById(R.id.memoList_back);

    /*    if (meM.memoColor ==1){
            attendanceListBack.setBackgroundResource(R.drawable.curve_back_black);
        }else if (meM.memoColor ==2) {
            attendanceListBack.setBackgroundResource(R.drawable.curve_back_black);
        }else{
            attendanceListBack.setBackgroundResource(R.drawable.curve_back_one_blue);
            }
*/

        textView_memoDate.setText(meM.notifDate);
        textView_memoSubject.setText(meM.notifSub);
        textView_memoDesc.setText(meM.notifDesc);


        return listViewItem;
    }
}