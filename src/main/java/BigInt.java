import exceptions.DivisionByZeroException;
import exceptions.InvalidNumberFormatException;
import exceptions.MaximumNumberOfDecimalExceededException;
import exceptions.NegativeValueException;

import java.util.Arrays;
import java.util.Objects;

public class BigInt implements Comparable<BigInt>{
    private final int numberOfDigits;
    private final int[] value;
    private final static int MAX_NUMBER_OF_DIGITS = 100_000;

    public BigInt(){
        numberOfDigits = 0;
        value = null;
    }

    public BigInt(String val) throws MaximumNumberOfDecimalExceededException, InvalidNumberFormatException {

        if(val.length() > MAX_NUMBER_OF_DIGITS){
            throw new MaximumNumberOfDecimalExceededException();
        }

        //Alocate the memmory
        numberOfDigits = val.length();
        value = new int[numberOfDigits];

        for(int i=0; i<val.length(); i++){
            int digit = Character.getNumericValue(val.charAt(i));
            if(digit < 0 || digit >9){
                throw new InvalidNumberFormatException();
            }

            value[val.length() - i - 1] = digit;
        }
    }

    public BigInt(int[] value){
        this.value = value;
        this.numberOfDigits = value.length;
    }

    public BigInt(BigInt b){
        this.value = b.getValue();
        this.numberOfDigits = b.getNumberOfDigits();
    }

    public BigInt(int i){
        int length = String.valueOf(i).length();
        this.value = new int[length];
        this.numberOfDigits = length;

        int counter = 0;

        while(i > 0){
            this.value[counter] = i % 10;
            i = i / 10;
            counter++;
        }
    }

    public BigInt add(BigInt n) {

        int minDigits = Math.min(this.numberOfDigits, n.getNumberOfDigits());
        int maxDigits = Math.max(this.numberOfDigits, n.getNumberOfDigits());

        int carry = 0;

        String value = "";

        for(int i = 0; i<minDigits; i++){
            int digit = this.value[i] + n.value[i] + carry;
            carry = 0;

            if(digit > 9){
                carry = 1;
                digit = digit - 10;
            }

            value = String.valueOf((digit)).concat(value);
        }

        if(this.numberOfDigits > n.numberOfDigits){
            for(int i = minDigits; i< maxDigits; i++){
                int digit = this.value[i] + carry;
                carry = 0;

                if(digit > 9){
                    carry = 1;
                    digit = digit - 10;
                }

                value = String.valueOf((digit)).concat(value);
            }
        }else{
            for(int i = minDigits; i< maxDigits; i++){
                int digit = n.getValue()[i] + carry;
                carry = 0;

                if(digit > 9){
                    carry = 1;
                    digit = digit - 10;
                }

                value = String.valueOf((digit)).concat(value);
            }
        }

        if(carry > 0){
            value = String.valueOf((carry)).concat(value);
        }

        try {
            return new BigInt(value);
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInt substract(BigInt n) throws NegativeValueException {

        if(this.compareTo(n) < 0){
            throw new NegativeValueException();
        }

        int carry = 0;

        String value = "";

        for(int i = 0; i<n.getNumberOfDigits(); i++){
            int digit = this.value[i] - n.value[i] + carry;
            carry = 0;

            if(digit < 0){
                carry = -1;
                digit = (this.value[i]+10) - n.value[i];
            }

            value = String.valueOf((digit)).concat(value);
        }


        for(int i = n.getNumberOfDigits(); i< this.numberOfDigits; i++){
            int digit = this.value[i] + carry;
            carry = 0;

            if(digit < 0){
                carry = -1;
                digit = (digit+10) + carry;
            }

            value = String.valueOf((digit)).concat(value);
        }

        if(value.startsWith("0")){
            value = value.substring(1);
        }

        try {
            return new BigInt(value);
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BigInt multiply(BigInt n){
        int len1 = this.numberOfDigits;
        int len2 = n.getNumberOfDigits();

        if (len1 == 0 || len2 == 0) {
            try {
                return new BigInt("0");
            } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
                e.printStackTrace();
            }
        }


        int[] result = new int[len1 + len2];
        int i_n1 = 0;
        int i_n2 = 0;

        for (int i = 0; i < len1; i++)
        {
            int carry = 0;
            int n1 = this.value[i];
            i_n2 = 0;

            for (int j = 0; j < len2; j++)
            {
                int n2 = n.getValue()[j];
                int sum = n1 * n2 + result[i_n1 + i_n2] + carry;

                carry = sum / 10;
                result[i_n1 + i_n2] = sum % 10;

                i_n2++;
            }

            if (carry > 0) {
                result[i_n1 + i_n2] += carry;
            }

            i_n1++;
        }

        // ignore '0's from the right
        int i = result.length - 1;
        while (i >= 0 && result[i] == 0) {
            i--;
        }

        if (i == -1) {
            try {
                return new BigInt("0");
            } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
                e.printStackTrace();
            }
        }

        String s = "";

        while (i >= 0) {
            s = s.concat(String.valueOf(result[i--]));
        }

        try {
            return new BigInt(s);
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BigInt divide(int divisor) throws DivisionByZeroException {

        if(divisor == 0){
            throw new DivisionByZeroException();
        }

        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = this.numberOfDigits - 1; i >= 0; i--) {
            int x = carry * 10 + this.value[i];
            result.append(x / divisor);
            carry = x % divisor;
        }

        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) != '0') {
                try {
                    return new BigInt(result.substring(i));
                } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public BigInt pow(int power){
        if(power == 0){
            try {
                return new BigInt("1");
            } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
                e.printStackTrace();
            }
        }

        BigInt b = new BigInt(this);

        for(int i=1; i<power; i++){
            b = new BigInt(b.multiply(this));
        }

        return b;
    }

    public BigInt sqrt(int root){
        for(int i = 1; i<Integer.MAX_VALUE; i++){
            BigInt b = new BigInt(i);
            if(b.pow(root).compareTo(this) > 0){
                return new BigInt(i-1);
            }
        }

        return null;
    }

    public BigInt sqrt(){
       return this.sqrt(2);
    }


    public int[] getValue() {
        return value;
    }

    public int getNumberOfDigits() {
        return numberOfDigits;
    }

    public String convertToString(){
        String s = "";

        for (int j : value) {
            s = String.valueOf(j).concat(s);
        }

        if(s.equals("")){
            return null;
        }

        return s;
    }

    @Override
    public int compareTo(BigInt b){
        if(this.numberOfDigits < b.numberOfDigits){
            return -1;
        }else if(this.numberOfDigits > b.numberOfDigits){
            return 1;
        }

        //by this point it means they have an equal number of digits
        for(int i = this.getNumberOfDigits() - 1; i>=0; i--){
            if(this.value[i] < b.value[i]){
                return -1;
            }else if(this.value[i] > b.value[i]){
                return 1;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigInt BigInt = (BigInt) o;
        return numberOfDigits == BigInt.numberOfDigits && Arrays.equals(value, BigInt.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numberOfDigits);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public String toString() {
        return this.convertToString();
    }
}
