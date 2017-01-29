package uwb.trainon;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.ProvisionViewModel;

public class ProvisionsActivity extends Fragment
{
    public View myView;
    private UserManager _userManager;
    private FileManager _fileManager;

    public void set_userManager(UserManager _userManager) {
        this._userManager = _userManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_provisions, container, false);

        InitializeView(inflater);

        return myView;
    }

    private void InitializeView(LayoutInflater inflater)
    {
        this.InitializePervisonList(inflater);
        this.InitalizeAddPervisionButton();

        this._fileManager = FileManager.GetFileManager();
    }

    private void InitializePervisonList(LayoutInflater inflater)
    {
        LinearLayout pervisionsList = (LinearLayout) myView.findViewById(R.id.provisions_list);

        List<ProvisionViewModel> provisionViewModelList = _fileManager.GetProvisions(_userManager.User.Login);
        for (ProvisionViewModel provision : provisionViewModelList)
        {
            View provisonView = inflater.inflate(R.layout.item_provisions, pervisionsList, false);
            TextView targetTextView = (TextView) provisonView.findViewById(R.id.pervision_target);
            TextView activityTextView = (TextView) provisonView.findViewById(R.id.pervision_activity);
            TextView dateTextView = (TextView) provisonView.findViewById(R.id.pervision_realization);

            targetTextView.setText(provision.Target);
            activityTextView.setText(provision.Activity);
            dateTextView.setText(provision.Realization.toString());

            pervisionsList.addView(provisonView);
        }
    }

    private void InitalizeAddPervisionButton()
    {
        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action " + _userManager.User.Login, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}