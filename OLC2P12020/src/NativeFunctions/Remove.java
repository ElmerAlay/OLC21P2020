package NativeFunctions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Remove implements ASTNode{
    private ASTNode exp1;
    private ASTNode exp2;
    private int row;
    private int column;

    public Remove(ASTNode exp1, ASTNode exp2, int row, int column) {
        super();
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = exp1.execute(environment, LError);
        Object op2 = exp2.execute(environment, LError);
        
        //verificamos si las expresiones son vectores
        if(op1 instanceof Vec && op2 instanceof Vec){
            Object values[] = ((Vec)op1).getValues();
            Object values2[] = ((Vec)op2).getValues();
            //Verificamos que ambos vectores sean de tamaño 1
            if(values.length==1 && values2.length==1){
                //Verificar que sea de tipo String
                if(values[0] instanceof String && values2[0] instanceof String){
                    Object tam[] = {values[0].toString().replaceAll(values2[0].toString(), "")};
                    //tam[0] = tam[0].toString().replace(" ", "");
                    return new Vec(tam);
                }else{
                    TError error = new TError("remove", "Semántico", "Los vectores dentro de la función no son de tipo String", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("remove", "Semántico", "Los vectores dentro de la función deben ser tamaño 1", row, column);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("remove", "Semántico", "La expresión dentro de la función no es válida", row, column);
        LError.add(error);

        return error;
    }
    
}
