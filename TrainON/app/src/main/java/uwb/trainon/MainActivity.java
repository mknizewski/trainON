package uwb.trainon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.SignInManager;

public class MainActivity extends AppCompatActivity
{
    private SignInManager _signInManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLoginButton(View view)
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
        if (login.equals(StringExtensions.Empty) || password.equals(StringExtensions.Empty))
            throw new Exception(MessagesDictionary.LoginIncorrect);
    }

    public void onClickRegisterButton(View view)
    {
        // TODO: Czy coś przesyłamy do Register Activity? Może manager?
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void ShowExceptionDialog(String meesage, String title)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(meesage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
