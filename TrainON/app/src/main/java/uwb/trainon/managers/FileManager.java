package uwb.trainon.managers;

import uwb.trainon.Interfaces.IManager;

public class FileManager implements IManager
{
    private FileManager()
    {

    }

    public static FileManager GetFileManager()
    {
        return new FileManager();
    }

    @Override
    public void Dispose() {

    }
}
