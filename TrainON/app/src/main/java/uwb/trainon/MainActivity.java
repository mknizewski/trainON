package uwb.trainon;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.IntentFactory;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.SignInManager;
import uwb.trainon.models.User;

public class MainActivity extends AppCompatActivity
{
    private SignInManager _signInManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileManager.CreateAppFolderIfDosentExists();

        //new Thread(new waitingToStart()).start();
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE); //wylaczam ladowanie
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
            User user = _signInManager.SignIn();

            this.SwitchToMainActivity(user);
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    MainActivity.this);
        }
    }

    private void SwitchToMainActivity(User user)
    {
        Intent intent = IntentFactory.GetIntent(this, Main2Activity.class);
        intent.putExtra("User", user);

        startActivity(intent);
    }

    public void onClickRegisterButton(View view)
    {
        Intent registerIntent = IntentFactory.GetIntent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

//    class waitingToStart implements Runnable
//    {
//        @Override
//        public void run() {
//            try{
//                Thread.sleep(3000);
//            } catch(InterruptedException e){
//                e.printStackTrace();
//            }
//            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//        }
//    }
}
