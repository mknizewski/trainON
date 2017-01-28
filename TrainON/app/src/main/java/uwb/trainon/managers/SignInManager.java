package uwb.trainon.managers;

import java.util.Map;

import uwb.trainon.Interfaces.IManager;
import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.StringExtensions;

public class SignInManager implements IManager
{
    private FileManager _fileManager;

    public String Login;
    public String Password;
    public boolean IsAuthenticated;

    private SignInManager(String login, String password)
    {
        this.Login = login;
        this.Password = password;
        this.IsAuthenticated = false;
        this._fileManager = FileManager.GetFileManager();
    }

    public static SignInManager GetManager(String login, String password)
    {
        return new SignInManager(login, password);
    }

    public void CheckCreditendials()
            throws Exception
    {
        if (Login.equals(StringExtensions.Empty) || Password.equals(StringExtensions.Empty))
            throw new Exception(MessagesDictionary.LoginIncorrect);

        if (!_fileManager.UserExists(Login))
            throw new Exception(MessagesDictionary.LoginNotFound);

        Map<String, String> userMap = _fileManager.GetUserDataFromFile(Login);
        String passwordFromFile = userMap.get("Password").toString();

        if (!Password.equals(passwordFromFile))
            throw new Exception(MessagesDictionary.LoginIncorrect);
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

    @Override
    public void Dispose() {

    }
}
