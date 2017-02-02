package uwb.trainon.models;

import java.util.Map;

import uwb.trainon.interfaces.IModel;

public class TreningViewModel implements IModel
{
    public String Activity;
    public DayOfWeek Day;
    public String Time;
    public String Comment;

    @Override
    public Map<String, String> ToMap() {
        return null;
    }

    public static String GetDayOfWeekToString(DayOfWeek dayOfWeek)
    {
        switch (dayOfWeek)
        {
            case Poniedzialek:
                return "Poniedziałek";
            case Wtorek:
                return "Wtorek";
            case Sroda:
                return "Środa";
            case Czwartek:
                return "Czwartek";
            case Piatek:
                return "Piątek";
            case Sobota:
                return "Sobota";
            case Niedziela:
                return "Niedziela";
            default:
                return "";
        }
    }
}
