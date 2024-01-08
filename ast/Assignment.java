package ast;
import environment.*;
import emitter.Emitter;

/**
 * Puts a variable in an environment
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Assignment implements Statement
{
    // instance variables - replace the example below with your own
    private String varName;
    private Expression value;

    /**
     * Creates an assignment object that will add to the environment
     *
     * @param varName name of the variable
     * @param value the value of the variable
     */
    public Assignment(String varName, Expression value)
    {
        this.varName = varName;
        this.value = value;
    }

    /**
     * Gets the name of the variable
     *
     * @return the name of the variable
     */
    public String getVarName()
    {
        return varName;
    }

    /**
     * Sets the variable name
     *
     * @param varName the name of the variable
     */
    public void setVarName(String varName)
    {
        this.varName = varName;
    }

    /**
     * Gets the value of the variable
     *
     * @return the value of the variable
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Sets the value of the variable
     *
     * @param value the value of the variable
     */
    public void setValue(Expression value)
    {
        this.value = value;
    }

    /**
     * Adds to the environment
     *
     * @param env the enviornment that will be the source of variables
     */
    public void exec(Environment env)
    {
        env.setVariable(varName, value.eval(env));
    }

    public void compile(Emitter e)
    {
        value.compile(e);
        e.emit("sw $t0 var" + varName);
    }
}
