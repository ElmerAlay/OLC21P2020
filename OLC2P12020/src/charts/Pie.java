package charts;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Pie implements ASTNode{
    private LinkedList<ASTNode> params;

    public Pie(LinkedList<ASTNode> params) {
        super();
        this.params = params;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        if(params.get(0) instanceof Vec){
            if(((Vec)params.get(0)).getValues()[0] instanceof Integer || ((Vec)params.get(0)).getValues()[0] instanceof Float){
                if(params.get(1) instanceof Vec && ((Vec)params.get(1)).getValues()[0] instanceof String){
                    Object data[] = ((Vec)params.get(0)).getValues();
                    Object labels[] = ((Vec)params.get(1)).getValues();
                    //Verifico que el título sea un vector de tamaño 1
                    if(params.get(2) instanceof Vec && ((Vec)params.get(2)).getValues().length==1 && ((Vec)params.get(2)).getValues()[0] instanceof String){
                        String main = (((Vec)params.get(0)).getValues()[0]).toString();
                        //Verifico que los vectores sean del mismo tamaño
                        if(data.length == labels.length){
                            
                        }
                    }else{
                        TError error = new TError("Pie", "Semántico", "El tercer argumento no es de tipo string o tiene más de un argumento", 0, 0);
                        LError.add(error);

                        return error;
                    } 
                }else{
                    TError error = new TError("Pie", "Semántico", "El segundo argumento no es de tipo string", 0, 0);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("Pie", "Semántico", "El tipo del primer argumento debe ser numerico o integer", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("Pie", "Semántico", "El primer argumento debe ser un vector", 0, 0);
        LError.add(error);

        return error;
    }
}
