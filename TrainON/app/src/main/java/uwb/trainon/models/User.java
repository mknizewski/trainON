package uwb.trainon.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class User implements Parcelable
{
    private static final double Meters = 100.0;

    public String Login;
    public String Password;
    public int Weight;
    public int Growth;
    public double BMI;

    protected User(Parcel in) {
        Login = in.readString();
        Password = in.readString();
        Weight = in.readInt();
        Growth = in.readInt();
        BMI = in.readDouble();
    }

    public User()
    { }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User GetUser(Map<String, String> userMap)
    {
        User user = new User();

        user.Login = userMap.get("Login").toString();
        user.Password = userMap.get("Password").toString();
        user.Weight = Integer.parseInt(userMap.get("Weight").toString());
        user.Growth = Integer.parseInt(userMap.get("Growth").toString());

        user.CalculateBMI();

        return user;
    }

    private void CalculateBMI()
    {
        double widthInMeters = Growth / Meters;
        this.BMI = Weight / Math.pow(widthInMeters, 2.0);
    }

    public double GetBmi()
    {
        this.CalculateBMI();
        return BMI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(Login);
        parcel.writeString(Password);
        parcel.writeInt(Weight);
        parcel.writeInt(Growth);
        parcel.writeDouble(BMI);
    }
}
