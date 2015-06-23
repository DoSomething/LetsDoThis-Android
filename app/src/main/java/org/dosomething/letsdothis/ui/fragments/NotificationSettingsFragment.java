package org.dosomething.letsdothis.ui.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.ui.ChangeEmailActivity;
import org.dosomething.letsdothis.ui.ChangeNumberActivity;
import org.dosomething.letsdothis.ui.ChangePasswordActivity;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class NotificationSettingsFragment extends PreferenceFragment
{
    public static NotificationSettingsFragment newInstance()
    {
        return new NotificationSettingsFragment();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.preference_notification);

        initChangeNotifications();
    }

    private void initChangeNotifications()
    {
        findPreference("setting_1")
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        //FIXME-----
                        Log.d("------", "----------NOTIFY");
                        return true;
                    }
                });
    }

}