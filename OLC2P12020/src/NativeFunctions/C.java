package NativeFunctions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class C implements ASTNode{ 
    private LinkedList<ASTNode> lexp;

    public C(LinkedList<ASTNode> lexp) {
        super();
        this.lexp = lexp;
    }
    
    public Object cast(LinkedList<Object> list){
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
                if(vec[0] instanceof String){
                    flagString = true;
                    break;
                }else if(vec[0] instanceof Float){
                    flagNumeric = true;
                }else if(vec[0] instanceof Integer){
                    flagInteger = true;
                }else
                    flagBoolean = true;
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
                                values[cont++] = Boolean.parseBoolean(vals[i].toString())?Float.parseFloat("1.0"):Float.parseFloat("0.0");
                            else
                                values[cont++] = Float.parseFloat(vals[i].toString());
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
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        LinkedList<Object> list = new LinkedList<>();
        for(ASTNode exp : lexp){
            list.add(exp.execute(environment, LError));
        }
        
        for(Object exp : list){
            if(!(exp instanceof Vec) && !(exp instanceof ListStruct)){
                TError error = new TError("C", "Semántico", "La función c solo acepta vectores y listas", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        return cast(list);
    }
}
