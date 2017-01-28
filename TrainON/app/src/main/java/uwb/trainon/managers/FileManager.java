package uwb.trainon.managers;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import uwb.trainon.Interfaces.IManager;
import uwb.trainon.extensions.StringExtensions;

public class FileManager implements IManager
{
    private static final String AppName = "TrainON";
    private static final String XmlProfileName = "profile.xml";

    public static FileManager GetFileManager()
    {
        return new FileManager();
    }

    public void SaveNewAccount(Map<String, String> registerMap)
            throws IOException
    {
        String login = registerMap.get("Login").toString();
        String password = registerMap.get("Password").toString();
        String weight = registerMap.get("Weight").toString();
        String growth = registerMap.get(("Growth")).toString();
        String userFolder = FileManager.GetAppFolderPath() + login;
        String userProfileFile = userFolder + XmlProfileName;

        File profile = new File(userProfileFile);
        profile.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream fileStream = new FileOutputStream(profile, false);

        xmlSerializer.setOutput(fileStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);

        xmlSerializer.startTag(null, "profile");

        xmlSerializer.startTag(null, "login");
        xmlSerializer.text(login);
        xmlSerializer.endTag(null, "login");

        xmlSerializer.startTag(null, "password");
        xmlSerializer.text(password);
        xmlSerializer.endTag(null, "password");

        xmlSerializer.startTag(null, "weight");
        xmlSerializer.text(weight);
        xmlSerializer.endTag(null, "weight");

        xmlSerializer.startTag(null, "growth");
        xmlSerializer.text(growth);
        xmlSerializer.endTag(null, "growth");

        xmlSerializer.endTag(null, "profile");
        xmlSerializer.endDocument();
        xmlSerializer.flush();

        fileStream.flush();
        fileStream.close();
    }

    public boolean UserExists(String login)
    {
        String appFolder = FileManager.GetAppFolderPath();
        File userDirectory = new File(appFolder + login);

        if (userDirectory.exists())
            return true;

        return false;
    }

    public Map<String, String> GetUserDataFromFile(String login)
    {
        return null;
    }

    public static String GetAppFolderPath()
    {
        String slash = StringExtensions.Slash;
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String appFolderPath = absolutePath + slash + AppName + slash;

        return  appFolderPath;
    }

    @Override
    public void Dispose()
    {

    }
}