package exceptions;

public class InvalidNumberFormatException extends Exception{
    public InvalidNumberFormatException(){
        super("The string must contain only decimals");
    }
}
