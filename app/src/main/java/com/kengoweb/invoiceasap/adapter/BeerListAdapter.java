package com.kengoweb.invoiceasap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kengoweb.invoiceasap.R;
import com.kengoweb.invoiceasap.model.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */

public class BeerListAdapter extends ArrayAdapter<Beer> {

    private List<Beer> listBeer = new ArrayList<>();
    private Context context;

    class ViewHolder {
        TextView textViewName;
        TextView textViewType;
    }

    public BeerListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.listBeer = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Beer beer = listBeer.get(position);

        view = LayoutInflater.from(context).inflate(R.layout.beer_list_row, parent, false);

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) view.findViewById(R.id.textViewName);
            viewHolder.textViewType = (TextView) view.findViewById(R.id.textViewType);
            view.setTag(viewHolder);
        }

        viewHolder.textViewName.setText(beer.getName());
        viewHolder.textViewType.setText(beer.getType());

        return view;
    }
}
