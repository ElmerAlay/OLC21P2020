package symbols;

/**
 *
 * @author junio
 */
public class Symbol {
    private Type type;
    private String id;
    private Object value;
    private int row;
    private int column;
    private String environmentName;
    
    /**
     * Se utiliza para crear un simbolo de base para una declaracion y 
     * asignacion de variable
     *
     * @param type el tipo de dato que correspondera a la variable cadena, entero, etc...
     * @param id el nombre de la variable
     * @param value contiene el valor de la variable
     * */
    public Symbol(Type type, String id, Object value, int row, int column, String environmentName) {
        this.type = type;
        this.id = id;
        this.value = value;
        this.row = row;
        this.column = column;
        this.environmentName = environmentName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }
}
