package symbols;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author junio
 */
public class Environment {
    private Map<String, Symbol> Table;
    private Environment Anterior;
    
    public Environment(Environment Anterior) {
        this.Table = new HashMap<>();
        this.Anterior = Anterior;
    }
    
    /**
     * Inserta una variable a la tabla de símbolos
     * @param simbolo
     * @return String
     */
    public String put(Symbol simbolo) {
        for (Environment e = this; e != null; e = e.getAnterior()) {
            Symbol encontro = (Symbol) (e.getTable().get(simbolo.getId()));
            if (encontro != null) {
                return "La variable con el identificador"
                        + simbolo.getId() + " ya existe.";
            }
        }
        
        this.Table.put(simbolo.getId(), simbolo);
        
        return null;
    }
    
    /**
     * Recupera el simbolo que tiene el id enviado
     * @param id
     * @return Simbolo
     */
    public Symbol get(String id) {
        for (Environment e = this; e != null; e = e.getAnterior()) {
            Symbol encontro = (Symbol) (e.getTable().get(id));
            if (encontro != null) {
                return encontro;
            }
        }
        return null;
    }

    public Map<String, Symbol> getTable() {
        return Table;
    }

    public void setTable(Map<String, Symbol> Table) {
        this.Table = Table;
    }

    public Environment getAnterior() {
        return Anterior;
    }

    public void setAnterior(Environment Anterior) {
        this.Anterior = Anterior;
    }
}
