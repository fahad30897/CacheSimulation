import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Driver {


    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int cachePower , blockPower , noOfDimensions, accessType, elementSize, stride , maxAddressSize, setPower;
        System.out.println("Enter size of cache (power of 2):");


        cachePower = Integer.parseInt(reader.readLine().trim());

        System.out.println("Enter size of a block(power of 2):");
        blockPower = Integer.parseInt(reader.readLine().trim());

        System.out.println("Enter no. of dimensions of array:");
        noOfDimensions = Integer.parseInt(reader.readLine().trim());

        int arrSize =1;
        for(int i = 0 ;i<noOfDimensions;i++){
            System.out.println(String.format("Enter size of Array in %d dimesion:", (i+1)));
            arrSize *= Integer.parseInt(reader.readLine().trim());
        }


        System.out.println("Enter type of access: 0 - row major , 1 - column major");
        accessType = Integer.parseInt(reader.readLine().trim());

        System.out.println("Enter size of single element:");
        elementSize = Integer.parseInt(reader.readLine().trim());

        System.out.println("Enter the stride");
        stride = Integer.parseInt(reader.readLine().trim());
        maxAddressSize = Integer.toBinaryString(arrSize * elementSize).length();

        Cache cache = null;
        System.out.println("Enter the type of cache ( FullyAssociative: F, DirectMapped: D, SetAssociative: S)");
        char c = reader.readLine().charAt(0);
//        String start = Stream.generate(() -> "0").limit(maxAddressSize).collect(Collectors.joining());
        switch (c){
            case 'D':
                cache = new FullyAssociativeCache(cachePower,blockPower);
                break;
            case 'F':
                cache = new FullyAssociativeCache(cachePower,blockPower);
                break;
            case 'S':
                System.out.println("Enter set Power - n (2^n-way set associative)");
                setPower = Integer.parseInt(reader.readLine().trim());
                cache = new SetAssociativeCache(cachePower,blockPower , setPower);
                break;
            default:
                cache = new DirectMappedCache(cachePower,blockPower);
        }

        int totalMiss = 0;
        for(int i = 0 ; i < arrSize; i+=stride){
            String newAddress ;

            //For row major
            if(accessType == 0) {
                newAddress = String.format("%" + maxAddressSize + "s", Integer.toBinaryString(i * elementSize)).replaceAll(" ", "0");
            }
            // For Column Major
            else{
                newAddress = String.format("%" +maxAddressSize + "s", Integer.toBinaryString(i*elementSize)).replaceAll(" " , "0");

            }
            if(!cache.load(newAddress)){
                System.out.println(newAddress + " was a miss");
                totalMiss++;
            }
            else{
                System.out.println(newAddress + " was a hit!");
            }
//            System.out.println("-----------------------------------");
//            System.out.println(cache);
//            System.out.println("-----------------------------------");
        }

        System.out.println(totalMiss);
    }
}
