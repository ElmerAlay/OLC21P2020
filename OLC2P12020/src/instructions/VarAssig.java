package instructions;

import View.MainWindow;
import abstracto.*;
import java.util.LinkedList;
import symbols.Arr;
import symbols.Casteo;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Symbol;
import symbols.Type;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class VarAssig implements ASTNode{
    private String name;
    private ASTNode exp;
    private int row;
    private int column;

    public VarAssig(String name, ASTNode exp, int row, int column) {
        super();
        this.name = name;
        this.exp = exp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        
        Object value = exp.execute(environment, LError);    //obtenemos el valor de la expresión
        Type type = new Type(null, "Vector");       //Creamos un nuevo tipo con valores iniciales
        
        //Verificamos que el value sea de tipo vector
        if(value instanceof Vec){
            Object result[] = ((Vec) value).getValues();
            
            //Modificamos el tipo primitivo 
            if(result[0] instanceof String)
                type.setTypes(Type.Types.STRING);
            else if (result[0] instanceof Double)
                type.setTypes(Type.Types.NUMERICO);
            else if (result[0] instanceof Integer)
                type.setTypes(Type.Types.INTEGER);
            else if (result[0] instanceof Boolean)
                type.setTypes(Type.Types.BOOLEANO);
            else {
                TError error = new TError(name, "Semántico", "No se pudo asignar el valor a la variable", row, column);
                LError.add(error);

                return error;
            }
            
            if(environment.get(name) == null){ //Significa que no encontró una variable con ese nombre registrado    
                environment.put(new Symbol(type, name, new Vec(result), row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
                MainWindow.general.put(new Symbol(type, name, new Vec(result), row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
            }else {
                environment.get(name).setValue(new Vec(result)); //De lo contrario actualizo su valor en la tabla
                environment.get(name).setType(type);
            }
        }
        //Verificamos que el value sea de tipo lista
        else if(value instanceof ListStruct){
            type.setTypeObject("Lista");
            type.setTypes(Type.Types.LISTA);
            
            if(environment.get(name) == null){ //Significa que no encontró una variable con ese nombre registrado    
                environment.put(new Symbol(type, name, (ListStruct)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
                MainWindow.general.put(new Symbol(type, name, (ListStruct)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
            }else {
                environment.get(name).setValue((ListStruct)value); //De lo contrario actualizo su valor en la tabla
                environment.get(name).setType(type);
            }
        }
        else if(value instanceof Mat){
            if(environment.get(name) == null){ //Significa que no encontró una variable con ese nombre registrado    
                environment.put(new Symbol(Casteo.setType(value), name, (Mat)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
                MainWindow.general.put(new Symbol(Casteo.setType(value), name, (Mat)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
            }else {
                environment.get(name).setValue((Mat)value); //De lo contrario actualizo su valor en la tabla
                environment.get(name).setType(Casteo.setType(value));
            }
            
        }
        else if(value instanceof Arr){
            if(environment.get(name) == null){ //Significa que no encontró una variable con ese nombre registrado    
                environment.put(new Symbol(Casteo.setType(value), name, (Arr)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
                MainWindow.general.put(new Symbol(Casteo.setType(value), name, (Arr)value, row, column, environment.getName())); //Entonces lo agregamos a la tabla de simbolos
            }else {
                environment.get(name).setValue((Arr)value); //De lo contrario actualizo su valor en la tabla
                environment.get(name).setType(Casteo.setType(value));
            }
        }
        
        return null;
    }
    
}
