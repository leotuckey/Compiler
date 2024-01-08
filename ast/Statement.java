package ast;
import environment.*;
import emitter.Emitter;

/**
 * Statement class is a statement in pascal
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public interface Statement
{
    /**
     * Executes the statement
     *
     * @param env the environment that will be the source of variables
     */
    public abstract void exec(Environment env);
    
    @Override
    String toString();

    public void compile(Emitter e);
}
