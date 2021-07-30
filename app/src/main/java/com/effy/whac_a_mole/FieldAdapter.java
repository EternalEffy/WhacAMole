package com.effy.whac_a_mole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class FieldAdapter extends BaseAdapter {

    private Context сontext;
    private List<Integer> holes;
    private Integer hole;

    public FieldAdapter(Context c, List<Integer> holes) {
        сontext = c;
        this.holes = holes;
    }

    @Override
    public int getCount() {
        return holes.size();
    }

    @Override
    public Object getItem(int position) {
        return holes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        hole = holes.get(position);

        if (convertView == null) {
            grid = new View(сontext);
            LayoutInflater inflater = (LayoutInflater) сontext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.grid_item, parent, false);
        } else {
            grid = (View) convertView;
        }
        ImageView imageView = (ImageView) grid.findViewById(R.id.hole);
        imageView.setImageResource((hole==1)?R.drawable.mole_in_hole:R.drawable.hole);
        return grid;
    }
}
