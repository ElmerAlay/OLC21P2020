package instructions;

import NativeFunctions.Print;
import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class CallFunc implements ASTNode{
    private String name;
    private LinkedList<ASTNode> lparam;

    public CallFunc(String name, LinkedList<ASTNode> lparam) {
        super();
        this.name = name;
        this.lparam = lparam;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verifico que sea alguna función nativa
        if(name.equals("print")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1){
                return new Print(lparam.get(0)).execute(environment, LError);
            }else{
                TError error = new TError(name, "Semántico", "La función Print no puede tener varios argumentos, sólo 1", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        return null;
    }
    
}
