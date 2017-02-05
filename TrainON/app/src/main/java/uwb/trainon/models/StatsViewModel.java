package uwb.trainon.models;

import java.util.Date;
import java.util.Map;

import uwb.trainon.interfaces.*;

public class StatsViewModel implements IModel
{
    public Date Day;
    public boolean ActivityDone;

    @Override
    public Map<String, String> ToMap() {
        return null;
    }
}
