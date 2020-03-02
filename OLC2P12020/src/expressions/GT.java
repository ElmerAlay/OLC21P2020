package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class GT implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public GT(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = this.op1.execute(environment, LError);
        Object op2 = this.op2.execute(environment, LError);
        
        //Primero verifico que los 2 operadores sean de tipo vector
        if(op1 instanceof Vec && op2 instanceof Vec){
            Object vec1[] = ((Vec)op1).getValues();
            Object vec2[] = ((Vec)op2).getValues();
            
            //Verifico si el 1er vector es de tamaño 1 y el 2do también
            if (vec1.length==1 && vec2.length==1) {
                if(vec1[0] instanceof String && vec2[0] instanceof String){
                    int res = vec1[0].toString().compareTo(vec2[0].toString());
                    Object result[] = { res > 0 };
                    return new Vec(result);
                }else if((vec1[0] instanceof Float || vec1[0] instanceof Integer) && (vec2[0] instanceof Float || vec2[0] instanceof Integer)){
                    int res = Float.compare(Float.parseFloat(vec1[0].toString()), Float.parseFloat(vec2[0].toString()));
                    Object result[] = { res>0 };
                    return new Vec(result);
                }else{
                    TError error = new TError(">", "Semántico", "no se pueden comparar esos 2 tipos de datos", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //Verifico que los 2 vectores sean del mismo tamaño pero que no sean 1
            else if(vec1.length==vec2.length){
                boolean flag = true;
                Object result[] = new Object[vec1.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec1.length; i++){
                    if(vec1[i] instanceof String && vec2[i] instanceof String){
                        int res = vec1[i].toString().compareTo(vec2[i].toString());
                        result[i] = res>0;
                    }else if((vec1[i] instanceof Float || vec1[i] instanceof Integer) && (vec2[i] instanceof Float || vec2[i] instanceof Integer)){
                        int res = Float.compare(Float.parseFloat(vec1[i].toString()), Float.parseFloat(vec2[i].toString()));
                        result[i] = res>0;
                    }else {
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError(">", "Semántico", "no se pueden comparar esos 2 tipos de datos", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //verifico que el 1er vector sea 1 y el segundo sea de mayor tamaño
            else if(vec1.length==1 && vec2.length>1){
                boolean flag = true;
                Object result[] = new Object[vec2.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec2.length; i++){
                    if(vec1[0] instanceof String && vec2[i] instanceof String){
                        int res = vec1[0].toString().compareTo(vec2[i].toString());
                        result[i] = res>0;
                    }else if((vec1[0] instanceof Float || vec1[0] instanceof Integer) && (vec2[i] instanceof Float || vec2[i] instanceof Integer)){
                        int res = Float.compare(Float.parseFloat(vec1[0].toString()), Float.parseFloat(vec2[i].toString()));
                        result[i] = res>0;
                    }else {
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError(">", "Semántico", "no se pueden comparar esos 2 tipos de datos", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //Verifico que el primer vector sea de tamaño n y el vector 2 sea tamaño 1
            else if(vec1.length>1 && vec2.length==1){
                boolean flag = true;
                Object result[] = new Object[vec1.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec1.length; i++){
                    if(vec1[i] instanceof String && vec2[0] instanceof String){
                        int res = vec1[i].toString().compareTo(vec2[0].toString());
                        result[i] = res>0;
                    }else if((vec1[i] instanceof Float || vec1[i] instanceof Integer) && (vec2[0] instanceof Float || vec2[0] instanceof Integer)){
                        int res = Float.compare(Float.parseFloat(vec1[i].toString()), Float.parseFloat(vec2[0].toString()));
                        result[i] = res>0;
                    }else {
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError(">", "Semántico", "no se pueden comparar esos 2 tipos de datos", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //Por último es el caso en el que los vectores son de distinto tamaño
            else{
                TError error = new TError(">", "Semántico", "no se pueden comparar 2 vectores de distinto tamaño", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError(">", "Semántico", "no se puede comparar esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}