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
    private int row;
    private int column;

    public StructAssig(String name, ASTNode exp, LinkedList<ASTNode> indexes, int row, int column) {
        super();
        this.name = name;
        this.exp = exp;
        this.indexes = indexes;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object value = exp.execute(environment, LError);    //obtenemos el valor de la expresión
        
        //Verificamos que la variable exista
        if(environment.get(name) != null){
            Symbol symbol = environment.get(name);  //Guardamos el símbolo en una variable
            
            //Verificamos que sea vector
            if(symbol.getValue() instanceof Vec){
                //Lista de índices sea igual a 1
                if(indexes.size()==1){
                    //El índice debe ser un vector de tipo integer de tamaño 1
                    Object ind = indexes.get(0).execute(environment, LError);
                    if(ind instanceof Vec && ((Vec)ind).getValues()[0] instanceof Integer && ((Vec)ind).getValues().length==1){
                        //La expresión es un vector de tamaño 1
                        if(value instanceof Vec && ((Vec)value).getValues().length==1){
                            //El índice es menor o igual al tamaño del vector
                            Object index[] = ((Vec)ind).getValues();
                            int tamV = ((Vec)symbol.getValue()).getValues().length;
                            if(Integer.parseInt(index[0].toString())<=tamV && Integer.parseInt(index[0].toString())>0){
                                LinkedList<Object> list = new LinkedList<>();
                                Object data[] = ((Vec)symbol.getValue()).getValues();
                                data[Integer.parseInt(index[0].toString())-1] = ((Vec)value).getValues()[0];
                                list.add(new Vec(data));
                                ((Vec)environment.get(name).getValue()).setValues(((Vec)Casteo.cast(list)).getValues());
                                environment.get(name).setType(Casteo.setType(((Vec)Casteo.cast(list)).getValues()[0]));
                                return null;
                            }else if(Integer.parseInt(index[0].toString())>tamV){
                                Object result[] = Casteo.llenar(((Vec)symbol.getValue()).getValues(), ((Vec)value).getValues()[0], Integer.parseInt(index[0].toString()));
                                ((Vec)environment.get(name).getValue()).setValues(result);
                                environment.get(name).setType(Casteo.setType(result[0]));
                                return null;
                            }else{
                                TError error = new TError(name, "Semántico", "Error en el índice del vector", row, column);
                                LError.add(error);
                                return error;
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El valor a asignar tiene más de un valor", row, column);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El indice debe ser de tipo integer y tener sólo un valor", row, column);
                        LError.add(error);
                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "Para modificar un vector sólo puede haber un índice", row, column);
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
                        if(ind[0] instanceof Integer){
                            Object expr = exp.execute(environment, LError);
                            if(expr instanceof Vec && ((Vec)expr).getValues().length==1){
                                LinkedList<Object> list = ((ListStruct)environment.get(name).getValue()).getValues();
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    ((ListStruct)environment.get(name).getValue()).setValues(Casteo.llenarList(list, expr, Integer.parseInt(ind[0].toString())-1));
                                    return null;
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), (expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice es menor a 1", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }
                            else if(expr instanceof ListStruct && ((ListStruct)expr).getValues().size()==1){
                                LinkedList<Object> list = new LinkedList<>();
                                list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    ((ListStruct)environment.get(name).getValue()).setValues(Casteo.llenarList(list, expr, Integer.parseInt(ind[0].toString())-1));
                                    return null;
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), (expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice es menor a 1", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }
                            else{
                                TError error = new TError(name, "Semántico", "Para modificación tipo1 no se puede agregar más de un elemento", row, column);
                                LError.add(error);
                                return error;
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El índice no es de tipo integer", row, column);
                            LError.add(error);
                            return error;
                        }
                    }
                    else if(i1 instanceof Vec2 && ((Vec2)i1).getValues().length==1){
                        Object ind[] = ((Vec2)i1).getValues();
                        if(ind[0] instanceof Integer){
                            Object expr = exp.execute(environment, LError);
                            if(expr instanceof Vec){
                                LinkedList<Object> list = ((ListStruct)environment.get(name).getValue()).getValues();
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                        ((ListStruct)environment.get(name).getValue()).setValues(Casteo.llenarList(list, expr, Integer.parseInt(ind[0].toString())-1));
                                        return null;
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                        list.set((Integer.parseInt(ind[0].toString())-1), ((Vec)expr));
                                        ((ListStruct)environment.get(name).getValue()).setValues(list);
                                        return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice es menor a 1", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }
                            else if(expr instanceof ListStruct){
                                LinkedList<Object> list = new LinkedList<>();
                                list.addAll(((ListStruct)environment.get(name).getValue()).getValues());
                                if(Integer.parseInt(ind[0].toString())>list.size()){
                                    ((ListStruct)environment.get(name).getValue()).setValues(Casteo.llenarList(list, expr, Integer.parseInt(ind[0].toString())-1));
                                    return null;
                                }else if(Integer.parseInt(ind[0].toString())<=list.size() && Integer.parseInt(ind[0].toString())>0){
                                    list.set((Integer.parseInt(ind[0].toString())-1), ((ListStruct)expr));
                                    ((ListStruct)environment.get(name).getValue()).setValues(list);
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice es menor a 1", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }
                            else{
                                TError error = new TError(name, "Semántico", "modificación tipo2, la expresión no es vector o lista", row, column);
                                LError.add(error);
                                return error;
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El índice no es de tipo integer", row, column);
                            LError.add(error);
                            return error;
                        }
                    }
                    else{
                        TError error = new TError(name, "Semántico", "El índice no es un vector de tamaño 1", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
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
                                        return null;
                                    }else{
                                        TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
                                        LError.add(error);
                                        return error;
                                    }
                                }else{
                                    TError error = new TError(name, "Semántico", "No se puede modificar porque el valor no es vector", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", row, column);
                                LError.add(error);
                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "La expresión debe ser vector de un valor", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
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
                                        return null;
                                    }else{
                                        TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
                                        LError.add(error);
                                        return error;
                                    }
                                }
                                else{
                                    TError error = new TError(name, "Semántico", "No se puede modificar porque el valor no es vector", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
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
                                    return null;
                                }else{
                                    TError error = new TError(name, "Semántico", "El índice no es integer mayor a 0", row, column);
                                    LError.add(error);
                                    return error;
                                }
                            }else{
                                TError error = new TError(name, "Semántico", "El índice es menor a 0", row, column);
                                LError.add(error);
                                return error;
                            }
                        }
                        else{
                            TError error = new TError(name, "Semántico", "Error en la expresión", row, column);
                            LError.add(error);
                            return error;
                        } 
                    }
                    else{
                        TError error = new TError(name, "Semántico", "Los índices deben ser vectores de un valor integer", row, column);
                        LError.add(error);
                        return error;
                    }
                }
            }
            //Verifico si la expresión es Matriz
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
                            return null;
                        }else{
                            TError error = new TError(name, "Semántico", "La expresión a asignar no es correcta", row, column);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Error en el índice de la matriz", row, column);
                        LError.add(error);
                        return error; 
                    }
                }else{
                    TError error = new TError(name, "Semántico", "Una matriz no puede tener más de un índice", row, column);
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
                                return null;
                            }else if(result instanceof ListStruct && value instanceof ListStruct){
                                res.set(in, (ListStruct)value);
                                ((Arr)environment.get(name).getValue()).setData(res);
                                return null;
                            }else{
                                TError error = new TError(name, "Semántico", "La expresión no es del tipo del arreglo", row, column);
                                LError.add(error);
                                return error;
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del arreglo", row, column);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Los índices deben ser vectores de tipo integer y tamaño 1", row, column);
                        LError.add(error);
                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "La variable es de tipo arreglo y el número de índices no es igual al de sus dimensiones", row, column);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "No representa ninguna estructura", row, column);
                LError.add(error);

                return error;
            }
        }else{
            TError error = new TError(name, "Semántico", "La variable no existe", row, column);
            LError.add(error);

            return error;
        }
        
        return null;
    }
}
