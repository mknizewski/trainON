package uwb.trainon.models;

import java.util.HashMap;
import java.util.Map;

import uwb.trainon.interfaces.IModel;

public class RegisterViewModel implements IModel
{
    public String Login;
    public String Password;
    public int Weight;
    public int Growth;

    @Override
    public Map<String, String> ToMap()
    {
        HashMap<String, String> registerModelMap = new HashMap<>();

        registerModelMap.put("Login", Login);
        registerModelMap.put("Password", Password);
        registerModelMap.put("Weight", String.valueOf(Weight));
        registerModelMap.put("Growth", String.valueOf(Growth));

        return registerModelMap;
    }
}
