package exceptions;

public class MaximumNumberOfDecimalExceededException extends Exception{
    public MaximumNumberOfDecimalExceededException(){
        super("Maximum number of decimals allowed is 100.000");
    }
}
