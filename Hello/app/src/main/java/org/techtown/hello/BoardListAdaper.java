package org.techtown.hello;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BoardListAdaper extends BaseAdapter {
    private Context context;
    private List<Notice> noticeList;

    BoardListAdaper(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    public BoardListAdaper(){}

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.notice_board, null);
        TextView titleText = (TextView) v.findViewById(R.id.titleText);
        TextView contentText = (TextView) v.findViewById(R.id.contentText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        titleText.setText(noticeList.get(i).getContent());
        contentText.setText(noticeList.get(i).getTitle());
        dateText.setText(noticeList.get(i).getDate());

        v.setTag(noticeList.get(i).getContent());
        return v;
    }
}
