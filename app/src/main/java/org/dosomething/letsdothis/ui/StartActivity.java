package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;
import org.dosomething.letsdothis.utils.AppPrefs;

/**
 * Created by toidiu on 4/15/15.
 */
public class StartActivity extends ActionBarActivity
{
    private static final String TAG = StartActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if( AppPrefs.getInstance(this).isLoggedIn())
            {goToMain();
        }
        else if(false) //FIXME check first run
        {
            goToRegisterLogin();
        }
        else
        {
            setContentView(R.layout.activity_start);
            findViewById(R.id.start).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(IntroActivity.getLaunchIntent(StartActivity.this));
                    finish();
                }
            });

            findViewById(R.id.slanted_bg).setBackground(new SlantedBackgroundDrawable());
            findViewById(R.id.slanted_bg).setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void goToRegisterLogin()
    {
        startActivity(RegisterLoginActivity.getLaunchIntent(this));
        finish();
    }

    private void goToMain()
    {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }

}
