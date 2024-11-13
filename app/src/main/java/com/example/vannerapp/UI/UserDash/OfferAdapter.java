package com.example.vannerapp.UI.UserDash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vannerapp.Model.Offer;
import com.example.vannerapp.R;

import java.util.List;

public class OfferAdapter extends ArrayAdapter<Offer> {

    private Context context;
    private List<Offer> offers;

    public OfferAdapter(Context context, List<Offer> offers) {
        super(context, 0, offers);
        this.context = context;
        this.offers = offers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false);
        }

        Offer offer = offers.get(position);

        TextView nameTextView = convertView.findViewById(R.id.offer_name);
        TextView companyTextView = convertView.findViewById(R.id.offer_company);
        TextView salaryTextView = convertView.findViewById(R.id.offer_salary);

        nameTextView.setText(offer.getName());
        companyTextView.setText(offer.getCompany());
        salaryTextView.setText(offer.getSalary());

        return convertView;
    }
}
