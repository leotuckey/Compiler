package parser;

import scanner.ScanErrorException;
import scanner.Scanner;
import emitter.Emitter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.*;
import java.util.Objects;

import ast.*;
import environment.*;

import java.util.ArrayList;

/**
 * Makes a parser with a scanner to do pascal operations
 *
 * @author Leo Tuckey
 * @version March 11, 2022
 */
public class Parser
{
    // instance variables - replace the example below with your own
    private Scanner scan;
    private String curr;

    /**
     * Constructor for objects of class Parser
     *
     * @param scan Scanner
     * @throws ScanErrorException
     */
    public Parser(Scanner scan) throws ScanErrorException
    {
        // initialise instance variables
        this.scan = scan;
        curr = scan.nextToken();
    }

    /**
     * Used to test
     *
     * @param args
     * @throws ScanErrorException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws ScanErrorException, FileNotFoundException,
            IOException
    {
        File test = new File("parserTest9.txt");
        FileInputStream file = new FileInputStream(test);
        Scanner testFile = new Scanner(file);
        Parser testPars = new Parser(testFile);
        ArrayList<Statement> a = new ArrayList<>();
        ArrayList<ProcedureDeclaration> procedures = new ArrayList<>();
        Environment currEnv = new Environment();
        try
        {
            Emitter emitterOut = new Emitter("output.asm");
            Program progParse = testPars.parseProgram();
            System.out.println("Program Tree: ");
            System.out.println(progParse);
            progParse.compile(emitterOut);
            //need to close the file completely
            emitterOut.emit("li $v0, 10");
            emitterOut.emit("syscall");
            emitterOut.close();
        }
        catch (ScanErrorException e)
        {
            System.out.println(e);
        }
    }

    public String getCurr()
    {
        return curr;
    }

    public void setCurr(String curr)
    {
        this.curr = curr;
    }

    /**
     * Eats a token
     *
     * @param exp the expected token
     * @return what was eaten
     * @throws ScanErrorException if there is an error while scanning
     */
    private String eat(String exp) throws ScanErrorException
    {
        // put your code here
        if (exp.equals(curr))
        {
            curr = scan.nextToken();
            return curr;
        }
        throw new IllegalArgumentException("Illegal character - expected \'"
                + exp + "\' and found \'" + curr + "\'");
    }

    //precondition: current token begins an IF statement
    //postcondition: all tokens in statement have been
    //      eaten; current token is first one
    //      after the IF statement

    /**
     * parses If
     *
     * @throws ScanErrorException if there is an error while scanning
     */
    private Statement parseIf() throws ScanErrorException
    {
        eat("IF");
        Expression cond = parseCondition();
        eat("THEN");
        Block block = (Block) parseStatement();
        return new If(cond, block);
    }

    /**
     * Your comments documenting this method according to the
     * style guide
     * precondition: current token is an integer
     * postcondition: number token has been eaten
     * parses a number
     *
     * @return the value of the parsed integer
     * @throws ScanErrorException if there is an error while scanning
     */
    private int parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(curr);
        eat(curr);
        return num;
    }

    /**
     * Parses a statement
     *
     * @throws ScanErrorException if there is an error while scanning
     */
    private Statement parseStatement() throws ScanErrorException
    {
        if (curr.equals("WRITELN"))
        {
            eat(curr);
            eat("(");
            Expression temp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(temp);
        }
        else if (curr.equals("BEGIN"))
        {
            ArrayList<Statement> statementList = new ArrayList<>();
            eat("BEGIN");
            while (!curr.equals("END"))
            {
                statementList.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(statementList);
        }
        else if (curr.equals("IF"))
        {
            return parseIf();
        }
        else if (curr.equals("RETURN"))
        {
            eat("RETURN");
            Expression returnedVar = parseExpression();
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        else if (curr.equals("WHILE"))
        {
            eat("WHILE");
            Expression cond = parseCondition();
            eat("DO");
            Block block = (Block) parseStatement();
            return new WhileLoop(cond, block);
        }
        else
        {
            String varName = curr;
            eat(curr);
            eat(":=");
            Statement returnedVar = new Assignment(varName, parseExpression());
            eat(curr);
            return returnedVar;
        }
    }

    /**
     * Parses a factor
     *
     * @return value
     * @throws ScanErrorException if there is an error while scanning
     */
    public Expression parseFactor() throws ScanErrorException
    {
        if (curr.equals("("))
        {
            eat(curr);
            Expression temp = parseExpression();
            eat(")");
            return temp;
        }
        if (curr.equals("-"))
        {
            eat(curr);
            return new BinOp(parseFactor(), "*", new ast.Number(-1));
        }
        if (isInteger(curr))
        {
            int integerReturn = Integer.parseInt(curr);
            eat(curr);
            return new ast.Number(integerReturn);
        }
        String name = curr;
        eat(curr);
        if (curr.equals("("))
        {
            eat("(");
            ArrayList<Expression> expressions = new ArrayList<>();
            for (; ; )
            {
                if (curr.equals(")"))
                {
                    eat(")");
                    break;
                }
                else if (curr.equals(","))
                {
                    eat(",");
                }
                else
                {
                    expressions.add(parseExpression());
                    if (!curr.equals(")"))
                        eat(curr);
                }
            }
            Expression[] expArr = new Expression[expressions.size()];
            for (int i = 0; i < expArr.length; i++)
            {
                expArr[i] = expressions.get(i);
            }
            return new ProcedureCall(name, expArr);
        }
        else
            return new Variable(name);
    }

    /**
     * Parses an expression
     *
     * @throws ScanErrorException if there is an error while scanning
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression res = parseTerm();
        while (curr.equals("+") || curr.equals("-"))
        {
            if (curr.equals("+"))
            {
                eat("+");
                res = new BinOp(res, "+", parseTerm());
            }
            else if (curr.equals("-"))
            {
                eat("-");
                res = new BinOp(res, "-", parseTerm());
            }
        }
        return res;
    }

    /**
     * Parses a term
     *
     * @throws ScanErrorException if there is an error while scanning
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression res = parseFactor();
        while (curr.equals("*") || curr.equals("/"))
        {
            if (curr.equals("*"))
            {
                eat("*");
                res = new BinOp(res, "*", parseFactor());
            }
            else if (curr.equals("/"))
            {
                eat("/");
                res = new BinOp(res, "/", parseFactor());
            }
        }
        return res;
    }
    public Program parseProgram() throws ScanErrorException
    {
        ArrayList<ProcedureDeclaration> procedures = new ArrayList<>();
        ArrayList<String> variables = new ArrayList<>();
        ArrayList<Statement> statements = new ArrayList<>();
        while (scan.hasNext())
            if (curr.equals("PROCEDURE"))
                procedures.add(parseDeclaration());
            else if (curr.equals("VAR"))
            {
                eat("VAR");
                while (!curr.equals(";"))
                {
                    if (curr.equals(","))
                        eat(",");
                    else
                    {
                        variables.add(curr);
                        eat(curr);
                    }
                }
                eat(";");
            }
            else
                statements.add(parseStatement());
        System.out.println("all variables are " + variables);
        return new Program(new Block(statements), new Environment(procedures, variables));
    }

    /**
     * Parses a condition
     *
     * @throws ScanErrorException if there is an error while scanning
     */
    private Expression parseCondition() throws ScanErrorException
    {
        Expression res = (parseExpression());
        while ((curr.equals(">") || curr.equals("<") || curr.equals("<=") || curr.equals(">=")
                || curr.equals("<>")))
        {
            if (curr.equals(">"))
            {
                eat(">");
                res = new BinOp(res, ">", parseExpression());
            }
            else if (curr.equals("<"))
            {
                eat("<");
                res = new BinOp(res, "<", parseExpression());
            }
            else if (curr.equals("<="))
            {
                eat("<=");
                res = new BinOp(res, "<=", parseExpression());
            }
            else if (curr.equals(">="))
            {
                eat(">=");
                res = new BinOp(res, ">=", parseExpression());
            }
            else
            {
                eat("<>");
                res = new BinOp(res, "<>", parseExpression());
            }
        }
        return res;
    }

    /**
     * Parses a declaration
     * @return value
     * @throws ScanErrorException
     */
    ProcedureDeclaration parseDeclaration() throws ScanErrorException
    {
        eat("PROCEDURE");
        String name = curr;
        eat(curr);
        eat("(");
        ArrayList<Variable> params = new ArrayList<>();
        while (true)
            if (curr.equals(")"))
            {
                eat(")");
                break;
            }
            else if (curr.equals(","))
                eat(",");
            else
            {
                params.add(new Variable(curr));
                eat(curr);
            }
        eat(";");
        Variable[] paramsArr = new Variable[params.size()];
        for (int i = 0; i < paramsArr.length; i++)
        {
            paramsArr[i] = params.get(i);
        }
        return new ProcedureDeclaration(name, parseStatement(),
                paramsArr);
    }

    /**
     * Checks if a token is an integer
     *
     * @param token the string that will be checked if it's an integer
     * @return true if it is an integer
     * otherwise, false
     */
    private static boolean isInteger(String token)
    {
        try
        {
            Integer.parseInt(token);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
}
