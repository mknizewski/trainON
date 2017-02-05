package uwb.trainon;

import android.app.Fragment;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.StatsViewModel;
import uwb.trainon.models.TreningViewModel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends Fragment
{
    private View myView;
    private UserManager _userManager;
    private FileManager _fileManager;

    public void SetUserManager(UserManager userManger)
    {
        this._userManager = userManger;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.activity_home, container, false);

        this.GetSentence();
        this.InitializeManagers();
        this.SetTreningModal();

        return myView;
    }

    private void InitializeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }

    private void GetSentence()
    {
        TextView myAwesomeTextView = (TextView)myView.findViewById(R.id.sentence);
        myAwesomeTextView.setText(_userManager.GetRandomSentences(myView));
    }

    private void SetTreningModal()
    {
        LinearLayout treningModal = (LinearLayout) myView.findViewById(R.id.trening);
        Button btnYes = (Button) myView.findViewById(R.id.button_activity_yes);
        Button btnNo = (Button) myView.findViewById(R.id.button_activity_no);
        List<StatsViewModel> statsViewModelList = _fileManager.GetStats(_userManager.User.Login);
        List<TreningViewModel> treningViewModelList = _fileManager.GetUserTrening(_userManager.User.Login);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        String day = StringExtensions.Empty;

        switch (currentDay)
        {
            case Calendar.MONDAY:
                day = "Poniedziałek";
                break;
            case Calendar.TUESDAY:
                day = "Wtorek";
                break;
            case Calendar.WEDNESDAY:
                day = "Środa";
                break;
            case Calendar.THURSDAY:
                day = "Czwartek";
                break;
            case Calendar.FRIDAY:
                day = "Piątek";
                break;
            case Calendar.SATURDAY:
                day = "Sobota";
                break;
            case Calendar.SUNDAY:
                day = "Niedziela";
                break;
        }

        for (StatsViewModel statsViewModel : statsViewModelList)
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String viewModelDate = dateFormat.format(statsViewModel.Day);
            String currentDaySt = dateFormat.format(currentDate);

            if (viewModelDate.equals(currentDaySt))
            {
                treningModal.setVisibility(View.GONE);
                return;
            }
        }

        for (TreningViewModel treningViewModel : treningViewModelList)
        {
            if (treningViewModel.Day.equals(day))
            {
                TextView treningName = (TextView) myView.findViewById(R.id.trening_name);
                treningName.setText(treningViewModel.Activity);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        StatsViewModel statsViewModel = new StatsViewModel();

                        statsViewModel.Day = new Date();
                        statsViewModel.ActivityDone = true;

                        try
                        {
                            _fileManager.SaveStats(_userManager.User.Login, statsViewModel);

                            CloseTreningModal();
                            Toast.makeText(
                                    myView.getContext(),
                                    "Tak trzymaj!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        catch (Exception ex)
                        {
                            AlertDialogExtension.ShowAlert(
                                    ex.getMessage(),
                                    StringExtensions.ErrorTitle,
                                    myView.getContext()
                            );
                        }
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        StatsViewModel statsViewModel = new StatsViewModel();

                        statsViewModel.Day = new Date();
                        statsViewModel.ActivityDone = false;

                        try
                        {
                            _fileManager.SaveStats(_userManager.User.Login, statsViewModel);

                            CloseTreningModal();
                            Toast.makeText(
                                    myView.getContext(),
                                    "Niedobrze :(",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                        catch (Exception ex)
                        {
                            AlertDialogExtension.ShowAlert(
                                    ex.getMessage(),
                                    StringExtensions.ErrorTitle,
                                    myView.getContext()
                            );
                        }
                    }
                });

                treningModal.setVisibility(View.VISIBLE);
                return;
            }
        }

        treningModal.setVisibility(View.GONE);
    }

    private void CloseTreningModal()
    {
        LinearLayout linearLayout = (LinearLayout) myView.findViewById(R.id.trening);
        linearLayout.setVisibility(View.GONE);
    }
}