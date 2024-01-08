package ast;
import environment.*;
import emitter.Emitter;

/**
 * Abstract class Expression that will be extended by other classes
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public interface Expression
{
    /**
     * Evaluates the Expression
     *
     * @param env the enviornment that will be the source of variables
     * 
     * @return the evaluated value
     */
    Object eval(Environment env);

    @Override
    String toString();

    public void compile(Emitter e);
}
