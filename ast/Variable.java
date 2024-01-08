package ast;
import environment.*;
import emitter.Emitter;

/**
 * Makes a variable with a name
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Variable implements Expression
{
    // instance variables - replace the example below with your own
    private String name;

    /**
     * Makes a variable object with the given name
     * 
     * @param name the String that will be the name of the variable
     */
    public Variable(String name)
    {
        // initialise instance variables
        this.name=name;
    }

    /**
     * Evaluates the variable with the given value.
     *
     * @param env the enviornment that will be the source of variables
     * @return the evaluation
     */
    public Object eval(Environment env)
    {
        if (env.hasVariable(name))
            return env.getVariable(name);
        throw new IllegalArgumentException("The variable '" + name + "' was never declared.");
    }

    public String getName()
    {
        return name;
    }

    public void compile(Emitter e)
    {
        e.emit("lw $t0, var" + name);
    }
}
