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
}
