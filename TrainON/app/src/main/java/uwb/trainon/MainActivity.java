package uwb.trainon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.IntentFactory;
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
            _signInManager = SignInManager.GetManager(login, password);
            _signInManager.CheckCreditendials();
            _signInManager.SignIn();

            this.SwitchToMainActivity();
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    MainActivity.this);
        }
    }

    private void SwitchToMainActivity()
    {

    }

    public void onClickRegisterButton(View view)
    {
        Intent registerIntent = IntentFactory.GetIntent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
