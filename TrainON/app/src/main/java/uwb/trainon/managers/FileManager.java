package uwb.trainon.managers;

import android.os.Environment;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import uwb.trainon.Interfaces.IManager;
import uwb.trainon.dictionaries.MessagesDictionary;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.models.RegisterViewModel;

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
        String userProfileFile = userFolder + StringExtensions.Slash  + XmlProfileName;

        File directoryProfile = new File(userFolder);
        directoryProfile.mkdir();

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

        return userDirectory.exists();
    }

    public Map<String, String> GetUserDataFromFile(String login)
            throws IOException, ParserConfigurationException,
                   SAXException
    {
        String profilePath = FileManager.GetAppFolderPath() + login + StringExtensions.Slash + XmlProfileName;
        File profile = new File(profilePath);
        FileInputStream inputStream = new FileInputStream(profile);
        InputStreamReader streamReader = new InputStreamReader(inputStream);

        char[] inputBuffer = new char[inputStream.available()];
        streamReader.read(inputBuffer);

        String data = new String(inputBuffer).replace("\n", "").replace("\r", "").replace(" ", "");

        streamReader.close();
        inputStream.close();

        InputStream in = new ByteArrayInputStream(data.getBytes(StringExtensions.UTF));
        RegisterViewModel registerModel = new RegisterViewModel();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(in);
        document.getDocumentElement().normalize();

        NodeList items = document.getElementsByTagName("profile");
        NodeList itemList = items.item(0).getChildNodes();

        registerModel.Login = login;
        registerModel.Password = itemList.item(1).getTextContent();
        registerModel.Weight = Integer.parseInt(itemList.item(2).getTextContent());
        registerModel.Growth = Integer.parseInt(itemList.item(3).getTextContent());

        return registerModel.ToMap();
    }

    public static String GetAppFolderPath()
    {
        String slash = StringExtensions.Slash;
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String appFolderPath = absolutePath + slash + AppName + slash;

        return  appFolderPath;
    }

    public static void CreateAppFolderIfDosentExists()
    {
        String slash = StringExtensions.Slash;
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String appFolderPath = absolutePath + slash + AppName;
        File directory = new File(appFolderPath);

        if (!directory.exists())
            directory.mkdir();
    }

    @Override
    public void Dispose()
    {

    }
}