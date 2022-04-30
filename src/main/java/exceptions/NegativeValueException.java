package exceptions;

public class NegativeValueException extends Exception{
    public NegativeValueException(){
        super("Substracted value should be lower or equal to the original number");
    }
}
