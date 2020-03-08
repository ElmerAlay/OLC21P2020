package instructions;

import abstracto.*;
import analizadores.Simbolo;
import java.util.LinkedList;
import stadisticFunctions.SupportFunctions;
import symbols.Arr;
import symbols.Casteo;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Symbol;
import symbols.Type;
import symbols.Vec;
import symbols.Vec2;

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
                
                //Verificamos que la expresión sea un vector y sea de tamaño 1
                if(value instanceof Vec && ((Vec)value).getValues().length==1){
                    Object val = ((Vec)value).getValues()[0];                
                    
                    //Verificamos que la lista de índices sólo tenga un objeto
                    if(indexes.size()==1){
                        Object index = indexes.remove(0).execute(environment, LError); //Guardo el índice en una variable

                        //Verificar que el índice sea un vector de tamaño 1
                        if(index instanceof Vec && ((Vec)index).getValues().length==1){
                            Object ind[] = ((Vec)index).getValues();    //Guardo en una varibale el índice 

                            //Verificamos que su índice sea de tipo integer
                            if(ind[0] instanceof Integer){
                                int tVec = ((Vec)symbol.getValue()).getValues().length; //Guardo en una variable el tamaño de la variable vector a asignar el valor

                                //Verificamos que si el tamaño del vector es igual a 1 y el índice también es igual a 1
                                if(tVec == 1 && Integer.parseInt(ind[0].toString()) == 1){
                                        //Hacemos el casteo
                                        if(val instanceof String){
                                            Object values []= ((Vec)symbol.getValue()).getValues();
                                            values[Integer.parseInt(ind[0].toString())-1] = val.toString();
                                            ((Vec)environment.get(name).getValue()).setValues(values);
                                            environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                                        }else if(val instanceof Float){
                                            Object values []= ((Vec)symbol.getValue()).getValues();
                                            values[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat(val.toString());
                                            ((Vec)environment.get(name).getValue()).setValues(values);
                                            environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                                        }else if(val instanceof Integer){
                                            Object values []= ((Vec)symbol.getValue()).getValues();
                                            values[Integer.parseInt(ind[0].toString())-1] = Integer.parseInt(val.toString());
                                            ((Vec)environment.get(name).getValue()).setValues(values);
                                            environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                                        }else if(val instanceof Boolean){
                                            Object values []= ((Vec)symbol.getValue()).getValues();
                                            values[Integer.parseInt(ind[0].toString())-1] = Boolean.parseBoolean(val.toString());
                                            ((Vec)environment.get(name).getValue()).setValues(values);
                                            environment.get(name).setType(new Type(Type.Types.BOOLEANO, "Vector"));
                                        }else{
                                            TError error = new TError(name, "Semántico", "La expresión no es de tipo primitivo", 0, 0);
                                            LError.add(error);

                                            return error;
                                        }
                                }else{
                                    //Verificamos que el índice esté entre 1 y el tamaño del vector
                                    if(Integer.parseInt(ind[0].toString()) > 0 && Integer.parseInt(ind[0].toString()) <= tVec){
                                        //Hacemos la comparación para el casteo
                                        //Verifico si el tipo de dato del vector es String
                                        if(symbol.getType().getTypes() == Type.Types.STRING || val instanceof String){
                                            Object values []= ((Vec)symbol.getValue()).getValues();
                                            for(int i = 0; i< values.length; i++){
                                                values[i] = values[i].toString();   //Convierto todos los valores al tipo de dato String
                                            }
                                            values[Integer.parseInt(ind[0].toString())-1] = val.toString();
                                            ((Vec)environment.get(name).getValue()).setValues(values);
                                            environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                                        }else{
                                            //Verifico si el tipo de dato de la expresión es float
                                            if(val instanceof Float || symbol.getType().getTypes() == Type.Types.NUMERICO){
                                                Object values []= ((Vec)symbol.getValue()).getValues();
                                                for(int i = 0; i< values.length; i++){
                                                    if(values[i] instanceof Boolean)
                                                        values[i] = Boolean.parseBoolean(values[i].toString()) ? Float.parseFloat("1.0") : Float.parseFloat("0.0"); //Convierto todos los valores al tipo de dato float 
                                                    else
                                                        values[i] = Float.parseFloat(values[i].toString());   //Convierto todos los valores al tipo de dato float
                                                }

                                                if(val instanceof Boolean && Boolean.parseBoolean(val.toString()))
                                                    values[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat("1.0");
                                                else if(value instanceof Boolean && !Boolean.parseBoolean(value.toString()))
                                                    values[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat("0.0");
                                                else
                                                    values[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat(val.toString());

                                                ((Vec)environment.get(name).getValue()).setValues(values);
                                                environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                                            }
                                            //Verifico si el tipo de dato de la expresión es integer y el del vector es booleano o integer
                                            else if((val instanceof Integer && (symbol.getType().getTypes() == Type.Types.BOOLEANO || symbol.getType().getTypes() == Type.Types.INTEGER)) ||
                                                    (val instanceof Boolean && symbol.getType().getTypes() == Type.Types.INTEGER)){
                                                Object values []= ((Vec)symbol.getValue()).getValues();
                                                for(int i = 0; i< values.length; i++){
                                                    if(values[i] instanceof Boolean)
                                                        values[i] = Boolean.parseBoolean(values[i].toString()) ? 1 : 0; //Convierto todos los valores al tipo de dato integer
                                                    else
                                                        values[i] = Integer.parseInt(values[i].toString());
                                                }

                                                if(val instanceof Boolean && Boolean.parseBoolean(val.toString()))
                                                    values[Integer.parseInt(ind[0].toString())-1] = 1;
                                                else if(val instanceof Boolean && !Boolean.parseBoolean(val.toString()))
                                                    values[Integer.parseInt(ind[0].toString())-1] = 0;
                                                else
                                                    values[Integer.parseInt(ind[0].toString())-1] = Integer.parseInt(val.toString());
                                                
                                                ((Vec)environment.get(name).getValue()).setValues(values);
                                                environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                                            }
                                            //Verifico si el tipo de dato de la expresión booleano y el del vector es booleano
                                            else if(val instanceof Boolean && symbol.getType().getTypes() == Type.Types.BOOLEANO){
                                                //Convierto la expresión a boolean y la almaceno en su lugar correspondiente
                                                Object values []= ((Vec)symbol.getValue()).getValues();
                                                values[Integer.parseInt(ind[0].toString())-1] = Boolean.parseBoolean(val.toString());
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
                                        if(symbol.getType().getTypes() == Type.Types.STRING || val instanceof String){
                                            Object vals[] = new Object[Integer.parseInt(ind[0].toString())]; //Contiene el vector nuevo total
                                            Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                            for(int i = 0; i< values.length; i++){
                                                vals[i] = values[i].toString();   //Convierto todos los valores al tipo de dato String
                                            }

                                            for(int i = values.length; i<Integer.parseInt(ind[0].toString()); i++){
                                                vals[i] = "null";     //Llenamos los espacios sobrantes con el valor por defecto
                                            }

                                            vals[Integer.parseInt(ind[0].toString())-1] = val.toString();
                                            ((Vec)environment.get(name).getValue()).setValues(vals);
                                            environment.get(name).setType(new Type(Type.Types.STRING, "Vector"));
                                        }else{
                                            //Verifico si el tipo de dato de la expresión es float
                                            if(symbol.getType().getTypes() == Type.Types.NUMERICO || val instanceof Float){
                                                Object vals[] = new Object[Integer.parseInt(ind[0].toString())]; //Contiene el vector nuevo total
                                                Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                                for(int i = 0; i< values.length; i++){
                                                    if(values[i] instanceof Boolean)
                                                        vals[i] = Boolean.parseBoolean(values[i].toString()) ? Float.parseFloat("1.0") : Float.parseFloat("0.0"); //Convierto todos los valores al tipo de dato float 
                                                    else
                                                        vals[i] = Float.parseFloat(values[i].toString());   //Convierto todos los valores al tipo de dato float
                                                }

                                                for(int i = values.length; i<Integer.parseInt(ind[0].toString()); i++){
                                                    vals[i] = Float.parseFloat("0.0");     //Llenamos los espacios sobrantes con el valor por defecto
                                                }

                                                if(val instanceof Boolean && Boolean.parseBoolean(val.toString()))
                                                    vals[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat("1.0");
                                                else if(val instanceof Boolean && !Boolean.parseBoolean(val.toString()))
                                                    vals[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat("0.0");
                                                else
                                                    vals[Integer.parseInt(ind[0].toString())-1] = Float.parseFloat(val.toString());
                                                ((Vec)environment.get(name).getValue()).setValues(vals);
                                                environment.get(name).setType(new Type(Type.Types.NUMERICO, "Vector"));
                                            }
                                            //Verifico si el tipo de dato de la expresión es integer y el del vector es booleano o integer
                                            else if((val instanceof Integer && (symbol.getType().getTypes() == Type.Types.BOOLEANO || symbol.getType().getTypes() == Type.Types.INTEGER)) ||
                                                    (val instanceof Boolean && symbol.getType().getTypes() == Type.Types.INTEGER)){
                                                Object vals[] = new Object[Integer.parseInt(ind[0].toString())]; //Contiene el vector nuevo total
                                                Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                                for(int i = 0; i< values.length; i++){
                                                    if(values[i] instanceof Boolean)
                                                        vals[i] = Boolean.parseBoolean(values[i].toString()) ? 1 : 0; //Convierto todos los valores al tipo de dato integer
                                                    else
                                                        vals[i] = Integer.parseInt(values[i].toString());
                                                }

                                                for(int i = values.length; i<Integer.parseInt(ind[0].toString()); i++){
                                                    vals[i] = 0;     //Llenamos los espacios sobrantes con el valor por defecto
                                                }

                                                if(val instanceof Boolean && Boolean.parseBoolean(val.toString()))
                                                    vals[Integer.parseInt(ind[0].toString())-1] = 1;
                                                else if(val instanceof Boolean && !Boolean.parseBoolean(val.toString()))
                                                    vals[Integer.parseInt(ind[0].toString())-1] = 0;
                                                else
                                                    vals[Integer.parseInt(ind[0].toString())-1] = Integer.parseInt(val.toString());
                                                ((Vec)environment.get(name).getValue()).setValues(vals);
                                                environment.get(name).setType(new Type(Type.Types.INTEGER, "Vector"));
                                            }
                                            //Verifico si el tipo de dato de la expresión booleano y el del vector es booleano
                                            else if(val instanceof Boolean && symbol.getType().getTypes() == Type.Types.BOOLEANO){
                                                Object vals[] = new Object[Integer.parseInt(ind[0].toString())]; //Contiene el vector nuevo total
                                                Object values []= ((Vec)symbol.getValue()).getValues(); //Contiene los valores del antiguo vector

                                                for(int i = 0; i< values.length; i++){
                                                    vals[i] = Boolean.parseBoolean(values[i].toString());   //Convierto todos los valores al tipo de dato boolean
                                                }

                                                for(int i = values.length; i<Integer.parseInt(ind[0].toString()); i++){
                                                    vals[i] = false;     //Llenamos los espacios sobrantes con el valor por defecto
                                                }

                                                vals[Integer.parseInt(ind[0].toString())-1] = Boolean.parseBoolean(val.toString());
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
                            TError error = new TError(name, "Semántico", "El índice debe ser de tipo vector y tener sólo un elemento", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Un vector no puede tener más de un índice a la hora de modificar", 0, 0);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "La expresión a asignar no es de tipo vector o es de tamaño mayor a 1", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //Si la variable es lista
            else if(symbol.getValue() instanceof ListStruct){
                if(indexes.size()==1){
                    Object i1= indexes.remove(0).execute(environment, LError);
                    if(i1 instanceof Vec && ((Vec)i1).getValues().length==1){
                        Object ind[] = ((Vec)i1).getValues();
                        Object expr = exp.execute(environment, LError);
                        if(expr instanceof Vec && ((Vec)expr).getValues().length==1){
                            LinkedList<Object> list = ((ListStruct)environment.get(name).getValue()).getValues();
                            if(ind[0] instanceof Integer){
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    for(int i=list.size(); i<Integer.parseInt(ind[0].toString()); i++){
                                        Object v[] = {"null"};
                                        list.add(new Vec(v));
                                    }
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((Vec)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((Vec)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es menor a 1", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }
                            else{
                                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else if(expr instanceof ListStruct && ((ListStruct)expr).getValues().size()==1){
                            LinkedList<Object> list = new LinkedList<>();
                            list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                            if(ind[0] instanceof Integer){
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    for(int i=list.size(); i<Integer.parseInt(ind[0].toString()); i++){
                                        Object v[] = {"null"};
                                        list.add(new Vec(v));
                                    }
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((ListStruct)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((ListStruct)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es menor a 1", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "Para modificación tipo1 no se puede agregar más de un elemento", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }
                    else if(i1 instanceof Vec2 && ((Vec2)i1).getValues().length==1){
                        Object ind[] = ((Vec2)i1).getValues();
                        Object expr = exp.execute(environment, LError);
                        if(expr instanceof Vec){
                            LinkedList<Object> list = ((ListStruct)environment.get(name).getValue()).getValues();
                            if(ind[0] instanceof Integer){
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    for(int i=list.size(); i<Integer.parseInt(ind[0].toString()); i++){
                                        Object v[] = {"null"};
                                        list.add(new Vec(v));
                                    }
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((Vec)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((Vec)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice es menor a 1", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }
                            else{
                                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else if(expr instanceof ListStruct){
                            LinkedList<Object> list = new LinkedList<>();
                            list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                            if(ind[0] instanceof Integer){
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    for(int i=list.size(); i<Integer.parseInt(ind[0].toString()); i++){
                                        Object v[] = {"null"};
                                        list.add(new Vec(v));
                                    }
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((ListStruct)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((ListStruct)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es menor a 1", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "modificación tipo2, la expresión no es vector o lista", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }
                    else{
                        TError error = new TError(name, "Semántico", "El índice no es un vector de tamaño 1", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }
                else if(indexes.size()==2){
                    Object i1= indexes.remove(0).execute(environment, LError);
                    Object i2= indexes.remove(0).execute(environment, LError);
                    if((i1 instanceof Vec && i2 instanceof Vec2) && (((Vec)i1).getValues().length==1 &&((Vec2)i2).getValues().length==1)
                       && (((Vec)i1).getValues()[0] instanceof Integer && ((Vec2)i2).getValues()[0] instanceof Integer)){
                        Object expr = exp.execute(environment, LError);
                        LinkedList<Object> list = new LinkedList<>();
                        list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                        if(expr instanceof Vec && ((Vec)expr).getValues().length==1){
                            int ind1 = Integer.parseInt(((Vec)i1).getValues()[0].toString());
                            
                            if(ind1>list.size()){
                                for(int i=list.size(); i<ind1; i++){
                                    Object a[] = {"null"};
                                    list.add(new Vec(a));
                                };
                                    
                                Object val[] = {Boolean.parseBoolean("false")};
                                int ind2 = Integer.parseInt(((Vec2)i2).getValues()[0].toString());
                                if(((Vec2)i2).getValues()[0] instanceof Integer && ind2>0){
                                    Object result[] = Casteo.llenar(val, ((Vec)expr).getValues()[0], ind2);
                                    list.set(ind1-1, new Vec(result));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else if(ind1<=list.size()&&ind1>0){
                                Object l = list.get(ind1-1);
                                if(l instanceof Vec){
                                    Object val[] = ((Vec)l).getValues();
                                    int ind2 = Integer.parseInt(((Vec2)i2).getValues()[0].toString());
                                    
                                    if(((Vec2)i2).getValues()[0] instanceof Integer && ind2>0){
                                        Object result[] = Casteo.llenar(val, ((Vec)expr).getValues()[0], ind2);
                                        list.set(ind1-1, new Vec(result));
                                        ((ListStruct)environment.get(name).getValue()).setValues(list);
                                    }else{
                                        TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                        LError.add(error);

                                        return error;
                                    }
                                }else{
                                    TError error = new TError(name, "Semántico", "No se puede modificar porque el valor no es vector", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else if(expr instanceof ListStruct){
                            int ind1 = Integer.parseInt(((Vec)i1).getValues()[0].toString());
                            Object a[] = {"null"};
                            
                            if(ind1>list.size()){
                                for(int i=list.size(); i<ind1; i++){
                                    list.add(new Vec(a));
                                };
                                
                                int ind2 = Integer.parseInt(((Vec2)i2).getValues()[0].toString());
                                if(((Vec2)i2).getValues()[0] instanceof Integer && ind2>0){
                                    LinkedList<Object> v = new LinkedList<>();
                                    for(int i=0; i<ind2; i++){
                                        v.add(new Vec(a));
                                    }
                                    v.set(ind2-1, (ListStruct)expr);
                                    list.set(ind1-1, new ListStruct(v));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else if(ind1<=list.size()&&ind1>0){
                                int ind2 = Integer.parseInt(((Vec2)i2).getValues()[0].toString());
                                if(((Vec2)i2).getValues()[0] instanceof Integer && ind2>0){
                                    LinkedList<Object> v = new LinkedList<>();
                                    for(int i=0; i<ind2; i++){
                                        v.add(new Vec(a));
                                    }
                                    v.set(ind2-1, (ListStruct)expr);
                                    list.set(ind1-1, new ListStruct(v));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "La expresión debe ser vector de un valor", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }
                    else if((i1 instanceof Vec2 && i2 instanceof Vec) && (((Vec2)i1).getValues().length==1 &&((Vec)i2).getValues().length==1)
                       && (((Vec2)i1).getValues()[0] instanceof Integer && ((Vec)i2).getValues()[0] instanceof Integer)){
                        Object expr = exp.execute(environment, LError);
                        LinkedList<Object> list = new LinkedList<>();
                        list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                        if(expr instanceof Vec && ((Vec)expr).getValues().length==1){
                            int ind1 = Integer.parseInt(((Vec2)i1).getValues()[0].toString());
                            
                            if(ind1>list.size()){
                                for(int i=list.size(); i<ind1; i++){
                                    Object a[] = {"null"};
                                    list.add(new Vec(a));
                                };
                                    
                                Object val[] = {Boolean.parseBoolean("false")};
                                int ind2 = Integer.parseInt(((Vec)i2).getValues()[0].toString());
                                if(((Vec)i2).getValues()[0] instanceof Integer && ind2>0){
                                    Object result[] = Casteo.llenar(val, ((Vec)expr).getValues()[0], ind2);
                                    list.set(ind1-1, new Vec(result));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else if(ind1<=list.size()&&ind1>0){
                                Object l = list.get(ind1-1);
                                if(l instanceof Vec){
                                    Object val[] = ((Vec)l).getValues();
                                    int ind2 = Integer.parseInt(((Vec)i2).getValues()[0].toString());
                                    
                                    if(((Vec)i2).getValues()[0] instanceof Integer && ind2>0){
                                        Object result[] = Casteo.llenar(val, ((Vec)expr).getValues()[0], ind2);
                                        list.set(ind1-1, new Vec(result));
                                        ((ListStruct)environment.get(name).getValue()).setValues(list);
                                    }else{
                                        TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                        LError.add(error);

                                        return error;
                                    }
                                }
                                else{
                                    TError error = new TError(name, "Semántico", "No se puede modificar porque el valor no es vector", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else if(expr instanceof ListStruct && ((ListStruct)expr).getValues().size()==1){
                            int ind1 = Integer.parseInt(((Vec2)i1).getValues()[0].toString());
                            Object a[] = {"null"};
                            
                            if(ind1>list.size()){
                                for(int i=list.size(); i<ind1; i++){
                                    list.add(new Vec(a));
                                };
                                
                                int ind2 = Integer.parseInt(((Vec)i2).getValues()[0].toString());
                                if(((Vec)i2).getValues()[0] instanceof Integer && ind2>0){
                                    LinkedList<Object> v = new LinkedList<>();
                                    for(int i=0; i<ind2; i++){
                                        v.add(new Vec(a));
                                    }
                                    v.set(ind2-1, (ListStruct)expr);
                                    list.set(ind1-1, new ListStruct(v));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else if(ind1<=list.size()&&ind1>0){
                                int ind2 = Integer.parseInt(((Vec)i2).getValues()[0].toString());
                                if(((Vec)i2).getValues()[0] instanceof Integer && ind2>0){
                                    LinkedList<Object> v = new LinkedList<>();
                                    for(int i=0; i<ind2; i++){
                                        v.add(new Vec(a));
                                    }
                                    v.set(ind2-1, (ListStruct)expr);
                                    list.set(ind1-1, new ListStruct(v));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", 0, 0);
                                    LError.add(error);

                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "Error en la expresión", 0, 0);
                            LError.add(error);

                            return error;
                        } 
                    }
                    else{
                        TError error = new TError(name, "Semántico", "Los índices deben ser vectores de un valor integer", 0, 0);
                        LError.add(error);

                        return error;
                    }
                }
            }
            else if(symbol.getValue() instanceof Mat){
                Mat mat = (Mat)symbol.getValue();
                //Verifico que la lista de índices sólo traiga un valor
                if(indexes.size()==1){
                    //Verifico que el índice sea un vector de tipo integer de un sólo valor que no se pase del límite de la matriz
                    Object index = indexes.remove(0).execute(environment, LError);
                    if(index instanceof Vec && ((Vec)index).getValues().length==1 &&
                            ((Vec)index).getValues()[0] instanceof Integer && 
                                Integer.parseInt((((Vec)index).getValues()[0]).toString())<=(mat.row*mat.col) &&
                                   Integer.parseInt((((Vec)index).getValues()[0]).toString())>0){
                        int ind = Integer.parseInt((((Vec)index).getValues()[0]).toString());
                        //Verifico que la expresion sea un vector de un sólo valor
                        if(value instanceof Vec && ((Vec)value).getValues().length==1){
                            int cont = 0;
                            for(int i=0; i<mat.col; i++){
                                for(int j=0; j<mat.row; j++){
                                    if(cont==(ind-1)){
                                        mat.getValues()[j][i] = ((Vec)value).getValues()[0];
                                        ((Mat)environment.get(name).getValue()).setValues(mat.getValues());
                                    }
                                    cont++;
                                }
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "La expresión a asignar no es correcta", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Error en el índice de la matriz", 0, 0);
                        LError.add(error);
                        
                        return error; 
                    }
                }else{
                    TError error = new TError(name, "Semántico", "Una matriz no puede tener más de un índice", 0, 0);
                    LError.add(error);
                    
                    return error;
                }
            }
            //Verifico que la variable sea de tipo Arr
            else if(symbol.getValue() instanceof Arr){
                Arr arr = ((Arr)environment.get(name).getValue());
                int tam[] = new int[arr.dim.length+2];
                tam[0] = arr.row;
                tam[1] = arr.col;
                for(int i=2; i<tam.length;i++){
                    tam[i] = arr.dim[i-2];
                }
                int in = 0;
                Object result = null;
                LinkedList<Object> index = new LinkedList<>();
                
                //Verifico que se accesa a todas las dimensiones
                if(indexes.size() == (arr.dim.length+2)){
                    //Verifico que cada indice sea de tipo integer y de tamaño uno
                    boolean flag = true;
                    for(ASTNode ind: indexes){
                        index.add(ind.execute(environment, LError));
                        if(index.getLast() instanceof Vec && ((Vec)index.getLast()).getValues().length==1 && ((Vec)index.getLast()).getValues()[0] instanceof Integer){
                        }else{
                            flag = false;
                            break;
                        }
                    }
                    
                    if(flag){
                        boolean f = true;
                        if(index.size() == 3){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i1>0 && i2>0 && i3>0){
                                int inds[] = {i1-1,i2-1,i3-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }else if(index.size() == 4){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            int i4 = Integer.parseInt((((Vec)index.get(3)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i4<=arr.dim[1] && i1>0 && i2>0 && i3>0 && i4>0){
                                int inds[] = {i1-1,i2-1,i3-1,i4-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }else if(index.size() == 5){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            int i4 = Integer.parseInt((((Vec)index.get(3)).getValues()[0]).toString());
                            int i5 = Integer.parseInt((((Vec)index.get(4)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i4<=arr.dim[1] && i5<=arr.dim[1] && i1>0 && i2>0 && i3>0 && i4>0 && i5>0){
                                int inds[] = {i1-1,i2-1,i3-1,i4-1,i5-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }
                        
                        if(f){
                            result = arr.getData().get(in);
                            LinkedList<Object> res= ((Arr)environment.get(name).getValue()).getData();
                            if(result instanceof Vec && value instanceof Vec){
                                res.set(in, (Vec)value);
                                ((Arr)environment.get(name).getValue()).setData(res);
                            }else if(result instanceof ListStruct && value instanceof ListStruct){
                                res.set(in, (ListStruct)value);
                                ((Arr)environment.get(name).getValue()).setData(res);
                            }else{
                                TError error = new TError(name, "Semántico", "La expresión no es del tipo del arreglo", 0, 0);
                                LError.add(error);
                                return error;
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del arreglo", 0, 0);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Los índices deben ser vectores de tipo integer y tamaño 1", 0, 0);
                        LError.add(error);
                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "La variable es de tipo arreglo y el número de índices no es igual al de sus dimensiones", 0, 0);
                    LError.add(error);
                    return error;
                }
            }
        }else{
            TError error = new TError(name, "Semántico", "La variable no existe", 0, 0);
            LError.add(error);

            return error;
        }
        
        return "asignación correcta\n";
    }
}
