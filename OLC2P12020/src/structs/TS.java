package structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author junio
 */
public class TS {
    private Map<String, Symbol> Table;
    private TS Anterior;
    
    public TS(TS Anterior) {
        this.Table = new HashMap<>();
        this.Anterior = Anterior;
    }
    
    public String setVariable(Symbol simbolo) {
        for (TS e = this; e != null; e = e.getAnterior()) {
            Symbol encontro = (Symbol) (e.getTable().get(simbolo.getId()));
            if (encontro != null) {
                return "La variable con el identificador"
                        + simbolo.getId() + " ya existe.";
            }
        }
        this.Table.put(simbolo.getId(), simbolo);
        return null;
    }
    
    public Symbol getVariable(String id) {
        for (TS e = this; e != null; e = e.getAnterior()) {
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

    public TS getAnterior() {
        return Anterior;
    }

    public void setAnterior(TS Anterior) {
        this.Anterior = Anterior;
    }
}
