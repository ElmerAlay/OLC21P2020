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
        this.exp = exp;
        this.lcase = lcase;
    }

    public SWITCH(ASTNode exp, LinkedList<CASE> lcase, LinkedList<ASTNode> linstf) {
        this.exp = exp;
        this.lcase = lcase;
        this.linstf = linstf;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        
        
        for(CASE in: lcase){
            Object res = in.execute(environment, LError);
            if(res instanceof Boolean && (Boolean.parseBoolean(res.toString())))
                return Boolean.parseBoolean(res.toString());
        }
        if(linstf!=null){
            for(ASTNode in: linstf){
                in.execute(environment, LError);
            } 
        }
        
        return false;
    }
}
