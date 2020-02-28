package instructions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Symbol;
import symbols.Type;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class StructAssig implements ASTNode{
    private String name;
    private ASTNode exp;
    private LinkedList<ASTNode> indexes;

    public StructAssig(String name, ASTNode exp, LinkedList<ASTNode> indexes) {
        super();
        this.name = name;
        this.exp = exp;
        this.indexes = indexes;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object value = exp.execute(environment, LError);    //obtenemos el valor de la expresión
        
        //Verificamos que la variable exista
        if(environment.get(name) != null){
            Symbol symbol = environment.get(name);  //Guardamos el símbolo en una variable
            
            //Verificamos que sea vector
            if(symbol.getValue() instanceof Vec){
                //Verificamos que la lista de índices sólo tenga un objeto
                if(indexes.size()==1){
                    Object index = indexes.remove(0).execute(environment, LError); //Guardo el índice en una variable
                    
                    //Verificamos que su índice sea de tipo integer
                    if(index instanceof Integer){
                        int tVec = ((Vec)symbol.getValue()).getValues().length; //Guardo en una variable el tamaño del vector
                        
                        //Verificamos que si el tamaño del vector es igual a 1
                        if(tVec == 1 && Integer.parseInt(index.toString()) == 1){
                            if(value instanceof String){
                                Object values []= ((Vec)symbol.getValue()).getValues();
                                values[Integer.parseInt(index.toString())-1] = value.toString();
                                ((Vec)environment.get(name).getValue()).setValues(values);
                                environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                            }else if(value instanceof Float){
                                Object values []= ((Vec)symbol.getValue()).getValues();
                                values[Integer.parseInt(index.toString())-1] = Float.parseFloat(value.toString());
                                ((Vec)environment.get(name).getValue()).setValues(values);
                                environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                            }else if(value instanceof Integer){
                                Object values []= ((Vec)symbol.getValue()).getValues();
                                values[Integer.parseInt(index.toString())-1] = Integer.parseInt(value.toString());
                                ((Vec)environment.get(name).getValue()).setValues(values);
                                environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                            }else if(value instanceof Boolean){
                                Object values []= ((Vec)symbol.getValue()).getValues();
                                values[Integer.parseInt(index.toString())-1] = Boolean.parseBoolean(value.toString());
                                ((Vec)environment.get(name).getValue()).setValues(values);
                                environment.get(name).setType(new Type(Type.Types.BOOLEANO, "Vector"));
                            }else{
                                TError error = new TError(name, "Semántico", "La expresión no es de tipo primitivo", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }else{
                            //Verificamos que el índice esté entre 1 y el tamaño del vector
                            if(Integer.parseInt(index.toString()) > 0 && Integer.parseInt(index.toString()) <= tVec){
                                //Hacemos la comparación para el casteo
                                //Verifico si el tipo de dato del vector es String
                                if(symbol.getType().getTypes() == Type.Types.STRING || value instanceof String){
                                    Object values []= ((Vec)symbol.getValue()).getValues();
                                    for(int i = 0; i< values.length; i++){
                                        values[i] = values[i].toString();   //Convierto todos los valores al tipo de dato String
                                    }
                                    values[Integer.parseInt(index.toString())-1] = value.toString();
                                    ((Vec)environment.get(name).getValue()).setValues(values);
                                    environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                                }else{
                                    //Verifico si el tipo de dato de la expresión es float
                                    if(value instanceof Float || symbol.getType().getTypes() == Type.Types.NUMERICO){
                                        Object values []= ((Vec)symbol.getValue()).getValues();
                                        for(int i = 0; i< values.length; i++){
                                            if(values[i] instanceof Boolean)
                                                values[i] = Boolean.parseBoolean(values[i].toString()) ? Float.parseFloat("1.0") : Float.parseFloat("0.0"); //Convierto todos los valores al tipo de dato float 
                                            else
                                                values[i] = Float.parseFloat(values[i].toString());   //Convierto todos los valores al tipo de dato float
                                        }

                                        if(value instanceof Boolean && Boolean.parseBoolean(value.toString()))
                                            values[Integer.parseInt(index.toString())-1] = Float.parseFloat("1.0");
                                        else if(value instanceof Boolean && !Boolean.parseBoolean(value.toString()))
                                            values[Integer.parseInt(index.toString())-1] = Float.parseFloat("0.0");
                                        else
                                            values[Integer.parseInt(index.toString())-1] = Float.parseFloat(value.toString());

                                        ((Vec)environment.get(name).getValue()).setValues(values);
                                        environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                                    }
                                    //Verifico si el tipo de dato de la expresión es integer y el del vector es booleano o integer
                                    else if((value instanceof Integer && (symbol.getType().getTypes() == Type.Types.BOOLEANO || symbol.getType().getTypes() == Type.Types.INTEGER)) ||
                                            (value instanceof Boolean && symbol.getType().getTypes() == Type.Types.INTEGER)){
                                        Object values []= ((Vec)symbol.getValue()).getValues();
                                        for(int i = 0; i< values.length; i++){
                                            if(values[i] instanceof Boolean)
                                                values[i] = Boolean.parseBoolean(values[i].toString()) ? 1 : 0; //Convierto todos los valores al tipo de dato integer
                                            else
                                                values[i] = Integer.parseInt(values[i].toString());
                                        }

                                        if(value instanceof Boolean && Boolean.parseBoolean(value.toString()))
                                            values[Integer.parseInt(index.toString())-1] = 1;
                                        else if(value instanceof Boolean && !Boolean.parseBoolean(value.toString()))
                                            values[Integer.parseInt(index.toString())-1] = 0;
                                        else
                                            values[Integer.parseInt(index.toString())-1] = Integer.parseInt(value.toString());
                                        ((Vec)environment.get(name).getValue()).setValues(values);
                                        environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                                    }
                                    //Verifico si el tipo de dato de la expresión booleano y el del vector es booleano
                                    else if(value instanceof Boolean && symbol.getType().getTypes() == Type.Types.BOOLEANO){
                                        //Convierto la expresión a boolean y la almaceno en su lugar correspondiente
                                        Object values []= ((Vec)symbol.getValue()).getValues();
                                        values[Integer.parseInt(index.toString())-1] = Boolean.parseBoolean(value.toString());
                                        ((Vec)environment.get(name).getValue()).setValues(values);
                                        environment.get(name).setType(new Type(Type.Types.BOOLEANO, "Vector"));
                                    }else {
                                        TError error = new TError(name, "Semántico", "La expresión no es de tipo primitivo", 0, 0);
                                        LError.add(error);

                                        return error;
                                    }
                                }
                            }
                            //el índice es mayor al tamaño del vector
                            else {
                                //Hacemos la comparación para el casteo
                                //Verifico si el tipo de dato del vector es String o el de la expresión es string
                                if(symbol.getType().getTypes() == Type.Types.STRING || value instanceof String){
                                    Object vals[] = new Object[Integer.parseInt(index.toString())]; //Contiene el vector nuevo total
                                    Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                    for(int i = 0; i< values.length; i++){
                                        vals[i] = values[i].toString();   //Convierto todos los valores al tipo de dato String
                                    }

                                    for(int i = values.length; i<Integer.parseInt(index.toString()); i++){
                                        vals[i] = "null";     //Llenamos los espacios sobrantes con el valor por defecto
                                    }

                                    vals[Integer.parseInt(index.toString())-1] = value.toString();
                                    ((Vec)environment.get(name).getValue()).setValues(vals);
                                    environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                                }else{
                                    //Verifico si el tipo de dato de la expresión es float
                                    if(symbol.getType().getTypes() == Type.Types.NUMERICO || value instanceof Float){
                                        Object vals[] = new Object[Integer.parseInt(index.toString())]; //Contiene el vector nuevo total
                                        Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                        for(int i = 0; i< values.length; i++){
                                            if(values[i] instanceof Boolean)
                                                vals[i] = Boolean.parseBoolean(values[i].toString()) ? Float.parseFloat("1.0") : Float.parseFloat("0.0"); //Convierto todos los valores al tipo de dato float 
                                            else
                                                vals[i] = Float.parseFloat(values[i].toString());   //Convierto todos los valores al tipo de dato float
                                        }

                                        for(int i = values.length; i<Integer.parseInt(index.toString()); i++){
                                            vals[i] = Float.parseFloat("0.0");     //Llenamos los espacios sobrantes con el valor por defecto
                                        }

                                        if(value instanceof Boolean && Boolean.parseBoolean(value.toString()))
                                            vals[Integer.parseInt(index.toString())-1] = Float.parseFloat("1.0");
                                        else if(value instanceof Boolean && !Boolean.parseBoolean(value.toString()))
                                            vals[Integer.parseInt(index.toString())-1] = Float.parseFloat("0.0");
                                        else
                                            vals[Integer.parseInt(index.toString())-1] = Float.parseFloat(value.toString());
                                        ((Vec)environment.get(name).getValue()).setValues(vals);
                                        environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                                    }
                                    //Verifico si el tipo de dato de la expresión es integer y el del vector es booleano o integer
                                    else if((value instanceof Integer && (symbol.getType().getTypes() == Type.Types.BOOLEANO || symbol.getType().getTypes() == Type.Types.INTEGER)) ||
                                            (value instanceof Boolean && symbol.getType().getTypes() == Type.Types.INTEGER)){
                                        Object vals[] = new Object[Integer.parseInt(index.toString())]; //Contiene el vector nuevo total
                                        Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                        for(int i = 0; i< values.length; i++){
                                            if(values[i] instanceof Boolean)
                                                vals[i] = Boolean.parseBoolean(values[i].toString()) ? 1 : 0; //Convierto todos los valores al tipo de dato integer
                                            else
                                                vals[i] = Integer.parseInt(values[i].toString());
                                        }

                                        for(int i = values.length; i<Integer.parseInt(index.toString()); i++){
                                            vals[i] = 0;     //Llenamos los espacios sobrantes con el valor por defecto
                                        }

                                        if(value instanceof Boolean && Boolean.parseBoolean(value.toString()))
                                            vals[Integer.parseInt(index.toString())-1] = 1;
                                        else if(value instanceof Boolean && !Boolean.parseBoolean(value.toString()))
                                            vals[Integer.parseInt(index.toString())-1] = 0;
                                        else
                                            vals[Integer.parseInt(index.toString())-1] = Integer.parseInt(value.toString());
                                        ((Vec)environment.get(name).getValue()).setValues(vals);
                                        environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                                    }
                                    //Verifico si el tipo de dato de la expresión booleano y el del vector es booleano
                                    else if(value instanceof Boolean && symbol.getType().getTypes() == Type.Types.BOOLEANO){
                                        Object vals[] = new Object[Integer.parseInt(index.toString())]; //Contiene el vector nuevo total
                                        Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                        for(int i = 0; i< values.length; i++){
                                            vals[i] = Boolean.parseBoolean(values[i].toString());   //Convierto todos los valores al tipo de dato boolean
                                        }

                                        for(int i = values.length; i<Integer.parseInt(index.toString()); i++){
                                            vals[i] = false;     //Llenamos los espacios sobrantes con el valor por defecto
                                        }

                                        vals[Integer.parseInt(index.toString())-1] = Boolean.parseBoolean(value.toString());
                                        ((Vec)environment.get(name).getValue()).setValues(vals);
                                        environment.get(name).setType(new Type(Type.Types.BOOLEANO, "Vector"));
                                    }else {
                                        TError error = new TError(name, "Semántico", "La expresión no es de tipo primitivo", 0, 0);
                                        LError.add(error);

                                        return error;
                                    }
                                }
                            }
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El índice debe ser de tipo integer", 0, 0);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "Un vector no puede tener más de un índice a la hora de modificar", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
        }
        
        return "asignación correcta\n";
    }
}
