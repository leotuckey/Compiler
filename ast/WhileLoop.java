package ast;
import environment.*;
import emitter.Emitter;

/**
 * Makes a while loop object that can execute a while loop
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class WhileLoop implements Statement
{
    // instance variables - replace the example below with your own
    private Expression condition;
    private Block block;

    /**
     * Makes a WhileLoop
     * 
     * @param condition the condition for the while loop
     * @param block the block in the while loop
     */
    public WhileLoop(Expression condition, Block block)
    {
        // initialise instance variables
        this.condition = condition;
        this.block = block;
    }

    /**
     * Executes the block while the condition remains true
     *
     * @param   env the environment that will be the source of variables
     */
    public void exec(Environment env)
    {
        // put your code here
        while ((boolean) condition.eval(env))
        {
            block.exec(env);
        }
    }

    public void compile(Emitter e)
    {
        String labelOne = e.nextLabelID();
        String labelTwo = e.nextLabelID();
        e.emit(labelTwo + ":");
        condition.compile(e);
        e.emit("beq $t0, $0, " + labelOne);
        block.compile(e);
        e.emit("j " + labelTwo);
        e.emit(labelOne + ":");
    }
}
