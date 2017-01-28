package uwb.trainon.managers;

import java.io.IOException;

import uwb.trainon.Interfaces.IManager;
import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.factories.ModelFactory;
import uwb.trainon.models.RegisterViewModel;

public class RegisterManager implements IManager
{
    private FileManager _fileManager;
    private RegisterViewModel _registerViewModel;

    private static final int MinPasswordLength = 5;

    private RegisterManager()
    {
        this._fileManager = FileManager.GetFileManager();
        this._registerViewModel = ModelFactory.GetModel(RegisterViewModel.class);
    }

    public static RegisterManager GetRegisterManager()
    {
        return new RegisterManager();
    }

    public void FillRegisterModel(
            String login,
            String password,
            int weight,
            int growth)
    {
        this._registerViewModel.Login = login;
        this._registerViewModel.Password = password;
        this._registerViewModel.Weight = weight;
        this._registerViewModel.Growth = growth;
    }

    public void ValidateData() throws Exception
    {
        if (_registerViewModel.Login.equals(StringExtensions.Empty))
            throw new Exception(MessagesDictionary.LoginIsEmpty);

        if (_registerViewModel.Password.equals(StringExtensions.Empty) ||
                _registerViewModel.Password.length() < MinPasswordLength)
            throw new Exception(MessagesDictionary.PasswordDosentMatchPattern);

        if (_fileManager.UserExists(_registerViewModel.Login))
            throw new Exception(MessagesDictionary.LoginIsUsed);
    }

    public void RegisterNewUser()
            throws IOException
    {
        _fileManager.SaveNewAccount(_registerViewModel.ToMap());
    }

    @Override
    public void Dispose()
    {
        _fileManager.Dispose();
        _registerViewModel = null;
    }
}
