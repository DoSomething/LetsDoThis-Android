package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.RegisterTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String FB_PROFILE = "FB_PROFILE";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText phoneEmail;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText birthday;
    //~=~=~=~=~=~=~=~=~=~=~=~=Fields

    public static Intent getLaunchIntent(Context context, Profile fbProfile)
    {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(FB_PROFILE, fbProfile);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Profile profile = getIntent().getParcelableExtra(FB_PROFILE);
        initRegisterListener();
        initUI(profile);
    }

    private void initRegisterListener()
    {
        phoneEmail = (EditText) findViewById(R.id.phone_email);
        password = (EditText) findViewById(R.id.password);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        birthday = (EditText) findViewById(R.id.birthday);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String phoneEmailtext = phoneEmail.getText().toString();
                String passtext = password.getText().toString();
                String firsttext = firstName.getText().toString();
                String lasttext = lastName.getText().toString();
                String birthtext = birthday.getText().toString();

                TaskQueue.loadQueueDefault(RegisterActivity.this).execute(
                        new RegisterTask(phoneEmailtext, passtext, firsttext, lasttext, birthtext));
            }
        });
    }

    private void initUI(Profile profile)
    {
        if(profile != null)
        {
            firstName.setText(profile.getFirstName());
            lastName.setText(profile.getLastName());
        }
    }

    @Override
    public void onBackPressed()
    {
        LDTApplication.loginManager.logOut();
        super.onBackPressed();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RegisterTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            Toast.makeText(this, "success register", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            Toast.makeText(this, "failed register", Toast.LENGTH_SHORT).show();
        }
    }

}