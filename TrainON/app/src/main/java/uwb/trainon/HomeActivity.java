package uwb.trainon;

import android.app.Fragment;
import uwb.trainon.managers.UserManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeActivity extends Fragment
{
    private View myView;
    private UserManager _userManager;

    public void SetUserManager(UserManager userManger)
    {
        this._userManager = userManger;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_home, container, false);
        this.GetSentence();


        return myView;
    }

    private void GetSentence()
    {
        TextView myAwesomeTextView = (TextView)myView.findViewById(R.id.sentence);
        myAwesomeTextView.setText(_userManager.GetRandomSentences(myView));
    }
}