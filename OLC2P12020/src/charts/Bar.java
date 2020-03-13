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
public class Bar implements ASTNode{
    private LinkedList<ASTNode> largs;

    public Bar(LinkedList<ASTNode> largs) {
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
        
        //Primero verifico que todos los argumentos sean vectores
        if(arg1 instanceof Vec && arg2 instanceof Vec && arg3 instanceof Vec && arg4 instanceof Vec && arg5 instanceof Vec){
            //Verifico que los argumentos 2,3,4 sean de tipo String
            Object xlab[] = ((Vec)arg2).getValues();
            Object ylab[] = ((Vec)arg3).getValues();
            Object main[] = ((Vec)arg4).getValues();
            if(xlab[0] instanceof String && ylab[0] instanceof String && main[0] instanceof String){
                //Verifico que el vector de etiquetas sea de tipo String
                Object names[] = ((Vec)arg5).getValues();
                if(names[0] instanceof String){
                    //Verifico que los datos sean de tipo numérico o de tipo String
                    Object values[] = ((Vec)arg1).getValues();
                    if(values[0] instanceof Integer || values[0] instanceof Float){
                        boolean flag = true;
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat((values[i]).toString()) < 0){
                                flag = false;
                                break;
                            }
                        }
                        
                        if(flag){
                            //Verifico si son del mismo tamaño los argumentos de valores y etiquetas
                            if(values.length<=names.length){
                                BarCharts bar = new BarCharts(values, xlab[0].toString(), ylab[0].toString(), main[0].toString(), names);
                                bar.start();
                                return null;
                            }else{
                                Object label[] = new Object[values.length];
                                for(int i=0; i<names.length; i++){
                                    label[i] = names[i];
                                }
                                for(int i=names.length; i<label.length; i++){
                                    label[i] = "Desconocido";
                                }
                                BarCharts bar = new BarCharts(values, xlab[0].toString(), ylab[0].toString(), main[0].toString(), label);
                                bar.start();
                                TError error = new TError("barplot", "Semántico", "El vector de labels es menor a los datos", 0, 0);
                                LError.add(error);

                                return error;
                            }
                        }else{
                            TError error = new TError("barplot", "Semántico", "El vector de valores no tiene números negativos", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError("barplot", "Semántico", "El vector de valores no es de tipo numérico", 0, 0);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError("barplot", "Semántico", "El último argumento debe ser de tipo String", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            else{
                TError error = new TError("barplot", "Semántico", "Los argumentos para las etiquetas deben ser de tipo String", 0, 0);
                LError.add(error);

                return error;
            }
        }
        TError error = new TError("barplot", "Semántico", "Algún argumento no es de tipo vector", 0, 0);
        LError.add(error);

        return error;
    }
}
