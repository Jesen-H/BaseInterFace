package net.hycollege.myproject.tools;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hycollege.myproject.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class Adapter extends BaseAdapter {

    Context context;
    List<Product> list;
    LayoutInflater inflater;

    public Adapter(Context context, List<Product> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = (TextView) view.findViewById(R.id.tv_id);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_id.setText(list.get(i).getId() + "");
        viewHolder.tv_name.setText(list.get(i).getName());
        viewHolder.tv_price.setText(list.get(i).getPrice() + "");
        return view;
    }

    class ViewHolder {
        TextView tv_id;
        TextView tv_name;
        TextView tv_price;
    }
}
