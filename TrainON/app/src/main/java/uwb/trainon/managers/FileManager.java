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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import uwb.trainon.interfaces.IManager;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.models.ProvisionViewModel;
import uwb.trainon.models.RegisterViewModel;
import uwb.trainon.models.TreningViewModel;

public class FileManager implements IManager
{
    private static final String AppName = "TrainON";
    private static final String XmlProfileName = "profile.xml";
    private static final String XmlProvisionsName = "provisions.xml";
    private static final String XmlTreningName = "trening.xml";
    private static final String XmlStatsName = "stats.xml";

    public static FileManager GetFileManager()
    {
        return new FileManager();
    }

    public void SaveNewAccount(Map<String, String> registerMap)
            throws IOException
    {
        this.CreateProfileXml(registerMap);
    }

    private void CreateProvisionsXml(Map<String, String> registerMap)
            throws IOException
    {
        String login = registerMap.get("Login").toString();
        String userFolder = FileManager.GetAppFolderPath() + login;
        String provisionFile = userFolder + StringExtensions.Slash + XmlProvisionsName;

        File provision = new File(provisionFile);
        provision.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream outputStream = new FileOutputStream(provision, false);

        xmlSerializer.setOutput(outputStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        outputStream.flush();
        outputStream.close();
    }

    public List<TreningViewModel> GetUserTrening(String login)
    {
        try
        {
            List<TreningViewModel> treningViewModelList = new ArrayList<>();
            String treningPath = FileManager.GetAppFolderPath() + login + StringExtensions.Slash + XmlTreningName;
            File treningFile = new File(treningPath);
            FileInputStream inputStream = new FileInputStream(treningFile);
            InputStreamReader streamReader = new InputStreamReader(inputStream);

            char[] inputBuffer = new char[inputStream.available()];
            streamReader.read(inputBuffer);

            String data = new String(inputBuffer)
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace(" ", "");

            streamReader.close();
            inputStream.close();

            InputStream in = new ByteArrayInputStream(data.getBytes(StringExtensions.UTF));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(in);

            NodeList root = document.getElementsByTagName("trening");
            NodeList items = root.item(0).getChildNodes();

            for (int i = 0; i < items.getLength(); i++)
            {
                NodeList nodeList = items.item(i).getChildNodes();
                TreningViewModel treningViewModel = new TreningViewModel();

                treningViewModel.Day = nodeList.item(0).getTextContent();
                treningViewModel.Activity = nodeList.item(1).getTextContent();
                treningViewModel.Time = nodeList.item(2).getTextContent();
                treningViewModel.Hours = Double.parseDouble(nodeList.item(3).getTextContent());
                treningViewModel.Comment = nodeList.item(4).getTextContent();

                treningViewModelList.add(treningViewModel);
            }

            return treningViewModelList;
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public boolean CheckIfExistsTrening(String day, String login)
    {
        List<TreningViewModel> treningViewModelList = GetUserTrening(login);

        for (TreningViewModel viewModel : treningViewModelList)
        {
            if (viewModel.Day.equals(day))
                return true;
        }

        return false;
    }

    public void SaveTreningByDay(TreningViewModel viewModel, String login)
            throws IOException
    {
        String userFolder = FileManager.GetAppFolderPath() + login;
        String treningFilePath = userFolder + StringExtensions.Slash + XmlTreningName;
        File treningFile = new File(treningFilePath);
        List<TreningViewModel> treningViewModelList = new ArrayList<>();

        if (treningFile.exists())
        {
            treningViewModelList = GetUserTrening(login);
            treningFile.delete();
        }

        treningFile.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream fileOutputStream = new FileOutputStream(treningFilePath, false);

        xmlSerializer.setOutput(fileOutputStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);

        xmlSerializer.startTag(null, "trening");

        xmlSerializer.startTag(null, "day");
        xmlSerializer.startTag(null, "dayname");
        xmlSerializer.text(viewModel.Day);
        xmlSerializer.endTag(null, "dayname");

        xmlSerializer.startTag(null, "activity");
        xmlSerializer.text(viewModel.Activity);
        xmlSerializer.endTag(null, "activity");

        xmlSerializer.startTag(null, "time");
        xmlSerializer.text(viewModel.Time);
        xmlSerializer.endTag(null, "time");

        xmlSerializer.startTag(null, "hours");
        xmlSerializer.text(String.valueOf(viewModel.Hours));
        xmlSerializer.endTag(null, "hours");

        xmlSerializer.startTag(null, "comment");
        xmlSerializer.text(viewModel.Comment);
        xmlSerializer.endTag(null, "comment");
        xmlSerializer.endTag(null, "day");

        for (TreningViewModel treningViewModel : treningViewModelList)
        {
            xmlSerializer.startTag(null, "day");
            xmlSerializer.startTag(null, "dayname");
            xmlSerializer.text(treningViewModel.Day);
            xmlSerializer.endTag(null, "dayname");

            xmlSerializer.startTag(null, "activity");
            xmlSerializer.text(treningViewModel.Activity);
            xmlSerializer.endTag(null, "activity");

            xmlSerializer.startTag(null, "time");
            xmlSerializer.text(treningViewModel.Time);
            xmlSerializer.endTag(null, "time");

            xmlSerializer.startTag(null, "hours");
            xmlSerializer.text(String.valueOf(treningViewModel.Hours));
            xmlSerializer.endTag(null, "hours");

            xmlSerializer.startTag(null, "comment");
            xmlSerializer.text(treningViewModel.Comment);
            xmlSerializer.endTag(null, "comment");
            xmlSerializer.endTag(null, "day");
        }

        xmlSerializer.endTag(null, "trening");

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void DeleteTrening(int index, String login)
        throws IOException, ParserConfigurationException,
               SAXException
    {
        List<TreningViewModel> treningViewModelList = GetUserTrening(login);

        treningViewModelList.remove(index);

        String userFolder = FileManager.GetAppFolderPath() + login;
        String treningPath = userFolder + StringExtensions.Slash + XmlTreningName;
        File treningFile = new File(treningPath);

        if (treningFile.exists())
            treningFile.delete();

        treningFile.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream fileOutputStream = new FileOutputStream(treningPath, false);

        xmlSerializer.setOutput(fileOutputStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);

        xmlSerializer.startTag(null, "trening");

        for (TreningViewModel treningViewModel : treningViewModelList)
        {
            xmlSerializer.startTag(null, "day");
            xmlSerializer.startTag(null, "dayname");
            xmlSerializer.text(treningViewModel.Day);
            xmlSerializer.endTag(null, "dayname");

            xmlSerializer.startTag(null, "activity");
            xmlSerializer.text(treningViewModel.Activity);
            xmlSerializer.endTag(null, "activity");

            xmlSerializer.startTag(null, "time");
            xmlSerializer.text(treningViewModel.Time);
            xmlSerializer.endTag(null, "time");

            xmlSerializer.startTag(null, "hours");
            xmlSerializer.text(String.valueOf(treningViewModel.Hours));
            xmlSerializer.endTag(null, "hours");

            xmlSerializer.startTag(null, "comment");
            xmlSerializer.text(treningViewModel.Comment);
            xmlSerializer.endTag(null, "comment");
            xmlSerializer.endTag(null, "day");
        }

        xmlSerializer.endTag(null, "trening");

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void DeleteProvision(int index, String login)
            throws IOException, ParserConfigurationException,
                   SAXException
    {
        List<ProvisionViewModel> provisionViewModelList = GetProvisions(login);
        provisionViewModelList.remove(index);

        String userFolder = FileManager.GetAppFolderPath() + login;
        String provisionFile = userFolder + StringExtensions.Slash + XmlProvisionsName;
        File provison = new File(provisionFile);

        if (provison.exists())
            provison.delete();

        provison.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream fileOutputStream = new FileOutputStream(provison, false);

        xmlSerializer.setOutput(fileOutputStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);
        xmlSerializer.startTag(null, "provisions");

        for (ProvisionViewModel viewModel : provisionViewModelList)
        {
            xmlSerializer.startTag(null, "provision");

            xmlSerializer.startTag(null, "target");
            xmlSerializer.text(viewModel.Target);
            xmlSerializer.endTag(null, "target");

            xmlSerializer.startTag(null, "date");
            xmlSerializer.text(viewModel.Realization.toString());
            xmlSerializer.endTag(null, "date");

            xmlSerializer.startTag(null, "activity");
            xmlSerializer.text(viewModel.Activity);
            xmlSerializer.endTag(null, "activity");

            xmlSerializer.endTag(null, "provision");
        }
        xmlSerializer.endTag(null, "provisions");

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public List<ProvisionViewModel> GetProvisions(String login)
            throws IOException, ParserConfigurationException,
                   SAXException
    {
        try
        {
            List<ProvisionViewModel> provisionViewModelList = new ArrayList<>();
            String provisionsPath = FileManager.GetAppFolderPath() + login + StringExtensions.Slash + XmlProvisionsName;
            File provisionFile = new File(provisionsPath);
            FileInputStream inputStream = new FileInputStream(provisionFile);
            InputStreamReader streamReader = new InputStreamReader(inputStream);

            char[] inputBuffer = new char[inputStream.available()];
            streamReader.read(inputBuffer);

            String data = new String(inputBuffer)
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace(" ", "");

            streamReader.close();
            inputStream.close();

            InputStream in = new ByteArrayInputStream(data.getBytes(StringExtensions.UTF));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(in);

            NodeList root = document.getElementsByTagName("provisions");
            NodeList items = root.item(0).getChildNodes();

            for (int i = 0; i < items.getLength(); i++)
            {
                NodeList itemList = items.item(i).getChildNodes();
                ProvisionViewModel viewModel = new ProvisionViewModel();

                viewModel.Target = itemList.item(0).getTextContent();
                viewModel.Realization = Date.valueOf(itemList.item(1).getTextContent());
                viewModel.Activity = itemList.item(2).getTextContent();

                provisionViewModelList.add(viewModel);
            }

            return provisionViewModelList;
        }
        catch (Exception ex){
            return new ArrayList<>();
        }
    }

    public void AddProvisions(String login, ProvisionViewModel provisionViewModel)
            throws IOException, SAXException, ParserConfigurationException
    {
        String userFolder = FileManager.GetAppFolderPath() + login;
        String provisionFile = userFolder + StringExtensions.Slash + XmlProvisionsName;
        List<ProvisionViewModel> provisionViewModelList = new ArrayList<>();
        File provision = new File(provisionFile);

        if (provision.exists())
        {
            provisionViewModelList = this.GetProvisions(login);
            provision.delete();
        }

        provision.createNewFile();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        FileOutputStream fileOutputStream = new FileOutputStream(provision, true);

        xmlSerializer.setOutput(fileOutputStream, StringExtensions.UTF);
        xmlSerializer.startDocument(null, Boolean.TRUE);
        xmlSerializer.setFeature(StringExtensions.XmlFeature, true);

        xmlSerializer.startTag(null, "provisions");
        xmlSerializer.startTag(null, "provision");

        xmlSerializer.startTag(null, "target");
        xmlSerializer.text(provisionViewModel.Target);
        xmlSerializer.endTag(null, "target");

        xmlSerializer.startTag(null, "date");
        xmlSerializer.text(provisionViewModel.Realization.toString());
        xmlSerializer.endTag(null, "date");

        xmlSerializer.startTag(null, "activity");
        xmlSerializer.text(provisionViewModel.Activity);
        xmlSerializer.endTag(null, "activity");

        xmlSerializer.endTag(null, "provision");

        for (ProvisionViewModel viewModel : provisionViewModelList)
        {
            xmlSerializer.startTag(null, "provision");

            xmlSerializer.startTag(null, "target");
            xmlSerializer.text(viewModel.Target);
            xmlSerializer.endTag(null, "target");

            xmlSerializer.startTag(null, "date");
            xmlSerializer.text(viewModel.Realization.toString());
            xmlSerializer.endTag(null, "date");

            xmlSerializer.startTag(null, "activity");
            xmlSerializer.text(viewModel.Activity);
            xmlSerializer.endTag(null, "activity");

            xmlSerializer.endTag(null, "provision");
        }

        xmlSerializer.endTag(null, "provisions");

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void CreateProfileXml(Map<String, String> registerMap)
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

        String data = new String(inputBuffer)
                .replace("\n", "")
                .replace("\r", "")
                .replace(" ", "");

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
    { }
}