package symbols;

/**
 *
 * @author junio
 */
public class Mat {
    private Object values[][];
    public int row;
    public int col;

    public Mat(Object[][] values, int row, int col) {
        this.values = values;
        this.row = row;
        this.col = col;
    }

    public Object[][] getValues() {
        return values;
    }

    public void setValues(Object[][] values) {
        this.values = values;
    }
    
}
