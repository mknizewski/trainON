package uwb.trainon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.ProvisionViewModel;
import uwb.trainon.models.User;

public class AddProvisionActivity extends AppCompatActivity
{
    private UserManager _userManager;
    private FileManager _fileManager;

    ImageButton date;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provision);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
        setTitle("Dodaj postanowienia");

        this.InitalizeManagers();
    }

    private void InitalizeManagers()
    {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("User");

        this._userManager = UserManager.GetUserManager(user);
        this._fileManager = FileManager.GetFileManager();
    }

    public void onClickAddProvision(View view)
    {
        try
        {
            ProvisionViewModel viewModel = this.GetProvisionViewModel();
            _fileManager.AddProvisions(_userManager.User.Login, viewModel);

            ProvisionsActivity.IsAdd = true;
            finish();
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    "Podaj cel treningu!",
                    StringExtensions.ErrorTitle,
                    AddProvisionActivity.this);
        }
    }

    private ProvisionViewModel GetProvisionViewModel()
    {
        ProvisionViewModel viewModel = new ProvisionViewModel();

        EditText targetEditText = (EditText) findViewById(R.id.add_provision_target);
        TextView realizationEditText = (TextView) findViewById(R.id.add_provision_realization);
        Spinner activitySpinner = (Spinner) findViewById(R.id.add_provision_activity);

        String target = targetEditText.getText().toString();
        String realization = realizationEditText.getText().toString();
        String activity = String.valueOf(activitySpinner.getSelectedItem());

        viewModel.Target = target;
        viewModel.Realization = Date.valueOf(realization);
        viewModel.Activity = activity;

        return viewModel;
    }

    /* CALENDARE */
    public void showDialogOnButtonClick()
    {
        date = (ImageButton)findViewById(R.id.cal);
        date.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
       if(id == DIALOG_ID)
           return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int montOfYear, int dayOfMonth)
        {
            year_x = year;
            month_x = montOfYear + 1;
            day_x = dayOfMonth;

            //Toast.makeText(AddProvisionActivity.this, year_x + "/" + month_x + "/" + day_x, Toast.LENGTH_LONG).show();
            String result = year_x + "-" + month_x + "-" + day_x;
            TextView edit = (TextView) findViewById(R.id.add_provision_realization);
            edit.setText(result);
        }
    };

    public void onClickReturn(View view)
    {
        finish();
    }
}
