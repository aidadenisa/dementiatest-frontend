package blog.aida.dementiatest_frontend.main.models;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by aida on 06-May-18.
 */

public class ConnectPoints implements Serializable {

    private int id;

    private String code;

    private int Index;

    private int x;

    private int y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
