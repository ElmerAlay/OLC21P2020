package symbols;

import java.util.LinkedList;

/**
 *
 * @author junio
 */
public class Arr {
    private LinkedList<Object> data;
    public int row;
    public int col;
    public int dim[];

    public Arr(LinkedList<Object> data, int row, int col, int[] dim) {
        this.data = data;
        this.row = row;
        this.col = col;
        this.dim = dim;
    }

    public LinkedList<Object> getData() {
        return data;
    }

    public void setData(LinkedList<Object> data) {
        this.data = data;
    }
    
}
