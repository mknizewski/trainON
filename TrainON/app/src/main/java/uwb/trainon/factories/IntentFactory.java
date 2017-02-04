package uwb.trainon.factories;

import android.content.Context;
import android.content.Intent;

public class IntentFactory
{
    public static Intent GetIntent(Context pageContext, Class<?> cls)
    {
        return new Intent(pageContext, cls);
    }
}
