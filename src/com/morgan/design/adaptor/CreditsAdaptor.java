package com.morgan.design.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morgan.design.R;
import com.morgan.design.helpers.Credit;

import java.util.List;

public class CreditsAdaptor extends ArrayAdapter<Credit> {

    private final Context context;
    private final List<Credit> credits;

    public CreditsAdaptor(Context context, int resource, List<Credit> credits) {
        super(context, resource, credits);
        this.context = context;
        this.credits = credits;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.credits_adaptor_row, null);
        }

        final Credit credit = credits.get(position);

        ViewHolder holder = new ViewHolder();

        holder.title = (TextView) view.findViewById(R.id.title);
        holder.title.setText(credit.getTitle());

        holder.caption = (TextView) view.findViewById(R.id.caption);
        holder.caption.setText(credit.getCaption());

        holder.logo = (ImageView) view.findViewById(R.id.logo);
        if (0 != credit.getLogo()) {
            holder.logo.setImageResource(credit.getLogo());
        }

        return view;
    }

    class ViewHolder {
        TextView title;
        TextView caption;
        ImageView logo;
    }

}
