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

/**
 * This is an activity where user load all contacts from his mobile phone
 */

public class ContactsList extends AppCompatActivity {

    private ListView lista;
    private ArrayList<AndroidContact> arrayList_Android_Contacts;
    private ArrayList<AndroidContact> selectedContacts;
    private Button loadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        selectedContacts = new ArrayList<AndroidContact>();
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

            }
        });
    }

    /**
     * This method sets all contacts got earlier on ListView to enable seeing them by user
     */
    public void fp_get_Android_Contacts(){
        arrayList_Android_Contacts = new ArrayList<AndroidContact>();

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
                    AndroidContact android_contact = new AndroidContact();
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

                AndroidContactsAdapter adapter = new AndroidContactsAdapter(this, arrayList_Android_Contacts);
                lista.setAdapter(adapter);
            }
        }
    }

}
