package instructions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Symbol;
import symbols.Type;

/**
 *
 * @author junio
 */
public class VarAssig implements ASTNode{
    private String name;
    private ASTNode exp;

    public VarAssig(String name, ASTNode exp) {
        super();
        this.name = name;
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object value = exp.execute(environment, LError);    //obtenemos el valor de la expresión
        Type type = new Type(null, "Vector");       //Creamos un nuevo tipo con valores iniciales
        
        //Modificamos el tipo primitivo 
        if(value instanceof String)
            type.setTypes(Type.Types.STRING);
        else if (value instanceof Float)
            type.setTypes(Type.Types.NUMERICO);
        else if (value instanceof Integer)
            type.setTypes(Type.Types.INTEGER);
        else if (value instanceof Boolean)
            type.setTypes(Type.Types.BOOLEANO);
        else {
            TError error = new TError("+", "Semántico", "No se pudo asignar el valor a la variable", 0, 0);
            LError.add(error);

            return error;
        }
        
        if(environment.get(name) == null) //Significa que no encontró una variable con ese nombre registrado
            environment.put(new Symbol(type, name, value)); //Entonces lo agregamos a la tabla de simbolos
        else {
            environment.get(name).setValue(value); //De lo contrario actualizo su valor en la tabla
            environment.get(name).setType(type);
        }
        
        return "asignación correcta\n";
    }
    
}
