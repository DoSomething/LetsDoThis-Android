package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseAvatar;

import java.io.File;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import retrofit.mime.TypedFile;

/**
 * Created by toidiu on 4/16/15.
 */
public class UploadAvatarTask extends Task {

    private String filePath;
    private String userId;
    public  User   user;
    private boolean mHasError;

    public UploadAvatarTask(String id, String filePath) {
        this.userId = id;
        this.filePath = filePath;
        this.mHasError = false;
    }

    public boolean hasError() {
        return mHasError;
    }

    @Override
    protected void run(Context context) throws Throwable {
        File file = new File(filePath);
        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();
        user = userDao.queryForId(userId);
        user.avatarPath = "file:" + filePath;

        userDao.createOrUpdate(user);

        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        ResponseAvatar response = NetworkHelper.getNorthstarAPIService()
                .uploadAvatar(userId, typedFile);
        user.avatarPath = response.data.photo;
        userDao.createOrUpdate(user);
    }

    @Override
    protected boolean handleError(Context context, Throwable e) {
        mHasError = true;
        return true;
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }
}
