package uwb.trainon;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.SignInManager;

public class MainActivity extends AppCompatActivity
{
    private SignInManager _signInManager;

    public MainActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LoginEvent()
    {
        EditText loginText = (EditText)findViewById(R.id.loginTextBox);
        String login = loginText.getText().toString();

        EditText passwordText = (EditText)findViewById(R.id.passwordTextBox);
        String password = passwordText.getText().toString();

        try
        {
            CheckCreditentials(login, password);
            _signInManager = SignInManager.GetManager(login, password);
        }
        catch (Exception ex)
        {
            ShowExceptionDialog(ex.getMessage(), StringExtensions.ErrorTitle);
        }
    }

    public void CheckCreditentials(String login, String password) throws Exception
    {
        if (login == StringExtensions.Empty || password == StringExtensions.Empty)
            throw new Exception(MessagesDictionary.LoginIncorrect);
    }

    public void RegisterEvent()
    {

    }

    private void ShowExceptionDialog(String meesage, String title)
    {
        final AlertDialog exceptionDialog = new AlertDialog.Builder(MainActivity.this).create();

        exceptionDialog.setTitle(title);
        exceptionDialog.setMessage(meesage);

        exceptionDialog.show();
    }
}
