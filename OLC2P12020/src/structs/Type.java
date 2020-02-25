package structs;

/**
 *
 * @author junio
 */
public class Type {
    
    public static enum Types {
        NUMERO,
        CADENA,
        BOOLEANO,
        VOID,
        LISTA
    };
    
    private Types types;
    private String typeObject;
    
    public Type(Types types) {
        this.types = types;
    }

    public Type(Types types, String typeObject) {
        this.types = types;
        this.typeObject = typeObject;
    }
    
    @Override
    public String toString() {
        if (typeObject == null) {
            return this.types + "";
        }
        return this.types + ":" + this.typeObject;
    }
    
    public boolean equals(Type obj) {
        if (this.typeObject == null && obj.typeObject == null) {
            return this.types == obj.types;
        } else if (this.typeObject != null && obj.typeObject != null) {
            return this.typeObject.equals(obj.typeObject);
        }
        return false;
    }
    
    /**
     * 
     * @return los tipos disponibles
     */
    public Types getTypes() {
        return types;
    }
    
    /**
     * 
     * @param types es de tipo Types
     */
    public void setTypes(Types types) {
        this.types = types;
    }

    /**
     * 
     * @return el tipo de objeto
     */
    public String getTypeObject() {
        return typeObject;
    }

    /**
     * 
     * @param typeObject de tipo String
     */
    public void setTypeObject(String typeObject) {
        this.typeObject = typeObject;
    }
}
