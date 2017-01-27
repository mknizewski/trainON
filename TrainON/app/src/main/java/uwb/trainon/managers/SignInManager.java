package uwb.trainon.managers;
import uwb.trainon.extensions.StringExtensions;

public class SignInManager
{
    public String Login;
    public String Password;
    public boolean IsAuthenticated;

    private SignInManager(String login, String password)
    {
        this.Login = login;
        this.Password = password;
        this.IsAuthenticated = false;
    }

    public static SignInManager GetManager(String login, String password)
    {
        return new SignInManager(login, password);
    }

    public boolean CheckCreditendials()
    {
        return false;
    }

    public void SignIn()
    {

    }

    public void SignOut()
    {
        this.Login = StringExtensions.Empty;
        this.Password = StringExtensions.Empty;
    }
}
