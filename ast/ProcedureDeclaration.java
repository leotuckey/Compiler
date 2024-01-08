package ast;

import java.util.ArrayList;

import environment.*;

import java.util.Arrays;

/**
 * ProcedureDeclaration is a procedure declaration in a pascal program
 *
 * @author Leo Tuckey
 * @version April 25, 2022
 */
public class ProcedureDeclaration
{
    // instance variables - replace the example below with your own
    private Statement statement;
    private String name;
    private Variable[] params;

    /**
     * Declares a procedure
     *
     * @param statement statements that will be run
     * @param name      the method's name
     * @param params    parameters needed for the method to work and run
     */
    public ProcedureDeclaration(Statement statement, String name,
                                Variable[] params)
    {
        this.statement = statement;
        this.name = name;
        this.params = params;
    }

    /**
     * Creates a ProcedureDeclaration object
     *
     * @param name      the method's name
     * @param statement statements that will be run
     * @param params    parameters needed for the method to work and run
     */
    public ProcedureDeclaration(String name, Statement statement,
                                Variable[] params)
    {
        this(statement, name, params);
    }

    /**
     * Gets statement
     *
     * @return the statement that will run when procedure called
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Sets a new statement
     *
     * @param statement new statement
     */
    public void setStatement(Statement statement)
    {
        this.statement = statement;
    }

    /**
     * Sets new name of procedure
     *
     * @param name new name of procedure
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the name of the procedure
     *
     * @return name of procedure
     */
    public String getName()
    {
        return name;
    }

    /**
     * Finds program in string form
     *
     * @return the string form of the program
     */
    @Override
    public String toString()
    {
        return "Method: (" + name + "," + statement + ")";
    }

    /**
     * Sets the parameters of the procedure
     *
     * @param parameters new parameters
     */
    public void setParams(Variable[] parameters)
    {
        this.params = params;
    }

    /**
     * Gets the parameter with the given name
     *
     * @return the parameters
     */
    public Variable[] getParams()
    {
        return params;
    }

    /**
     * Returns parameters as a string
     *
     * @param test the tested parameter
     * @return true if the parameter is in list
     * otherwise, false
     */
    public boolean hasParam(String test)
    {
        return Arrays.stream(params).anyMatch(param -> param.getName().equals(test));
    }

    /**
     * Executes the procedure
     *
     * @param currEnv the current environment
     */
    public void exec(Environment currEnv)
    {
        currEnv.addProcedure(this);
    }
}
