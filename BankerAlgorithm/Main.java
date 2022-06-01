// Nada Omar Fathi  20190581
// Fayza Ahmed Sayed  20190376
// Yomna Mahmoud Abdelwahab 20190625
// Asmaa Refaat AbdElmabood  20190101

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int n; //number of processes
        int m; //number of resources

        System.out.println("Enter valid number of processes: ");
        n = input.nextInt();
        System.out.println("Enter the number of resources: ");
        m = input.nextInt();

        int [] available = new int[m]; //the available amount of each resource
        int [][] maximum = new int[n][m]; //the maximum demand of each process
        int [][] allocation = new int[n][m]; //the amount currently allocated to each process
        int [][] need = new int[n][m]; //the remaining needs of each process

        //take the initial number of the available resources
        System.out.println();
        System.out.println("Enter the available resource values accordingly: ");
        for(int i = 0; i < m; i++)
        {
            available[i] = input.nextInt();
        }

        //take the maximum need
        System.out.println();
        System.out.println("Enter the maximum demand values accordingly: ");
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                maximum[i][j] = input.nextInt();
            }
        }

        //take the actually allocated resources for each process
        System.out.println();
        System.out.println("Enter the allocation values accordingly: ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocation[i][j] = input.nextInt();
            }
        }

        //calculate the need matrix
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < m; j++)
            {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }

        Banker banker = new Banker(available,maximum ,  allocation ,  need , n ,  m);
        banker.print();

        String choice = input.next();
        while(!choice.equalsIgnoreCase("Quit")) {
           if(choice.equalsIgnoreCase("RQ")){
                int pIndex = input.nextInt();
                int[] RQ = new int[m];
                for (int i = 0; i < m; i++) {
                    RQ[i] = input.nextInt();
                }
                banker.bankerAlgorithm(pIndex, RQ);
                banker.print();
            }

           else if(choice.equalsIgnoreCase("RL")){
                int pIndex = input.nextInt();
                int[] RL = new int[m];
                for (int i = 0; i < m; i++) {
                    RL[i] = input.nextInt();
                }
                banker.release(pIndex, RL);
                banker.print();
           }

           else if(choice.equalsIgnoreCase("Recover"))
           {
               int victimIndex = n-1;
               while(victimIndex>=0 && banker.state.equals("unsafe"))
               {
                   banker.recoveryAlgorithm(victimIndex);
                   victimIndex--;
               }
               System.out.println("Recovery is done");
               System.out.println();
           }

           else System.out.println("Invalid input , Enter RQ or RL");

            choice = input.next();
        }
    }
}