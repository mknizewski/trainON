package uwb.trainon.managers;

import java.io.Serializable;

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
    {

    }

    @Override
    public void Dispose()
    {

    }
}
