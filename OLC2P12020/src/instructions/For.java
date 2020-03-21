package instructions;

import View.MainWindow;
import abstracto.ASTNode;
import abstracto.TError;
import analizadores.Simbolo;
import static com.oracle.nio.BufferSecrets.instance;
import expressions.Constant;
import java.util.LinkedList;
import symbols.Arr;
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
public class For implements ASTNode{
    private String id;
    private ASTNode exp;
    private LinkedList<ASTNode> linst;
    private int row;
    private int column;

    public For(String id, ASTNode exp, LinkedList<ASTNode> linst, int row, int column) {
        super();
        this.id = id;
        this.exp = exp;
        this.linst = linst;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificar de qué tipo es la expresión
        Environment local = new Environment(environment, "local_for");
        Object op = exp.execute(environment, LError);
        if(op instanceof Vec || op instanceof Mat || op instanceof ListStruct || op instanceof Arr){
            if(op instanceof Vec){
                for(int i=0;i<((Vec)op).getValues().length; i++){
                    Constant c = new Constant(((Vec)op).getValues()[i]);
                    new VarAssig(id, c, row, column).execute(local, LError);
                    for(ASTNode ins: linst){
                        if(ins instanceof Break)
                            return null;
                        else if(ins instanceof Continue)
                            break;
                        else if(ins instanceof Return){
                            Object op2 = ((Return)ins).getExp().execute(local, LError);
                            if(op2 instanceof Vec)
                                return (Vec)op2;
                            else if(op2 instanceof ListStruct)
                                return (ListStruct)op2;
                            else if(op2 instanceof Mat)
                                return (Mat)op2;
                            else if(op2 instanceof Arr)
                                return (Arr)op2;

                            TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                            LError.add(error);

                            return error;
                        }
                        else if(ins instanceof ReturnEmpty)
                            return null;
                        ins.execute(local, LError);
                    }
                }
            }else if(op instanceof ListStruct){
                for(int i=0;i<((ListStruct)op).getValues().size(); i++){
                    if(((ListStruct)op).getValues().get(i) instanceof ListStruct){
                        Type type = new Type(Type.Types.LISTA, "Lista");
                        LinkedList<Object> l = ((ListStruct)((ListStruct)op).getValues().get(i)).getValues();
                        if(local.get(id) == null){ //Significa que no encontró una variable con ese nombre registrado    
                            local.put(new Symbol(type, id, new ListStruct(l), row, column, local.getName())); //Entonces lo agregamos a la tabla de simbolos
                            MainWindow.general.put(new Symbol(type, id, new ListStruct(l), row, column, local.getName())); //Entonces lo agregamos a la tabla de simbolos
                        }else {
                            local.get(id).setValue(new ListStruct(l)); //De lo contrario actualizo su valor en la tabla
                            local.get(id).setType(type);
                        }
                    }
                    if(((ListStruct)op).getValues().get(i) instanceof Vec){
                        Object res[] = ((Vec)((ListStruct)op).getValues().get(i)).getValues();
                        Type type = new Type(null, "Vector");
                        if(res[0] instanceof String)
                            type.setTypes(Type.Types.STRING);
                        else if (res[0] instanceof Float)
                            type.setTypes(Type.Types.NUMERICO);
                        else if (res[0] instanceof Integer)
                            type.setTypes(Type.Types.INTEGER);
                        else if (res[0] instanceof Boolean)
                            type.setTypes(Type.Types.BOOLEANO);
                        
                        if(local.get(id) == null){ //Significa que no encontró una variable con ese nombre registrado
                            local.put(new Symbol(type, id, new Vec(res), row, column, local.getName())); //Entonces lo agregamos a la tabla de simbolos
                            MainWindow.general.put(new Symbol(type, id, new Vec(res), row, column, local.getName())); //Entonces lo agregamos a la tabla de simbolos
                        }else {
                            local.get(id).setValue(new Vec(res)); //De lo contrario actualizo su valor en la tabla
                            local.get(id).setType(type);
                        }
                    }
                    for(ASTNode ins: linst){
                        if(ins instanceof Break)
                            return null;
                        else if(ins instanceof Continue)
                            break;
                        else if(ins instanceof Return){
                            Object op2 = ((Return)ins).getExp().execute(local, LError);
                            if(op2 instanceof Vec)
                                return (Vec)op2;
                            else if(op2 instanceof ListStruct)
                                return (ListStruct)op2;
                            else if(op2 instanceof Mat)
                                return (Mat)op2;
                            else if(op2 instanceof Arr)
                                return (Arr)op2;

                            TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                            LError.add(error);

                            return error;
                        }
                        else if(ins instanceof ReturnEmpty)
                            return null;
                        ins.execute(local, LError);
                    }
                }
            }else if(op instanceof Mat){
                for(int i=0;i<((Mat)op).row; i++){
                    for(int j=0;j<((Mat)op).col; j++){
                        Constant c = new Constant(((Mat)op).getValues()[i][j]);
                        new VarAssig(id, c, row, column).execute(local, LError);
                        for(ASTNode ins: linst){
                        if(ins instanceof Break)
                            return null;
                        else if(ins instanceof Continue)
                            break;
                        else if(ins instanceof Return){
                            Object op2 = ((Return)ins).getExp().execute(local, LError);
                            if(op2 instanceof Vec)
                                return (Vec)op2;
                            else if(op2 instanceof ListStruct)
                                return (ListStruct)op2;
                            else if(op2 instanceof Mat)
                                return (Mat)op2;
                            else if(op2 instanceof Arr)
                                return (Arr)op2;

                            TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                            LError.add(error);

                            return error;
                        }
                        else if(ins instanceof ReturnEmpty)
                            return null;
                        ins.execute(local, LError);
                        }
                    }
                }
            }else if(op instanceof Arr){
                for(int i=0;i<((Arr)op).getData().size(); i++){
                    if(((Arr)op).getData().get(i) instanceof Vec){
                        Constant c = new Constant(((Vec)((Arr)op).getData().get(i)).getValues()[0]);
                        new VarAssig(id, c, row, column).execute(local, LError);
                    }else if(((Arr)op).getData().get(i) instanceof ListStruct){
                        Constant c = new Constant(((ListStruct)((Arr)op).getData().get(i)).getValues().get(0));
                        new VarAssig(id, c, row, column).execute(local, LError);
                    }
                    for(ASTNode ins: linst){
                        if(ins instanceof Break)
                            return null;
                        else if(ins instanceof Continue)
                            break;
                        else if(ins instanceof Return){
                            Object op2 = ((Return)ins).getExp().execute(local, LError);
                            if(op2 instanceof Vec)
                                return (Vec)op2;
                            else if(op2 instanceof ListStruct)
                                return (ListStruct)op2;
                            else if(op2 instanceof Mat)
                                return (Mat)op2;
                            else if(op2 instanceof Arr)
                                return (Arr)op2;

                            TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                            LError.add(error);

                            return error;
                        }
                        else if(ins instanceof ReturnEmpty)
                            return null;
                        ins.execute(local, LError);
                    }
                }
            }
            
            return null;
        }else{
            TError error = new TError("For", "Semántico", "La expresión debe ser de tipo de alguna estructura", row, column);
            LError.add(error);
            return error;
        }
    }
}
