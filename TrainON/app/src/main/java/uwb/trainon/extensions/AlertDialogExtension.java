package uwb.trainon.extensions;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import uwb.trainon.Main2Activity;
import uwb.trainon.MainActivity;
import uwb.trainon.factories.IntentFactory;
import uwb.trainon.managers.UserManager;

public class AlertDialogExtension
{
    public static void ShowAlert(String meesage,
                                 String title,
                                 AppCompatActivity activity)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
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

    public static void LogOutAlert(final AppCompatActivity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Wylogowanie");
        builder.setMessage("Czy na pewno chcesz się wylogować?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Main2Activity ac = (Main2Activity) activity;
                UserManager userManager = ac.GetUserManager();

                userManager.SignOut();
                userManager.Dispose();

                Intent loginIntent = IntentFactory.GetIntent(ac, MainActivity.class);
                ac.startActivity(loginIntent);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
