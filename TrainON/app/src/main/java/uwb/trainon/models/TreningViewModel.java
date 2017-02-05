package uwb.trainon.models;

import java.util.Map;

import uwb.trainon.interfaces.IModel;

public class TreningViewModel implements IModel
{
    public String Activity;
    public String Day;
    public String Time;
    public double Hours;
    public String Comment;

    @Override
    public Map<String, String> ToMap() {
        return null;
    }
}
