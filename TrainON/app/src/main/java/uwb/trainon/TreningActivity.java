package uwb.trainon;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import uwb.trainon.factories.IntentFactory;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;

public class TreningActivity extends Fragment
{
    private View _myView;
    private FileManager _fileManager;
    private UserManager _userManager;

    public static boolean IsAdd = false;

    public void SetUserManager(UserManager userManager)
    {
        this._userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (IsAdd)
        {
            IsAdd = false;

            this.RefreshActivity();

            Toast.makeText(
                    _myView.getContext(),
                    "Poprawnie dodano trening.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this._myView = inflater.inflate(R.layout.activity_trening, container, false);
        this.InitializeManagers();
        this.InitializeListeners();

        return _myView;
    }

    private void InitializeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }

    private void InitializeListeners()
    {
        FloatingActionButton floatingActionButton = (FloatingActionButton) _myView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = IntentFactory.GetIntent(_myView.getContext(), AddTreningActivity.class);
                intent.putExtra("User", _userManager.User);

                startActivity(intent);
            }
        });
    }

    private void RefreshActivity()
    {
        getFragmentManager()
                .beginTransaction()
                .detach(TreningActivity.this)
                .attach(TreningActivity.this)
                .commit();
    }
}
