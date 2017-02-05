package uwb.trainon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;

public class StatsActivity extends Fragment
{
    private View _myView;
    private UserManager _userManager;
    private FileManager _fileManager;

    public void SetUserManager(UserManager userManager)
    {
        this._userManager = userManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _myView = inflater.inflate(R.layout.activity_stats, container, false);

        this.InitliazeManagers();

        return _myView;
    }

    private void InitliazeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }
}
