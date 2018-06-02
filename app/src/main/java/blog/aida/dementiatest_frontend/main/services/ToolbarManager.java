package blog.aida.dementiatest_frontend.main.services;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import blog.aida.dementiatest_frontend.R;

/**
 * Created by aida on 02-Jun-18.
 */

public class ToolbarManager {

    public static void setupToolbar(AppCompatActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        toolbarConfigurations(toolbar,activity);
    }

    public static void setupToolbar(View view, AppCompatActivity activity) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbarConfigurations(toolbar,activity);

    }

    private static void toolbarConfigurations(Toolbar toolbar, AppCompatActivity activity) {
        if(toolbar.findViewById(R.id.toolbar_title_dementia) != null ) {
            ((TextView)toolbar.findViewById(R.id.toolbar_title_dementia)).setTypeface(FontManager.getTypeface(activity,FontManager.MerriweatherBold));
        }
        if(toolbar.findViewById(R.id.toolbar_title_test) != null ) {
            ((TextView)toolbar.findViewById(R.id.toolbar_title_test)).setTypeface(FontManager.getTypeface(activity,FontManager.LatoRegular));
        }
        activity.setSupportActionBar(toolbar);
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


}
