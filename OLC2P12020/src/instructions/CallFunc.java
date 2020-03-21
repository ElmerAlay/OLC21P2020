package instructions;

import NativeFunctions.*;
import abstracto.*;
import charts.Bar;
import charts.Dispersion;
import charts.Histogram;
import charts.Line;
import charts.Pie;
import expressions.Default;
import java.util.LinkedList;
import stadisticFunctions.Mean;
import stadisticFunctions.Median;
import stadisticFunctions.Mode;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Symbol;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class CallFunc implements ASTNode{
    private String name;
    private LinkedList<ASTNode> lparam;
    private int row;
    private int column;

    public CallFunc(String name, LinkedList<ASTNode> lparam, int row, int column) {
        super();
        this.name = name;
        this.lparam = lparam;
        this.row = row;
        this.column = column;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verifico que sea alguna función nativa
        if(name.equals("print")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new Print(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("stringlength")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new StringLength(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("remove")){
            //Verifico que tenga dos parámetro
            if(lparam.size()==2)
                return new Remove(lparam.get(0), lparam.get(1), row, column).execute(environment, LError);
        }else if(name.equals("tolowercase")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new ToLowerCase(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("touppercase")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new ToUpperCase(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("trunk")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new Trunk(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("round")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new Round(lparam.get(0), row, column).execute(environment, LError);
        }else if(name.equals("mean")){
            return new Mean(lparam, row, column).execute(environment, LError);
        }else if(name.equals("median")){
            return new Median(lparam, row, column).execute(environment, LError);
        }else if(name.equals("mode")){
            return new Mode(lparam, row, column).execute(environment, LError);
        }else if(name.equals("list")){
            return new ListFunc(lparam, row, column).execute(environment, LError);
        }else if(name.equals("c")){
            return new C(lparam, row, column).execute(environment, LError);
        }else if(name.equals("matrix") && lparam.size()==3){
            return new Matrix(lparam, row, column).execute(environment, LError);
        }else if(name.equals("array") && lparam.size()==2){
            return new ArrayFunc(lparam, row, column).execute(environment, LError);
        }else if(name.equals("typeof") && lparam.size()==1){
            return new TypeOf(lparam.remove(0), row, column).execute(environment, LError);
        }else if(name.equals("length") && lparam.size()==1){
            return new Length(lparam.remove(0), row, column).execute(environment, LError);
        }else if(name.equals("ncol") && lparam.size()==1){
            return new NCol(lparam.remove(0), row, column).execute(environment, LError);
        }else if(name.equals("nrow") && lparam.size()==1){
            return new NRow(lparam.remove(0), row, column).execute(environment, LError);
        }else if(name.equals("pie") && lparam.size()==3){
            return new Pie(lparam, row, column).execute(environment, LError);
        }else if(name.equals("barplot") && lparam.size()==5){
            return new Bar(lparam, row, column).execute(environment, LError);
        }else if(name.equals("plot") && lparam.get(4).execute(environment, LError) instanceof Vec &&
                (((Vec)lparam.get(4).execute(environment, LError)).getValues()[0] instanceof Integer ||((Vec)lparam.get(4).execute(environment, LError)).getValues()[0] instanceof Float) 
                && lparam.size()==5){
            return new Dispersion(lparam, row, column).execute(environment, LError);
        }else if(name.equals("plot") && lparam.size()==5){
            return new Line(lparam, row, column).execute(environment, LError);
        }else if(name.equals("hist") && lparam.size()==3){
            return new Histogram(lparam, row, column).execute(environment, LError);
        }else {
            //Lo buscamos en nuestra tabla de símbolos
            if(environment.get(name+"_func")!=null){
                //Lo almaceno en un símbolo
                Symbol sim = environment.get(name+"_func");
                //Verifico que sea una función
                if(sim.getValue() instanceof Function){
                    //Verifico que tengan el mismo número de argumentos
                    Function func = (Function)sim.getValue();
                    if(func.getLparam().size()==lparam.size()){
                        Environment local = new Environment(environment, "local_"+name+"_func");
                        for(int i=0; i<lparam.size(); i++){
                            //Guardo los parámetros de la función en la tabla de símbolos
                            func.getLparam().get(i).execute(local, LError);
                        }
                        for(int i=0; i<lparam.size(); i++){
                            //Verifico que sean correctos los argumentos de la llamada de la función
                            if(func.getNameParam(i) != null){
                                //El argumento es la palabra default
                                if(lparam.get(i) instanceof Default){
                                    //Verifico si la variable tiene valor
                                    if(local.get(func.getNameParam(i)).getValue()==null){
                                        TError error = new TError(name, "Semántico", "El parámetro no tiene valor por defecto", row, column);
                                        LError.add(error);

                                        return error;
                                    }
                                }else
                                    new VarAssig(func.getNameParam(i), lparam.get(i), row, column).execute(local, LError);
                            }else{
                                TError error = new TError(name, "Semántico", "Error en el argumento "+(i+1), row, column);
                                LError.add(error);

                                return error;
                            }
                        }
                        
                        //Ejecuto su lista de instrucciones
                        for(ASTNode inst: func.getLinst()){
                            if(inst instanceof Return){
                                Object op = ((Return)inst).getExp().execute(local, LError);
                                if(op instanceof Vec)
                                    return (Vec)op;
                                else if(op instanceof ListStruct)
                                    return (ListStruct)op;
                                else if(op instanceof Mat)
                                    return (Mat)op;
                                else if(op instanceof Arr)
                                    return (Arr)op;

                                TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                                LError.add(error);

                                return error;
                            }
                            
                            inst.execute(local, LError);
                        }
                        return null;
                    }else{
                        TError error = new TError(name, "Semántico", "El número de argumentos es distinto", row, column);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "El id no representa una función", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError(name, "Semántico", "La función no existe", row, column);
                LError.add(error);

                return error; 
            }
        }
        
        TError error = new TError(name, "Semántico", "Error de argumentos en la función", row, column);
        LError.add(error);

        return error;
    }
    
}
