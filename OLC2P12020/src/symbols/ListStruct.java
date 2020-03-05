package symbols;

import java.util.LinkedList;

/**
 *
 * @author junio
 */
public class ListStruct {
    private LinkedList<Object> values;
    
    public ListStruct(LinkedList<Object> values){
        this.values = values;
    }

    public LinkedList<Object> getValues() {
        return values;
    }

    public void setValues(LinkedList<Object> values) {
        this.values = values;
    }
}
