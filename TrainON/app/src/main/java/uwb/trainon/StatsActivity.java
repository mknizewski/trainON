package uwb.trainon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uwb.trainon.extensions.AlertDialogExtension;
import uwb.trainon.extensions.StringExtensions;
import uwb.trainon.managers.FileManager;
import uwb.trainon.managers.UserManager;
import uwb.trainon.models.StatsViewModel;

public class StatsActivity extends Fragment
{
    private View _myView;
    private UserManager _userManager;
    private FileManager _fileManager;

    public void SetUserManager(UserManager userManager)
    {
        this._userManager = userManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _myView = inflater.inflate(R.layout.activity_stats, container, false);

        this.InitliazeManagers();
        this.FillStats(inflater);

        return _myView;
    }

    private void InitliazeManagers()
    {
        this._fileManager = FileManager.GetFileManager();
    }

    private void FillStats(LayoutInflater inflater)
    {
        try
        {
            LinearLayout historyList = (LinearLayout) _myView.findViewById(R.id.stats_list);
            List<StatsViewModel> statsViewModelList = _fileManager.GetStats(_userManager.User.Login);
            int good = 0;
            int notGood = 0;
            int count = 0;

            for (StatsViewModel viewModel : statsViewModelList)
            {
                View historyView = inflater.inflate(R.layout.item_stats, historyList, false);
                TextView date = (TextView) historyView.findViewById(R.id.stats_date);
                TextView status = (TextView) historyView.findViewById(R.id.stats_status);

                date.setText(viewModel.Day.toString());

                if (viewModel.ActivityDone)
                {
                    status.setText("TAK");
                    good++;
                }
                else
                {
                    status.setText("NIE");
                    notGood++;
                }

                count++;
                historyList.addView(historyView);
            }

            TextView goodStats = (TextView) _myView.findViewById(R.id.stats_good_count);
            TextView notGoodStats = (TextView) _myView.findViewById(R.id.stats_notgood_count);
            TextView percentage = (TextView) _myView.findViewById(R.id.stats_percentage);
            double perc = count != 0 ? (good * 100) / count : 0.0;

            goodStats.setText(String.valueOf(good));
            notGoodStats.setText(String.valueOf(notGood));
            percentage.setText(String.valueOf(perc) + "%");
        }
        catch (Exception ex)
        {
            AlertDialogExtension.ShowAlert(
                    ex.getMessage(),
                    StringExtensions.ErrorTitle,
                    _myView.getContext()
            );
        }
    }
}
