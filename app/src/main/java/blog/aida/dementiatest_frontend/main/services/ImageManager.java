package blog.aida.dementiatest_frontend.main.services;

import blog.aida.dementiatest_frontend.R;

/**
 * Created by aida on 05-Jun-18.
 */

public class ImageManager {

    public static int getImageResourceFromName(String imageName) {

        switch(imageName) {
            case "wreath":
                return R.drawable.wreath;

            case "volcano":
                return R.drawable.volcano;

            default:
                return -1;
        }

    }
}
