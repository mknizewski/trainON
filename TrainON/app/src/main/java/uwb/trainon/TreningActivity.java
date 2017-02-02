package uwb.trainon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;

public class TreningActivity extends Fragment
{
    private View _myView;
    private FileManager _fileManager;
    private UserManager _userManager;

    public void SetUserManager(UserManager userManager)
    {
        this._userManager = userManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this._myView = inflater.inflate(R.layout.activity_trening, container, false);
        this.InitializeManagers();

        return _myView;
    }

    private void InitializeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }
}
