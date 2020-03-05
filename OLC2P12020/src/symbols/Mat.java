package symbols;

/**
 *
 * @author junio
 */
public class Mat {
    private Object values[][];

    public Mat(Object[][] values) {
        this.values = values;
    }

    public Object[][] getValues() {
        return values;
    }

    public void setValues(Object[][] values) {
        this.values = values;
    }
    
}
