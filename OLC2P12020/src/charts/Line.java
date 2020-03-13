
package charts;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Line implements ASTNode{
    private LinkedList<ASTNode> largs;

    public Line(LinkedList<ASTNode> largs) {
        super();
        this.largs = largs;
    }
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object arg1 = largs.get(0).execute(environment, LError);
        Object arg2 = largs.get(1).execute(environment, LError);
        Object arg3 = largs.get(2).execute(environment, LError);
        Object arg4 = largs.get(3).execute(environment, LError);
        Object arg5 = largs.get(4).execute(environment, LError);
        //Verifico que los argumentos sean vectores
        if(arg2 instanceof Vec && arg3 instanceof Vec && arg4 instanceof Vec && arg5 instanceof Vec){
            //Verifico que los argumentos 3,4,5 sean de tipo String
            Object xlab[] = ((Vec)arg3).getValues();
            Object ylab[] = ((Vec)arg4).getValues();
            Object main[] = ((Vec)arg5).getValues();
            if(xlab[0] instanceof String && ylab[0] instanceof String && main[0] instanceof String){
                //Verifico que si los valores son vectores
                if(arg1 instanceof Vec){
                    //Verifico que sea de tipo float o integer
                    Object values[] = ((Vec)arg1).getValues();
                    if(values[0] instanceof Float || values[0] instanceof Integer){
                        //Verifico que los valores sean positivos
                        boolean flag = true;
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat((values[i]).toString()) < 0){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            //Verifico que el segundo argumento sea de tipo String
                            Object types[] = ((Vec)arg2).getValues();
                            String type = "";
                            if(types[0] instanceof String){
                                if(types[0].toString().toLowerCase().equals("p"))
                                    type="p";
                                else if(types[0].toString().toLowerCase().equals("i"))
                                    type="i";
                                else if(types[0].toString().toLowerCase().equals("o"))
                                    type = "o";
                                else{
                                    LineCharts line = new LineCharts(values, "o", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    TError error = new TError("plot", "Semántico", "El argumento type es incorrecto", 0, 0);
                                    LError.add(error);

                                    return error; 
                                }
                                LineCharts line = new LineCharts(values, type, xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                line.start();
                                return null;
                            }else{
                                LineCharts line = new LineCharts(values, "o", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                line.start();
                                TError error = new TError("plot", "Semántico", "El argumento type es incorrecto", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }else{
                            TError error = new TError("plot", "Semántico", "Algunos valores son negativos", 0, 0);
                            LError.add(error);

                            return error; 
                        }
                    }else{
                        TError error = new TError("plot", "Semántico", "Los valores no son de tipo numerico o integer", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }
                //Verifico si los valores es una matriz
                else if(arg1 instanceof Mat){
                    Object vals[][] = ((Mat)arg1).getValues();
                    Object values[] = new Object[((Mat)arg1).col * ((Mat)arg1).row];
                    int cont = 0;
                    for(int i=0; i<((Mat)arg1).col; i++){
                        for(int j=0; j<((Mat)arg1).row; j++){
                            values[cont] = vals[j][i];
                            cont++;
                        }
                    }
                    
                    //Verifico que sea de tipo float o integer
                    if(values[0] instanceof Float || values[0] instanceof Integer){
                        //Verifico que los valores sean positivos
                        boolean flag = true;
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat((values[i]).toString()) < 0){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            //Verifico que el segundo argumento sea de tipo String
                            Object types[] = ((Vec)arg2).getValues();
                            String type = "";
                            if(types[0] instanceof String){
                                if(types[0].toString().toLowerCase().equals("p"))
                                    type="p";
                                else if(types[0].toString().toLowerCase().equals("i"))
                                    type="i";
                                else if(types[0].toString().toLowerCase().equals("o"))
                                    type = "o";
                                else{
                                    LineCharts line = new LineCharts(values, "o", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    TError error = new TError("plot", "Semántico", "El argumento type es incorrecto", 0, 0);
                                    LError.add(error);

                                    return error; 
                                }
                                LineCharts line = new LineCharts(values, type, xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                line.start();
                                return null;
                            }else{
                                LineCharts line = new LineCharts(values, "o", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                line.start();
                                TError error = new TError("plot", "Semántico", "El argumento type es incorrecto", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }else{
                            TError error = new TError("plot", "Semántico", "Algunos valores son negativos", 0, 0);
                            LError.add(error);

                            return error; 
                        }
                    }else{
                        TError error = new TError("plot", "Semántico", "Los valores no son de tipo numerico o integer", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }else{
                    TError error = new TError("plot", "Semántico", "Los valores no son vector", 0, 0);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("plot", "Semántico", "Los últimos tres argumentos deben ser de tipo string", 0, 0);
                LError.add(error);

                return error;
            }
        }
        TError error = new TError("plot", "Semántico", "Algún argumento no es de tipo vector", 0, 0);
        LError.add(error);

        return error;
    }
}
