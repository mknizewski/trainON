package uwb.trainon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.TreningViewModel;
import uwb.trainon.models.User;

public class AddTreningActivity extends AppCompatActivity
{
    private UserManager _userManager;
    private FileManager _fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trening);

        this.GetManagers();

        setTitle("Dodaj trening");
    }

    private void GetManagers()
    {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("User");

        this._userManager = UserManager.GetUserManager(user);
        this._fileManager = FileManager.GetFileManager();
    }

    public void onClickAddTrening(View view)
    {

        try
        {
            TreningViewModel treningViewModel = GetViewModel();
            if (_fileManager.CheckIfExistsTrening(treningViewModel.Day, _userManager.User.Login))
                throw new Exception(MessagesDictionary.TreningIsExists);

            _fileManager.SaveTreningByDay(treningViewModel, _userManager.User.Login);

            TreningActivity.IsAdd = true;
            finish();
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    MessagesDictionary.TreningIsExists,
                    StringExtensions.ErrorTitle,
                    AddTreningActivity.this
            );
        }
    }

    private TreningViewModel GetViewModel()
    {
        TreningViewModel treningViewModel = new TreningViewModel();

        Spinner dayOfWeekSpinner = (Spinner) findViewById(R.id.trening_add_day);
        Spinner activitySpinner = (Spinner) findViewById(R.id.trening_add_activity);
        Spinner timeSpinner = (Spinner) findViewById(R.id.trening_add_time);
        EditText hoursEditText = (EditText) findViewById(R.id.trening_add_hours);
        EditText commentEditText = (EditText) findViewById(R.id.trening_add_comment);

        treningViewModel.Day = dayOfWeekSpinner.getSelectedItem().toString();
        treningViewModel.Activity = activitySpinner.getSelectedItem().toString();
        treningViewModel.Time = timeSpinner.getSelectedItem().toString();
        treningViewModel.Hours = Double.parseDouble(hoursEditText.getText().toString());
        treningViewModel.Comment = commentEditText.getText().toString();

        return treningViewModel;
    }

    public void onClickCancel(View view)
    {
        finish();
    }
}
