package blog.aida.dementiatest_frontend.main.services;

import blog.aida.dementiatest_frontend.R;

/**
 * Created by aida on 30-May-18.
 */

public final class TestUtils {

    public static int getImageResource(String name) {

        switch(name) {
            case "cube":
                return R.drawable.cube;

            case "wreath":
                return R.drawable.wreath;

            case "volcano":
                return R.drawable.volcano;

            case "dots":
                return R.drawable.dots;

            default:
                return -1;
        }

    }
}
