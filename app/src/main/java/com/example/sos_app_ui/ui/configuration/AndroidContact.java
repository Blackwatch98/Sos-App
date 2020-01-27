package com.example.sos_app_ui.ui.configuration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sos_app_ui.R;

import java.io.Serializable;
import java.util.List;

public class AndroidContact implements Serializable {
    public String getAndroid_contact_Name() {
        return android_contact_Name;
    }

    public void setAndroid_contact_Name(String android_contact_Name) {
        this.android_contact_Name = android_contact_Name;
    }

    public String android_contact_Name = "";

    public String getAndroid_contact_TelefonNr() {
        return android_contact_TelefonNr;
    }

    public void setAndroid_contact_TelefonNr(String android_contact_TelefonNr) {
        this.android_contact_TelefonNr = android_contact_TelefonNr;
    }

    public String android_contact_TelefonNr = "";
    public int android_contact_ID=0;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AndroidContact c1 = (AndroidContact)obj;

        if (!this.android_contact_Name.equals(c1.android_contact_Name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.android_contact_Name.hashCode();
    }
}


class AndroidContactsAdapter extends BaseAdapter {
    Context mContext;
    List<AndroidContact> mList_Android_Contacts;
    String theme = "";

    public AndroidContactsAdapter(Context mContext, List<AndroidContact> mContact) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
    }

    public AndroidContactsAdapter(Context mContext, List<AndroidContact> mContact, String theme) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
        this.theme = theme;
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
        View view;
        if (this.theme.equals("dark"))
            view = View.inflate(mContext, R.layout.contactlist_items_dark, null);
        else
            view = View.inflate(mContext, R.layout.contactlist_items, null);

        TextView textview_contact_Name = view.findViewById(R.id.textview_android_contact_name);
        TextView textview_contact_TelefonNr = view.findViewById(R.id.textview_android_contact_phoneNr);

        textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
        textview_contact_TelefonNr.setText(mList_Android_Contacts.get(position).android_contact_TelefonNr);

        view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
        return view;
    }
}