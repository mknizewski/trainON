package uwb.trainon;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.User;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserManager _userManager;

    private void GetUser()
    {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("User");

        _userManager = UserManager.GetUserManager(user);
    }

    public UserManager GetUserManager()
    {
        return _userManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.GetUser();
        this.InitializeView(navigationView.getHeaderView(0));

        //set text from array
        Resources res = getResources();
        String[] sentences = res.getStringArray(R.array.sentences);
        String[] authors = res.getStringArray(R.array.authors);
        TextView myAwesomeTextView = (TextView)findViewById(R.id.sentence);

        int size = sentences.length;
        int val;
        Random r = new Random();
        val = r.nextInt(size);
        String result;
        result = sentences[val] + System.getProperty ("line.separator") + authors[val];
        myAwesomeTextView.setText(result);
    }

    private void InitializeView(View header)
    {
        TextView loginInNav = (TextView) header.findViewById(R.id.loginInNav);
        loginInNav.setText(_userManager.User.Login);

        TextView bmiInNav = (TextView) header.findViewById(R.id.bmiInNav);
        String bmiFormat = String.format(bmiInNav.getText().toString(), _userManager.User.GetBmi());
        bmiInNav.setText(bmiFormat);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_trening)
        {

        }
        else if (id == R.id.nav_stats)
        {

        }
        else if (id == R.id.nav_provisions)
        {
            ProvisionsActivity provisionsActivity = new ProvisionsActivity();
            provisionsActivity.set_userManager(_userManager);

            fragmentManager.beginTransaction()
                    .replace(R.id.content_main2, provisionsActivity)
                    .commit();
        }
        else if (id == R.id.nav_settings)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main2, new SettingsActivity())
                    .commit();
        }
        else if (id == R.id.nav_authors)
        {

        }
        else if (id == R.id.nav_logout)
        {
            AlertDialogExtension.LogOutAlert(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
