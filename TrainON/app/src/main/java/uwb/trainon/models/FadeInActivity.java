//package uwb.trainon.models;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.animation.Animation;
//import android.widget.TextView;
//
//import uwb.trainon.R;
//
///**
// * Created by Piotrek on 2017-02-02.
// */
//
//public class FadeInActivity extends Activity {
//
//    TextView txtMessage;
//
//    // Animation
//    Animation animFadein;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fadein);
//
//        txtMessage = (TextView) findViewById(R.id.txtMessage);
//
//        // load the animation
//        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.fade_in);
//    }
//}