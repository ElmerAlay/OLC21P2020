package instructions;

import View.MainWindow;
import abstracto.ASTNode;
import abstracto.TError;
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

/**
 *
 * @author junio
 */
public class IdParam implements ASTNode{
    private String name;
    private ASTNode exp;
    private int row;
    private int column;

    public IdParam(String name, ASTNode exp, int row, int column) {
        this.name = name;
        this.exp = exp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        if(exp != null){
            Object op = exp.execute(environment, LError);
            Type type = new Type(Type.Types.BOOLEANO,"Vector");
            if(op instanceof Vec){
                type = Casteo.setType(((Vec)op).getValues()[0]);
            }else{
                type = Casteo.setType(op);
            }
            
            if(!type.getTypeObject().equals("Error")){
                environment.put(new Symbol(type, name, op, row, column, environment.getName()));
                MainWindow.general.put(new Symbol(type, name, op, row, column, environment.getName()));
                return null;
            }else{
                TError error = new TError(name, "Semántico", "Error en la expresión del parámetro de la función", row, column);
                LError.add(error);
                return error;
            }
        }else{
            environment.put(new Symbol(new Type(Type.Types.BOOLEANO, "Vector"), name, null, row, column, environment.getName()));
            MainWindow.general.put(new Symbol(new Type(Type.Types.BOOLEANO, "Vector"), name, null, row, column, environment.getName()));
            return null;
        }
    }
    
    public String getName(){
        return this.name;
    }
}
