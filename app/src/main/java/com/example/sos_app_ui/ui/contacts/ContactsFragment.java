package com.example.sos_app_ui.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.provider.ContactsContract;

import com.example.sos_app_ui.R;

public class ContactsFragment extends Fragment {

    private ContatsViewModel contatsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contatsViewModel =
                ViewModelProviders.of(this).get(ContatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        return root;
    }

    public void btnLoad_AndroidContacts_onClick(View view) {

    }

}