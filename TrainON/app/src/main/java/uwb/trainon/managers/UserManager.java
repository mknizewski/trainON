package uwb.trainon.managers;

import android.content.res.Resources;
import android.view.View;

import java.io.Serializable;
import java.util.Random;

import uwb.trainon.R;
import uwb.trainon.interfaces.IManager;
import uwb.trainon.models.User;

public class UserManager implements IManager, Serializable
{
    public User User;

    private UserManager(User user)
    {
        this.User = user;
    }

    public static UserManager GetUserManager(User user)
    {
        return new UserManager(user);
    }

    public void SignOut()
    { }

    public String GetRandomSentences(View view)
    {
        Resources res = view.getResources();
        String[] sentences = res.getStringArray(R.array.sentences);
        String[] authors = res.getStringArray(R.array.authors);

        int size = sentences.length;
        int val;

        Random r = new Random();
        val = r.nextInt(size);
        String result = sentences[val] + System.getProperty ("line.separator") + authors[val];

        return result;
    }

    @Override
    public void Dispose()
    { }
}
