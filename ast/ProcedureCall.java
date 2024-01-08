package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import emitter.Emitter;
import environment.*;

/**
 * ProcedureCall is a procedure call in AST
 *
 * @author Leo Tuckey
 * @version April 25,2022
 */
public class ProcedureCall implements Statement, Expression
{
    // instance variables - replace the example below with your own
    private final String procedureCalled;
    private final Expression[] args;

    /**
     * Creates a new procedure call object.
     *
     * @param procedureCalled name of procedure called
     * @param args            arguments of procedure call
     */
    public ProcedureCall(String procedureCalled, Expression[] args)
    {
        this.procedureCalled = procedureCalled;
        this.args = args;
    }

    /**
     * Gets procedure called
     *
     * @return procedure called
     */
    public String getProcedureCalled()
    {
        return procedureCalled;
    }

    /**
     * Evaluates the variable from the given environment.
     *
     * @param e the used environment
     * @return the variable's value
     * @precondition e is not null
     * @postcondition The code has been run and the variable evaluated
     */
    @Override
    public Object eval(Environment e)
    {
        exec(e);
        return e.getVariable(procedureCalled);
    }

    /**
     * Finds program in string form
     *
     * @return the string form of the program
     */
    @Override
    public String toString()
    {
        StringBuilder strbuilder = new StringBuilder();
        Arrays.stream(args).forEachOrdered(argument ->
                {
                    strbuilder.append(argument.toString());
                    strbuilder.append(", ");
                }
        );
        if (args.length > 0)
            strbuilder.delete(strbuilder.length() - 2, strbuilder.length());
        return "Procedure(" + procedureCalled + "(" + strbuilder + ")" + ")";
    }

    /**
     * Executes procedure call
     *
     * @param env the environment used
     * @precondition e is not null
     * @postcondition The code has been executed and the variable updated
     */
    @Override
    public void exec(Environment env)
    {
        Object[] argsArr = new Object[args.length];
        for (int i = 0; i < args.length; i++)
        {
            argsArr[i] = args[i].eval(env);
        }
        Environment newEnv;
        if (env.hasProcedure(procedureCalled))
        {
            newEnv = env.global().clone();
            ProcedureDeclaration pro = env.getSpecificProcedure(procedureCalled);
            for (int i = 0; i < pro.getParams().length; i++)
            {
                newEnv.getAllVariables().put(pro.getParams()[i].getName(), argsArr[i]);
            }
            pro.getStatement().exec(newEnv);
        }
        else
            throw new IllegalArgumentException("Procedure '" + procedureCalled + "' not found");
        for (Map.Entry<String, Object> entry : newEnv.getAllVariables().entrySet())
            if (env.hasVariable(entry.getKey()) &&
                    !env.getSpecificProcedure(procedureCalled).hasParam(entry.getKey()))
            {
                env.setVariable(entry.getKey(), entry.getValue());
            }
    }

    public void compile(Emitter e)
    {
    }
}
