package instructions;

import NativeFunctions.*;
import abstracto.*;
import charts.Bar;
import charts.Dispersion;
import charts.Histogram;
import charts.Line;
import charts.Pie;
import java.util.LinkedList;
import stadisticFunctions.Mean;
import stadisticFunctions.Median;
import stadisticFunctions.Mode;
import symbols.Environment;
import symbols.Vec;

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
            if(lparam.size()==1)
                return new Print(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("stringlength")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new StringLength(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("remove")){
            //Verifico que tenga dos parámetro
            if(lparam.size()==2)
                return new Remove(lparam.get(0), lparam.get(1)).execute(environment, LError);
        }else if(name.equals("tolowercase")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new ToLowerCase(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("touppercase")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new ToUpperCase(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("trunk")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new Trunk(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("round")){
            //Verifico que sólo tenga un parámetro
            if(lparam.size()==1)
                return new Round(lparam.get(0)).execute(environment, LError);
        }else if(name.equals("mean")){
            return new Mean(lparam).execute(environment, LError);
        }else if(name.equals("median")){
            return new Median(lparam).execute(environment, LError);
        }else if(name.equals("mode")){
            return new Mode(lparam).execute(environment, LError);
        }else if(name.equals("list")){
            return new ListFunc(lparam).execute(environment, LError);
        }else if(name.equals("c")){
            return new C(lparam).execute(environment, LError);
        }else if(name.equals("matrix") && lparam.size()==3){
            return new Matrix(lparam).execute(environment, LError);
        }else if(name.equals("array") && lparam.size()==2){
            return new ArrayFunc(lparam).execute(environment, LError);
        }else if(name.equals("typeof") && lparam.size()==1){
            return new TypeOf(lparam.remove(0)).execute(environment, LError);
        }else if(name.equals("length") && lparam.size()==1){
            return new Length(lparam.remove(0)).execute(environment, LError);
        }else if(name.equals("ncol") && lparam.size()==1){
            return new NCol(lparam.remove(0)).execute(environment, LError);
        }else if(name.equals("nrow") && lparam.size()==1){
            return new NRow(lparam.remove(0)).execute(environment, LError);
        }else if(name.equals("pie") && lparam.size()==3){
            return new Pie(lparam).execute(environment, LError);
        }else if(name.equals("barplot") && lparam.size()==5){
            return new Bar(lparam).execute(environment, LError);
        }else if(name.equals("plot") && lparam.get(4).execute(environment, LError) instanceof Vec &&
                (((Vec)lparam.get(4).execute(environment, LError)).getValues()[0] instanceof Integer ||((Vec)lparam.get(4).execute(environment, LError)).getValues()[0] instanceof Float) 
                && lparam.size()==5){
            return new Dispersion(lparam).execute(environment, LError);
        }else if(name.equals("plot") && lparam.size()==5){
            return new Line(lparam).execute(environment, LError);
        }else if(name.equals("hist") && lparam.size()==3){
            return new Histogram(lparam).execute(environment, LError);
        }
        
        TError error = new TError(name, "Semántico", "Error de argumentos en la función", 0, 0);
        LError.add(error);

        return error;
    }
    
}
