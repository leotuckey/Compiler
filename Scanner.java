package scanner;

import java.io.*;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.FileInputStream;
//notebook answers: Compiler throws error if the character is a new line. The new 
//line will be skipped over
//If the character was a parenthesis, it would be the keyword if and the start
//of an if statement. If would be one token, and the parenthesis would be another.
/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Leo tuckey
 * @version January 13, 2022
 *  
 * Usage:
 * This object scans inputs to verify that the tokens are regular expressions
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar
     * Changes the value of the current character to the next one.
     */
    private void getNextChar()
    {
        try
        {
            currentChar=(char)in.read();
            if(currentChar==-1 || currentChar=='.')
            {
                eof=true;
                return;
            }
        }
        catch(IOException a)
        {
            a.printStackTrace();
            throw new RuntimeException(a);
        }
    }

    /**
     * Method: eat
     * Changes current character to next character if parameter is the current 
     * character.
     * 
     * @param expected the expected character that will be tested to see
     * if it is the same as the current character.
     * @throws ScanErrorException if there is an error while scanning
     */
    private void eat(char expected) throws ScanErrorException
    {
        if(expected==currentChar)
        {
            getNextChar();
            return;
        }
        else
            throw new ScanErrorException("Illegal character - expected"
                + expected + "and found " + currentChar);
    }

    /**
     * Determines if the inputted character is a digit
     * 
     * @param input the inputted character 
     * @return true if input is a digit; otherwise,
     * false
     */
    public static boolean isDigit(char input)
    {
        return input=='0' || input=='1' || input=='2' || input=='3' 
            || input=='4' || input=='5' || input=='6' || input=='7' || input=='8' || input=='9';
    }

    /**
     * Determines if the inputted character is a letter
     * 
     * @param input the inputted character 
     * @return true if input is a letter; otherwise,
     * false
     */
    public static boolean isLetter(char input)
    {
        return (Pattern.matches("[a-z]", String.valueOf(input)) || 
            (Pattern.matches("[A-Z]", String.valueOf(input))));
    }

    /**
     * Determines if the inputted character is a white space
     * 
     * @param input the inputted character 
     * @return true if input is a white space; otherwise,
     * false
     */
    public static boolean isWhiteSpace(char input)
    {
        return (Pattern.matches("[‘ ‘ ‘\\t’ ‘\\r’ ‘\\n’]", String.valueOf(input)));
    }

    /**
     * Determines if the inputted character is an operand
     * 
     * @param input the inputted character 
     * @return true if input is an operand; otherwise,
     * false
     */
    private boolean isOperand(char input)
    {
        return (Pattern.matches("['>' '<' ':' ‘=’ ‘+’ ‘*’ ‘/’ ‘%’ ‘(‘ ‘)’]", String.valueOf(input)) 
            || input=='-');
    }

    /**
     * Method: hasNext
     * Determines if there is a next token and not at end of file.
     * @return true if not at end of the file; otherwise,
     * false
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Scans a string and adds the characters to a string as long as all 
     * the characters are digits.
     * 
     * @return the lexeme
     * @throws ScanErrorException if there is an error while scanning
     * 
     */
    private String scanNumber() throws ScanErrorException
    {
        StringBuilder lexeme = new StringBuilder();
        while (isDigit(currentChar))
        {
            lexeme.append(currentChar);
            eat(currentChar);
        }
        return lexeme.toString();
    }

    /**
     * Scans a string and adds the characters to a string as long as all 
     * the characters are letters or digits.
     * 
     * @return the lexeme
     * @throws ScanErrorException if there is an error while scanning
     */
    private String scanIdentifier() throws ScanErrorException
    {
        StringBuilder lexeme = new StringBuilder();
        while (isLetter(currentChar) || isDigit(currentChar))
        {
            lexeme.append(currentChar);
            eat(currentChar);
        }
        return lexeme.toString();
    }

    /**
     * Scans a string and adds the characters to a string as long as all 
     * the characters are operands.
     * 
     * @return the lexeme
     * @throws ScanErrorException if there is an error while scanning
     */
    private String scanOperand() throws ScanErrorException
    {
        StringBuilder lexeme = new StringBuilder();
        if(currentChar==')')
        {
            eat(currentChar);
            return ")";
        }
        if(currentChar=='(')
        {
            eat(currentChar);
            return "(";
        }
        while(isOperand(currentChar))
        {
            lexeme.append(currentChar);
            eat(currentChar);
        }
        return lexeme.toString();
    }

    /**
     * Method: nextToken
     * Scans the current token
     * @return the scanned lexeme
     * @throws ScanErrorException if there is an error while scanning
     */
    public String nextToken() throws ScanErrorException
    {
        return nextTokenHelper().replace(" ", "");
    }
    
    /**
     * Method: nextToken
     * Scans the current token
     * @return the scanned lexeme
     * @throws ScanErrorException if there is an error while scanning
     */
    public String nextTokenHelper() throws ScanErrorException
    {
        while(isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if(eof)
        {
            return "END";
        }
        if(hasNext())
        {
            if(currentChar == '/')
            {
                eat(currentChar);
                if(currentChar == '/')
                {
                    while(currentChar!='\n' && hasNext())
                    {
                        eat(currentChar);
                    }
                    return nextToken();
                }
                else
                    return "/";
            }
            if (isOperand(currentChar))
                return scanOperand();
            if (isDigit(currentChar))
                return scanNumber();
            if (isLetter(currentChar))
                return scanIdentifier();
            if (currentChar == ';')
            {
                eat(currentChar);
                return ";";
            }
            if(currentChar == ',')
            {
                eat(currentChar);
                return ",";
            }
        }
        throw new ScanErrorException("Unknown Character");
    }
}
