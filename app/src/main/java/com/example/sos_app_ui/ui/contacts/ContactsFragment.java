package com.example.sos_app_ui.ui.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sos_app_ui.R;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    private ContatsViewModel contatsViewModel;
    private ListView listView_Android_Contacts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //contatsViewModel =
                //ViewModelProviders.of(this).get(ContatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_last_activity, container, false);

//        Button button = root.findViewById(R.id.btnLoad_AndroidContacts);
//        listView_Android_Contacts = (ListView) root.findViewById(R.id.listView_Android_Contacts);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContactsFragment.this.fp_get_Android_Contacts();
//            }
//        });

        return root;
    }

    public void fp_get_Android_Contacts() {
        ArrayList<AndroidContactModel> arrayList_Android_Contacts = new ArrayList<>();

        Cursor cursor_Android_Contacts = null;
    ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        if(cursor_Android_Contacts != null) {
            if (cursor_Android_Contacts.getCount() > 0) {
                while (cursor_Android_Contacts.moveToNext()) {
                    AndroidContactModel android_contact = new AndroidContactModel();
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

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println(phoneNumber);
                            android_contact.android_contact_phone_number = phoneNumber;
                        }
                        phoneCursor.close();
                    }

                    // Add the contact to the ArrayList
                    //arrayList_Android_Contacts.add(android_contact);
                }

                //< show results >
                //ContactsAdapter adapter = new ContactsAdapter(getContext(), arrayList_Android_Contacts);
                //listView_Android_Contacts.setAdapter(adapter);
                //</ show results >
            }
        }
    }
}