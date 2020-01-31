package com.example.sos_app_ui.ui.configuration;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sos_app_ui.R;
import android.widget.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactsList extends AppCompatActivity {

    private ListView lista;
    private ArrayList<Android_Contact> arrayList_Android_Contacts;
    private ArrayList<Android_Contact> selectedContacts;
    private Button loadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        selectedContacts = new ArrayList<Android_Contact>();
        lista = findViewById(R.id.contacts);
        loadBtn = findViewById(R.id.loadContacts);
        loadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadBtn.setEnabled(false);
                fp_get_Android_Contacts();
            }
        });

        Button bot2 = findViewById(R.id.addBtn);
        bot2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)selectedContacts);


                Intent resultIntent = new Intent(v.getContext(),
                        WarningTargets.class);
                resultIntent.putExtra("selectedContacts",args);
                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });

        lista.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int color = Color.TRANSPARENT;
                Drawable background = view.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();


                if(color == Color.TRANSPARENT)
                {
                    view.setBackgroundColor(Color.LTGRAY);
                    selectedContacts.add(arrayList_Android_Contacts.get(i));
                }
                else if(color == Color.LTGRAY)
                {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    selectedContacts.remove(arrayList_Android_Contacts.get(i));
                }


                System.out.println("ID " + i);
            }
        });
    }


    public void fp_get_Android_Contacts(){
        arrayList_Android_Contacts = new ArrayList<Android_Contact>();

        //--< get all Contacts >--
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try
        {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }
        catch(Exception ex)
        {
            Log.e("Error on contact", ex.getMessage());
        }

        if (cursor_Android_Contacts != null)
        {
            if (cursor_Android_Contacts.getCount() > 0)
            {

                while (cursor_Android_Contacts.moveToNext())
                {
                    Android_Contact android_contact = new Android_Contact();
                    String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                    String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    System.out.println(contact_display_name);

                    android_contact.android_contact_Name = contact_display_name;

                    int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                , null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                                , new String[]{contact_id}
                                , null);

                        while (phoneCursor.moveToNext())
                        {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println(phoneNumber);
                            android_contact.android_contact_TelefonNr = phoneNumber;
                        }
                        phoneCursor.close();
                    }

                    arrayList_Android_Contacts.add(android_contact);
                }

                Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(this, arrayList_Android_Contacts);
                lista.setAdapter(adapter);
            }
        }
    }

}

class Android_Contact implements Serializable{
    public String android_contact_Name = "";
    public String android_contact_TelefonNr = "";
    public int android_contact_ID=0;

    public String getAndroid_contact_Name() {
        return android_contact_Name;
    }

    public void setAndroid_contact_Name(String android_contact_Name) {
        this.android_contact_Name = android_contact_Name;
    }

    public String getAndroid_contact_TelefonNr() {
        return android_contact_TelefonNr;
    }

    public void setAndroid_contact_TelefonNr(String android_contact_TelefonNr) {
        this.android_contact_TelefonNr = android_contact_TelefonNr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Android_Contact c1 = (Android_Contact)obj;

        if (!this.android_contact_Name.equals(c1.android_contact_Name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.android_contact_Name.hashCode();
    }
}

class Adapter_for_Android_Contacts extends BaseAdapter {
    Context mContext;
    List<Android_Contact> mList_Android_Contacts;
    String theme = "";

    public Adapter_for_Android_Contacts(Context mContext, List<Android_Contact> mContact) {
        this.mContext = mContext;
        this.mList_Android_Contacts = mContact;
    }

    public Adapter_for_Android_Contacts(Context mContext, List<Android_Contact> mContact, String theme) {
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

