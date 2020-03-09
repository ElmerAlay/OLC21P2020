package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Tern implements ASTNode{
    private ASTNode condition;
    private ASTNode expTrue;
    private ASTNode expFalse;

    public Tern(ASTNode condition, ASTNode expTrue, ASTNode expFalse) {
        super();
        this.condition = condition;
        this.expTrue = expTrue;
        this.expFalse = expFalse;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object cond = condition.execute(environment, LError);
        Object expT = expTrue.execute(environment, LError);
        Object expF = expFalse.execute(environment, LError);
        
        //Comprobamos que la condición sea un vector de tipo booleano
        if(cond instanceof Vec && ((Vec)cond).getValues()[0] instanceof Boolean){
            //Verificamos si todo el vector es true
            Object v[] = ((Vec)cond).getValues();
            boolean flag = true;
            for(int i=0; i<v.length; i++){
                if(Boolean.parseBoolean(v[i].toString())){
                }else{
                    flag = false;
                    break;
                }
            }
            
            if(flag){
                if((expT instanceof Vec || expT instanceof ListStruct || expT instanceof Mat || expT instanceof Arr) && (expF instanceof Vec || expF instanceof ListStruct || expF instanceof Mat || expF instanceof Arr))
                    return expT;
                TError error = new TError("?", "Semántico", "La expresión verdadera o falsa no es correcta", 0, 0);
                LError.add(error);

                return error;
            }else{
                if((expT instanceof Vec || expT instanceof ListStruct || expT instanceof Mat || expT instanceof Arr) && (expF instanceof Vec || expF instanceof ListStruct || expF instanceof Mat || expF instanceof Arr))
                    return expF;
                TError error = new TError("?", "Semántico", "La expresión verdadera o falsa no es correcta", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("?", "Semántico", "La condición del operador ternario no es de tipo booleano", 0, 0);
        LError.add(error);

        return error;
    }
}
