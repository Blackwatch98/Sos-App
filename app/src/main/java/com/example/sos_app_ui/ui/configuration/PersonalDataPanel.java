package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sos_app_ui.R;
import com.google.android.material.textfield.TextInputEditText;

public class PersonalDataPanel extends AppCompatActivity {


    private TextInputEditText fname;
    private TextInputEditText sname;
    private TextInputEditText age;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = findViewById(R.id.confirmBtn1);
        fname = findViewById(R.id.fNameInput);
        sname = findViewById(R.id.sNameInput);
        age = findViewById(R.id.ageInput);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(dataValidate())
                {
                    String s1 = fname.getText().toString();
                    String s2 =sname.getText().toString();
                    String s3 =age.getText().toString();

                    Bundle args = new Bundle();
                    Intent resultIntent = new Intent(v.getContext(),
                            CreateNewConfiguration.class);

                    args.putString("fname",s1);
                    args.putString("sname",s2);
                    args.putString("age",s3);

                    System.out.println(args);
                    resultIntent.putExtras(args);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext(), "You did not fill all the options or some data is wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean dataValidate()
    {
        String str = this.fname.getText().toString().toLowerCase();


        if(str.matches(""))
            return false;

        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }

        str = this.sname.getText().toString().toLowerCase();
        if(str.matches(""))
            return false;


        charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }

        str = this.age.getText().toString();
        if(str.matches(""))
            return false;
      
        charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= '0' && ch <= '9')) {
                return false;
            }
        }

        int age = Integer.parseInt(str);

        if(age < 5 || age > 55)
            return false;

        return true;
    }
}