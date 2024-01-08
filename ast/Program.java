package ast;
import emitter.Emitter;
import environment.*;

/**
 * Part of ast class that has a list of declarations and statements.
 *
 * @author Leo Tuckey
 * @version April 25, 2022
 */
public class Program implements Statement
{
    // instance variables - replace the example below with your own
    private final Statement toRun;
    private final Environment globalEnv;

    /**
     * Constructor for program
     *
     * @param s the statement to run
     * @param e the environment used
     */
    public Program(Statement s, Environment e)
    {
        toRun = s;
        globalEnv = e;
    }

    /**
     * Executes program
     *
     * @param e the environment used
     */
    @Override
    public void exec(Environment e)
    {
        e.setGlobalEnvironment(globalEnv);
        toRun.exec(e.global());
    }

    /**
     * Finds program in string form
     *
     * @return the string form of the program
     */
    @Override
    public String toString()
    {
        return toRun.toString();
    }

    public void compile(Emitter e)
    {
        String fileName = e.getFileName();
        if (fileName.contains(".asm"))
            fileName = fileName.substring(0, e.getFileName().length() - 4);
        e.emit("# Program Created from AST from " + fileName);
        e.emit(".data");
        for (String varName : globalEnv.getAllVariables().keySet())
        {
            e.emit("var" + varName + ":");
            e.emit(".word 0");
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        toRun.compile(e);
    }
}
