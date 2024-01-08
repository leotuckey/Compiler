package ast;
import environment.*;
import emitter.Emitter;

/**
 * Creates a writeln object that prints things
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Writeln implements Statement
{
    // instance variables - replace the example below with your own
    private Expression exp;

    /**
     * Constructor for objects of class Writeln
     * 
     * @param exp the expression that will be printed
     */
    public Writeln(Expression exp)
    {
        // initialise instance variables
        this.exp = exp;
    }

    /**
     * Prints the expression given
     *
     * @param  env  the environment that will be the source of variables
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0, $t0");
        e.emit("li $v0, 1");
        e.emit("syscall");
        e.emit("li $a0 '\\n'");
        e.emit("li $v0 11");
        e.emit("syscall");
    }
}
