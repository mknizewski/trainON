package uwb.trainon.managers;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import uwb.trainon.interfaces.IManager;
import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.models.User;

public class SignInManager implements IManager
{
    private FileManager _fileManager;

    public String Login;
    public String Password;

    private SignInManager(String login, String password)
    {
        this.Login = login;
        this.Password = password;
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

    public User SignIn()
            throws IOException, SAXException, ParserConfigurationException
    {
        User userModel = User.GetUser(_fileManager.GetUserDataFromFile(Login));

        return userModel;
    }

    @Override
    public void Dispose() {

    }
}
