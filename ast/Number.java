package ast;
import environment.*;
import emitter.Emitter;

/**
 * Converts an integer into a treenode
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Number implements Expression
{
    // instance variables - replace the example below with your own
    private int num;

    /**
     * Creates a number object that represents the given integer
     * 
     * @param num the given integer
     */
    public Number(int num)
    {
        // initialise instance variables
        this.num=num;
    }

    /**
     * Evaluates the integer
     *
     * @param env the enviornment that will be the source of variables
     * @return the number object
     */
    public Object eval(Environment env)
    {
        return num;
    }

    public void compile(Emitter e)
    {
        e.emit("li $t0, " + num);
    }
}
