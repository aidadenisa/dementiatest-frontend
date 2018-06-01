package blog.aida.dementiatest_frontend.main.models;

import java.io.Serializable;

/**
 * Created by aida on 06-May-18.
 */

public class ConnectPoints implements Serializable, Cloneable {

    private int id;

    private String code;

    private int index;

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
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        ConnectPoints point = new ConnectPoints();
        point.setX(this.x);
        point.setY(this.y);
        point.setCode(this.code);
        point.setIndex(this.index);
        point.setId(this.id);

        return point;
    }
}
