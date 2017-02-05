package uwb.trainon;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.IntentFactory;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.SignInManager;
import uwb.trainon.models.User;

public class MainActivity extends AppCompatActivity
{
    private SignInManager _signInManager;

    ImageView image_logo;
    LinearLayout panel_login;
    Animation animation_fadein;
    Animation animation_slideup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileManager.CreateAppFolderIfDosentExists();

        // animate + loading + sound
        image_logo = (ImageView)findViewById(R.id.imageView);
        panel_login = (LinearLayout)findViewById(R.id.panel_log);
        animation_fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final MediaPlayer open_sound = MediaPlayer.create(this, R.raw.intro);

        open_sound.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                image_logo.setVisibility(View.VISIBLE);
                image_logo.startAnimation(animation_fadein);
                panel_login.setVisibility(View.VISIBLE);
                panel_login.startAnimation(animation_fadein);
            }
        }, 3000);
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
}
