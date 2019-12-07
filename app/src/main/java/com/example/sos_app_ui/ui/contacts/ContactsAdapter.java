package com.example.sos_app_ui.ui.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sos_app_ui.R;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {
    Context mContext;
    List<AndroidContactModel> mList_Android_Contacts;

    public ContactsAdapter(Context mContext, List<AndroidContactModel> mContact) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
    }

    @Override
    public int getCount() {
        return mList_Android_Contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mList_Android_Contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(mContext, R.layout.contactlist_items,null);
        TextView textview_contact_Name= (TextView) view.findViewById(R.id.textview_android_contact_name);
        TextView textview_contact__phone_number= (TextView) view.findViewById(R.id.textview_android_contact_phoneNr);

        textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
        textview_contact__phone_number.setText(mList_Android_Contacts.get(position).android_contact_phone_number);

        view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
        return view;
    }
}
