package uwb.trainon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provision);

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
            finish();
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    AddProvisionActivity.this);
        }
    }

    private ProvisionViewModel GetProvisionViewModel()
    {
        ProvisionViewModel viewModel = new ProvisionViewModel();


        return viewModel;
    }

    public void onClickReturn(View view)
    {
        finish();
    }
}
