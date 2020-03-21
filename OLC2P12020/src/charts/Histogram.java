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
public class Histogram implements ASTNode{
    private LinkedList<ASTNode> largs;
    private int row;
    private int column;

    public Histogram(LinkedList<ASTNode> largs, int row, int column) {
        super();
        this.largs = largs;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object arg1 = largs.get(0).execute(environment, LError);
        Object arg2 = largs.get(1).execute(environment, LError);
        Object arg3 = largs.get(2).execute(environment, LError);
        //Verifico que los 3 elementos sean vectores
        if(arg1 instanceof Vec && arg2 instanceof Vec && arg3 instanceof Vec){
            //Verifico que los argumentos 2,3 sean de tipo string
            Object xlab[] = ((Vec)arg3).getValues();
            Object main[] = ((Vec)arg2).getValues();
            if(xlab[0] instanceof String && main[0] instanceof String){
                //Verifico que mis datos sean de tipo numerico
                Object values[] = ((Vec)arg1).getValues();
                if(values[0] instanceof Float || values[0] instanceof Integer){
                    boolean flag = true;
                    for(int i=0; i<values.length; i++){
                        if(Float.parseFloat((values[i]).toString()) < 0){
                            flag = false;
                            break;
                        }
                    }
                    
                    if(flag){
                        HistogramChart hc = new HistogramChart(values, xlab[0].toString(), main[0].toString());
                        hc.start();
                        return null;
                    }else{
                        TError error = new TError("histogram", "Semántico", "Existen valores negativos en el vector", row, column);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError("histogram", "Semántico", "Los valores no son de tipo numerico o Integer", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("histogram", "Semántico", "Los argumentos de etiquetas no son de tipo String", row, column);
                LError.add(error);

                return error;
            }
        }
        TError error = new TError("histogram", "Semántico", "Algún argumento no es de tipo vector", row, column);
        LError.add(error);

        return error;
    }
}
