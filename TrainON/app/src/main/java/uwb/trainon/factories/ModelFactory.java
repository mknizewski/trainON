package uwb.trainon.factories;

public class ModelFactory
{
    public static<T> T GetModel(Class<T> tClass)
    {
        try
        {
            T instance = tClass.newInstance();

            return instance;
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return null;
        }
    }
}
