package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class SWITCH implements ASTNode{
    private ASTNode exp;
    private LinkedList<CASE> lcase;
    private LinkedList<ASTNode> linstf;

    public SWITCH(ASTNode exp, LinkedList<CASE> lcase) {
        super();
        this.exp = exp;
        this.lcase = lcase;
    }

    public SWITCH(ASTNode exp, LinkedList<CASE> lcase, LinkedList<ASTNode> linstf) {
        super();
        this.exp = exp;
        this.lcase = lcase;
        this.linstf = linstf;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        
        
        for(CASE in: lcase){
            Environment local = new Environment(environment, "local_switch_case");
            Object res = in.execute(local, LError);
            if(res instanceof Boolean && (Boolean.parseBoolean(res.toString())))
                return Boolean.parseBoolean(res.toString());
        }
        if(linstf!=null){
            Environment local = new Environment(environment, "local_switch_default");
            for(ASTNode in: linstf){
                in.execute(local, LError);
            } 
        }
        
        return null;
    }
}
