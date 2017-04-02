package com.wgl.android.library.viewholder;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Ganlin.Wu on 2016/9/22.
 */
public class ViewHolder {
    public static <T> T get(View view, int id) {
        View target;
        SparseArray<View> views = (SparseArray<View>) view.getTag();
        if (views == null) {
            views = new SparseArray<>();
            view.setTag(views);
        }
        target = views.get(id);
        if (target == null) {
            target = view.findViewById(id);
            views.put(id, target);
        }
        return (T) target;
    }

    /*
	 * 在getview里面的用法
	 *
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) {
	 *
	 * if (convertView == null) { convertView = LayoutInflater.from(context)
	 * .inflate(R.layout.banana_phone, parent, false); }
	 *
	 * ImageView bananaView = ViewHolder.get(convertView, R.id.banana); TextView
	 * phoneView = ViewHolder.get(convertView, R.id.phone);
	 *
	 * BananaPhone bananaPhone = getItem(position);
	 * phoneView.setText(bananaPhone.getPhone());
	 * bananaView.setImageResource(bananaPhone.getBanana());
	 *
	 * return convertView; }
	 */
}
