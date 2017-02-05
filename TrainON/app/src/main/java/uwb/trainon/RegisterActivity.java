package uwb.trainon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.RegisterManager;

public class RegisterActivity extends AppCompatActivity
{
    private RegisterManager _registerManager;

    public RegisterActivity()
    {
        this._registerManager = RegisterManager.GetRegisterManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickRegisterButton(View view)
    {
        try
        {
            this.SendDataToManager();

            _registerManager.ValidateData();
            _registerManager.RegisterNewUser();

            this.SwitchActivityToMain();
        }
        catch (IOException ex)
        {
            AlertDialogExtension.ShowAlert(
                    MessagesDictionary.PermissionDenied,
                    StringExtensions.ErrorTitle,
                    RegisterActivity.this);
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    RegisterActivity.this);
        }
    }

    private void SendDataToManager()
    {
        EditText loginEditText = (EditText)findViewById(R.id.loginEditText);
        String login = loginEditText.getText().toString();

        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        String password = passwordEditText.getText().toString();

        EditText weightEditText = (EditText)findViewById(R.id.weightEditText);
        int weight = Integer.parseInt(weightEditText.getText().toString());

        EditText growthEditText = (EditText)findViewById(R.id.growthEditText);
        int growth = Integer.parseInt(growthEditText.getText().toString());

        _registerManager.FillRegisterModel(
                login,
                password,
                weight,
                growth);
    }

    private void SwitchActivityToMain()
    {
        Toast.makeText(
                RegisterActivity.this,
                "Poprawnie zarejestrowano nowe konto.",
                Toast.LENGTH_LONG
        ).show();
    }
}
