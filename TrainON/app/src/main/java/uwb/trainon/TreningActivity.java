package uwb.trainon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.IntentFactory;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.TreningViewModel;

public class TreningActivity extends Fragment
{
    private View _myView;
    private FileManager _fileManager;
    private UserManager _userManager;

    public static boolean IsAdd = false;

    public void SetUserManager(UserManager userManager)
    {
        this._userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (IsAdd)
        {
            IsAdd = false;

            this.RefreshActivity();

            Toast.makeText(
                    _myView.getContext(),
                    "Poprawnie dodano trening.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this._myView = inflater.inflate(R.layout.activity_trening, container, false);
        this.InitializeManagers();
        this.InitializeListeners();
        this.FillActivity(inflater);

        return _myView;
    }

    private void InitializeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }

    private void FillActivity(LayoutInflater inflater)
    {
        LinearLayout treningList = (LinearLayout) _myView.findViewById(R.id.trening_list);

        if (treningList.getChildCount() != 0)
            treningList.removeAllViews();

        final List<TreningViewModel> treningViewModelList = _fileManager.GetUserTrening(_userManager.User.Login);
        for (final TreningViewModel viewModel : treningViewModelList)
        {
            View treningView = inflater.inflate(R.layout.item_trening, treningList, false);

            TextView header = (TextView) treningView.findViewById(R.id.trening_header_day);
            TextView treningActivity = (TextView) treningView.findViewById(R.id.trening_activity);
            TextView treningDay = (TextView) treningView.findViewById(R.id.trening_day);
            TextView hours = (TextView) treningView.findViewById(R.id.trening_hours);
            TextView commnet = (TextView) treningView.findViewById(R.id.trening_commnent);

            header.setText(viewModel.Day);
            treningActivity.setText(viewModel.Activity);
            treningDay.setText(viewModel.Time);
            hours.setText(String.valueOf(viewModel.Hours));
            commnet.setText(viewModel.Comment);

            Button deleteBtn = (Button) treningView.findViewById(R.id.delete_trening_button);
            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(_myView.getContext());
                    builder1.setMessage(MessagesDictionary.DeleteTrening);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Tak",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    try
                                    {
                                        _fileManager.DeleteTrening(
                                                treningViewModelList.indexOf(viewModel),
                                                _userManager.User.Login
                                        );

                                        RefreshActivity();

                                        Toast.makeText(
                                                _myView.getContext(),
                                                "Poprawnie usunięto trening.",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                    catch (Exception ex)
                                    {
                                        AlertDialogExtension.ShowAlert(
                                                ex.getMessage(),
                                                StringExtensions.ErrorTitle,
                                                _myView.getContext()
                                        );
                                    }

                                    dialog.dismiss();
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
            });

            treningList.addView(treningView);
        }
    }

    private void InitializeListeners()
    {
        FloatingActionButton floatingActionButton = (FloatingActionButton) _myView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = IntentFactory.GetIntent(_myView.getContext(), AddTreningActivity.class);
                intent.putExtra("User", _userManager.User);

                startActivity(intent);
            }
        });
    }

    private void RefreshActivity()
    {
        getFragmentManager()
                .beginTransaction()
                .detach(TreningActivity.this)
                .attach(TreningActivity.this)
                .commit();
    }
}
