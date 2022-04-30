package exceptions;

public class DivisionByZeroException extends Exception{
    public DivisionByZeroException(){
        super("Division by 0 is not mathematically possible");
    }
}
