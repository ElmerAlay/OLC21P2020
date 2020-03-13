package stadisticFunctions;

/**
 *
 * @author junio
 */
public class SupportFunctions {
    // 0 - Menor a Mayor, 1 - Mayor a menor
    public static Object[] burbuja ( Object [] v, int ord ) {
        int i, j, n = v.length; 
        float aux;// = 0;
        
        for ( i = 0; i < n - 1; i++ ){
           v[i] = Float.parseFloat(v[i].toString()); 
        }
        
        for ( i = 0; i < n - 1; i++ ){
            for ( j = i + 1; j < n; j++ ){
                if ( ord == 0 ){
                    if ( Float.parseFloat(v[i].toString()) > Float.parseFloat(v[j].toString()) ) {
                        aux = Float.parseFloat(v[j].toString());
                        v[j] = Float.parseFloat(v[i].toString());
                        v[i] = aux;
                    }
                }else if ( ord == 1 ){
                    if ( Float.parseFloat(v[i].toString()) < Float.parseFloat(v[j].toString()) ) {
                        aux = Float.parseFloat(v[i].toString());
                        v[i] = Float.parseFloat(v[j].toString());
                        v[j] = aux;
                    }
                }
            }
        }

        return v;
    }
    
    public static boolean esMultiplo(int n1,int n2){;
	return n1%n2==0;
    }
    
    public static int mapLexiMat(int fila, int columna, int tam){
        return columna*tam+fila;
    }
    
    public static int mapLexArr(int indexes[], int tam[], int dim){
        if(dim==2)
            return indexes[0]*tam[1] + indexes[1];
        
        return mapLexArr(indexes, tam, dim-1)*tam[dim-1]+indexes[dim-1];
    }
    
    public static String type(Object vec){
        String result = "";
        
        if(vec instanceof String)
            result = "STRING";
        else if (vec instanceof Float)
            result = "NUMERICO";
        else if (vec instanceof Integer)
            result = "INTEGER";
        else if (vec instanceof Boolean)
            result = "BOOLEAN";
        return result;
    }
    
    public static double getMax(Object vec[], int op){
        double max, min;
        min=max=Double.parseDouble(vec[0].toString());
 
	for(int i = 0; i < vec.length; i++){
            if(min>Double.parseDouble(vec[i].toString()))
		min=Double.parseDouble(vec[i].toString());
            
            if(max<Double.parseDouble(vec[i].toString()))
		max=Double.parseDouble(vec[i].toString());
	}
        
        if(op==1)
            return max;
        else
            return min;
    }
}
