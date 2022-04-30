public class Main {
    public static void main(String[] args) {
        try {
            BigInt n1 = new BigInt("654154154151454545415415454");
            BigInt n2 = new BigInt("63516561563156316545145146514654");
            BigInt n3 = new BigInt("1248163264128256512");
            BigInt n4 = new BigInt("216");

            System.out.println(n1.multiply(n2));
            System.out.println(n3.divide(125));
            System.out.println(n3.pow(3));
            System.out.println(n4.sqrt(3));
        }catch (Exception ignored){

        }




    }
}
