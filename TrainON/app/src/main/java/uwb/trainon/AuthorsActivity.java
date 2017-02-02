package uwb.trainon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthorsActivity extends Fragment
{
    private View _myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this._myView = inflater.inflate(R.layout.activity_authors, container, false);
        return _myView;
    }
}
