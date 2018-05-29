package blog.aida.dementiatest_frontend.main.services;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by aida on 01-May-18.
 */

public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf",
            LatoBold = ROOT + "Lato/Lato-Bold.ttf",
            LatoRegular = ROOT + "Lato/Lato-Regular.ttf",
            MerriweatherBold = ROOT + "Merriweather/Merriweather-Bold.otf",
            MerriweatherRegular = ROOT + "Merriweather/Merriweather-Regular.otf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}