package ast;
import environment.*;
import emitter.Emitter;

/**
 * If the condition is true, executes a statement
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class If implements Statement
{
    // instance variables - replace the example below with your own
    private Expression condition;
    private Block b;

    /**
     * Makes an if object with the given condition and block
     * 
     * @param condition the given condition
     * @param b the given block
     */
    public If(Expression condition, Block b)
    {
        // initialise instance variables
        this.condition = condition;
        this.b=b;
    }

    /**
     * If the condition is true, executes a statement
     *
     * @param env the environment that will the be the source of variables
     */
    public void exec(Environment env)
    {
        // put your code here
        if ((boolean) condition.eval(env))
        {
            b.exec(env);
        }
    }

    public void compile(Emitter e)
    {
        condition.compile(e);
        String labelOne = e.nextLabelID();
        e.emit("beq $t0, $0, " + labelOne);
        b.compile(e);
        e.emit(labelOne + ":");
    }
}
