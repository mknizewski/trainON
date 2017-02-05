package uwb.trainon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.IntentFactory;
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
        this.InitializeView(inflater);
        return myView;
    }

    private void InitializeView(LayoutInflater inflater)
    {
        try
        {
            this._fileManager = FileManager.GetFileManager();
            this.InitializePervisonList(inflater);
            this.InitalizeAddPervisionButton();
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    myView.getContext()
            );
        }
    }

    private void InitializePervisonList(LayoutInflater inflater)
            throws IOException, SAXException, ParserConfigurationException
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

            Button deleteBtn = (Button) provisonView.findViewById(R.id.delete_pervisions_button);
            View.OnClickListener deleteBtnListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(myView.getContext());
                    builder1.setMessage(StringExtensions.DeleteProvisionConfirmation);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Tak",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Nie",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            };

            deleteBtn.setOnClickListener(deleteBtnListener);
            pervisionsList.addView(provisonView);
        }
    }

    private void InitalizeAddPervisionButton()
    {
        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetAddPervisionActivity();
            }
        });
    }

    private void SetAddPervisionActivity()
    {
        Intent addProvisionIntent = IntentFactory.GetIntent(myView.getContext(), AddProvisionActivity.class);
        addProvisionIntent.putExtra("User", _userManager.User);
        startActivity(addProvisionIntent);
    }
}