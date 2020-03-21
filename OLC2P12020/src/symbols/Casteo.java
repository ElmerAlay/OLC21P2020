/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbols;

import java.util.LinkedList;

/**
 *
 * @author junio
 */
public class Casteo {
    public static Object cast(LinkedList<Object> list){
        boolean flagList = false, flagString=false, flagNumeric=false, flagInteger=false, flagBoolean=false;
        
        for(Object exp: list){
            if(exp instanceof ListStruct){
                flagList = true;
                break;
            }
        }
        for(Object exp: list){
            if(exp instanceof Vec){
                Object vec[] = ((Vec)exp).getValues();
                for(int i=0; i<vec.length; i++){
                    if(vec[i] instanceof String){
                        flagString = true;
                        break;
                    }else if(vec[i] instanceof Double){
                        flagNumeric = true;
                    }else if(vec[i] instanceof Integer){
                        flagInteger = true;
                    }else
                        flagBoolean = true;
                }
            }
        }
        
        //Todos los elementos pasaran a ser una lista
        if(flagList){
            LinkedList<Object> values = new LinkedList<>();
            for(Object exp: list){
                if(!(exp instanceof ListStruct))
                    values.add(exp);
                else{
                    for(Object e: ((ListStruct)exp).getValues()){
                        values.add(e);
                    }
                }
            }
            
            return new ListStruct(values);
        }
        //Todos los elementos pasarán a ser un vector casteado
        else {
            int cont = 0;
            for(int i=0; i<list.size(); i++){
                cont += ((Vec)list.get(i)).getValues().length;
            }
            Object values[] = new Object[cont];
            
            cont = 0;
            for(Object l: list){
                Object vals[] = ((Vec)l).getValues();
                for(int i=0; i<vals.length; i++){
                    if(flagString)
                        values[cont++] = vals[i].toString();
                    else{
                        if(flagNumeric){
                            if(vals[i] instanceof Boolean)
                                values[cont++] = Boolean.parseBoolean(vals[i].toString())?Double.parseDouble("1.0"):Double.parseDouble("0.0");
                            else
                                values[cont++] = Double.parseDouble(vals[i].toString());
                        }else if(flagInteger)
                            if(vals[i] instanceof Boolean)
                                values[cont++] = Boolean.parseBoolean(vals[i].toString())? 1 : 0;
                            else
                                values[cont++] = Integer.parseInt(vals[i].toString());
                        else
                            values[cont++] = Boolean.parseBoolean(vals[i].toString());
                    }
                }
            }
            
            return new Vec(values);
        }
    }
    
    public static Object[] llenar(Object val[], Object v, int index){
        if(index<=val.length){
            for(int i=0; i<val.length;i++){
                if(v instanceof String || val[0] instanceof String){
                    val[i] = val[i].toString();
                    val[index-1] = v.toString();
                }else{
                    if(v instanceof Double || val[0] instanceof Double){
                        if(val[i] instanceof Boolean){
                            val[i]=Boolean.parseBoolean(val[i].toString())?Double.parseDouble("1.0"):Double.parseDouble("0.0");
                        }else{
                            val[i] = Double.parseDouble(val[i].toString());
                        }
                        
                        if(v instanceof Boolean){
                            val[index-1]=Boolean.parseBoolean(v.toString())?Double.parseDouble("1.0"):Double.parseDouble("0.0");
                        }else{
                            val[index-1] =Double.parseDouble(v.toString());
                        }
                    }else if((v instanceof Integer && (val[0] instanceof Integer||val[0] instanceof Boolean)) ||
                             (v instanceof Boolean && val[0] instanceof Integer)){
                        if(val[i] instanceof Boolean){
                            val[i]=Boolean.parseBoolean(val[i].toString())?1:0;
                        }else{
                            val[i] = Integer.parseInt(val[i].toString());
                        }
                        if(v instanceof Boolean){
                            val[index-1]=Boolean.parseBoolean(v.toString())?1:0;
                        }else{
                            val[index-1] =Integer.parseInt(v.toString());
                        }
                    }else if(v instanceof Boolean && val[0] instanceof Boolean){
                        val[i] = Boolean.parseBoolean(val[i].toString());
                        val[index-1]=Boolean.parseBoolean(v.toString());
                    }
                } 
            }
            
            return val;
        }else{
            Object val2[] = new Object[index];
            
            for(int i=0; i<index; i++){
                if(v instanceof String || val[0] instanceof String){
                    val2[i] = "null";
                }else{
                    if(v instanceof Double || val[0] instanceof Double){
                        val2[i] = Double.parseDouble("0.0");
                    }else if((v instanceof Integer && (val[0] instanceof Integer||val[0] instanceof Boolean)) ||
                             (v instanceof Boolean && val[0] instanceof Integer)){
                        val2[i] = 0;
                    }else if(v instanceof Boolean && val[0] instanceof Boolean){
                        val2[i] = false;
                    }
                }
            }
            
            for(int i=0; i<val.length; i++){
                if(val2[0] instanceof String){
                    val2[i] = val[i].toString();
                    val2[index-1] = v.toString();
                }else{
                    if(val2[0] instanceof Double){
                        if(val[i] instanceof Boolean){
                            val2[i]=Boolean.parseBoolean(val[i].toString())?Double.parseDouble("1.0"):Double.parseDouble("0.0");
                        }else{
                            val2[i] = Double.parseDouble(val[i].toString());
                        }
                        if(v instanceof Boolean){
                            val2[index-1]=Boolean.parseBoolean(v.toString())?Double.parseDouble("1.0"):Double.parseDouble("0.0");
                        }else{
                            val2[index-1] =Double.parseDouble(v.toString());
                        }
                    }else if(val2[0] instanceof Integer){
                        if(val[i] instanceof Boolean){
                            val2[i]=Boolean.parseBoolean(val[i].toString())?1:0;
                        }else{
                            val2[i] = Integer.parseInt(val[i].toString());
                        }
                        if(v instanceof Boolean){
                            val2[index-1]=Boolean.parseBoolean(v.toString())?1:0;
                        }else{
                            val2[index-1] =Integer.parseInt(v.toString());
                        }
                    }else if(val2[0] instanceof Boolean){
                        val2[i] = Boolean.parseBoolean(val[i].toString());
                        val2[index-1]=Boolean.parseBoolean(v.toString());
                    }
                }
            }
            return val2;
        }
    }
    
    public static LinkedList<Object> llenarList(LinkedList<Object> list, Object v, int index){
        for(int i=list.size(); i<index-1; i++){
            Object value[] = {"null"};
            list.add(new Vec(value));
        }
        list.add(v);
        return list;
    }
    
    public static Object[] convertDouble(Object[] v){
        for(int i=0; i<v.length; i++){
            v[i] = Double.parseDouble(v[i].toString());
        }
        return v;
    }
    
    public static Type setType(Object v){
        if(v instanceof String){
            return new Type(Type.Types.STRING, "Vector");
        }
        if(v instanceof Double){
            return new Type(Type.Types.NUMERICO, "Vector");
        }
        if(v instanceof Integer){
            return new Type(Type.Types.INTEGER, "Vector");
        }
        if(v instanceof Boolean){
            return new Type(Type.Types.BOOLEANO, "Vector");
        }
        if(v instanceof ListStruct){
            return new Type(Type.Types.LISTA, "Lista");
        }
        if(v instanceof Mat){
            if(((Mat)v).getValues()[0][0] instanceof String)
                return new Type(Type.Types.STRING, "Matriz");
            if(((Mat)v).getValues()[0][0] instanceof Double){
                return new Type(Type.Types.NUMERICO, "Matriz");
            }
            if(((Mat)v).getValues()[0][0] instanceof Integer){
                return new Type(Type.Types.INTEGER, "Matriz");
            }
            if(((Mat)v).getValues()[0][0] instanceof Boolean){
                return new Type(Type.Types.BOOLEANO, "Matriz");
            }
        }
        if(v instanceof Arr){
            if(((Arr)v).getData().get(0) instanceof ListStruct)
                return new Type(Type.Types.LISTA, "Arreglo");
            else if(((Arr)v).getData().get(0) instanceof Vec){
                //Modificamos el tipo primitivo 
                if(((Vec)((Arr)v).getData().get(0)).getValues()[0] instanceof String)
                    return new Type(Type.Types.STRING, "Arreglo");
                else if (((Vec)((Arr)v).getData().get(0)).getValues()[0] instanceof Double)
                    return new Type(Type.Types.NUMERICO, "Arreglo");
                else if (((Vec)((Arr)v).getData().get(0)).getValues()[0] instanceof Integer)
                    return new Type(Type.Types.INTEGER, "Arreglo");
                else if (((Vec)((Arr)v).getData().get(0)).getValues()[0] instanceof Boolean)
                    return new Type(Type.Types.BOOLEANO, "Arreglo");
            }
        }
        
        return new Type(Type.Types.MATRIZ, "Error");
    }
}
