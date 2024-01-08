package ast;
import java.util.ArrayList;
import environment.*;
import emitter.Emitter;
/**
 * Makes a block of statements
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Block implements Statement
{
    // instance variables - replace the example below with your own
    private ArrayList<Statement> exp;

    /**
     * Makes a block of statements
     * 
     * @param exp ArrayList of statements
     */
    public Block(ArrayList<Statement> exp)
    {
        // initialise instance variables
        this.exp = exp;
    }

    /**
     * Executes all of the block
     *
     * @param env the enviornment that will be the source of variables
     */
    public void exec(Environment env)
    {
        // put your code here
        for(int i=0; i<exp.size(); i++)
            exp.get(i).exec(env);
    }

    public void compile(Emitter e)
    {
        for (Statement expression : exp)
            expression.compile(e);
    }
}
