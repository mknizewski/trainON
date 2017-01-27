package uwb.trainon.managers;
import uwb.trainon.dictionaries.MessagesDictionary;
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

    public void CheckCreditendials() throws Exception
    {
        if (Login.equals(StringExtensions.Empty) || Password.equals(StringExtensions.Empty))
            throw new Exception(MessagesDictionary.LoginIncorrect);

        // TODO: Sprawdzenie w pilku czy jest odpowiedni wpis
    }

    public void SignIn()
    {
        // TODO: Model usera - klasa statyczna - ustawienie flagi IsAuth na true
    }

    public void SignOut()
    {
        this.Login = StringExtensions.Empty;
        this.Password = StringExtensions.Empty;
    }
}
