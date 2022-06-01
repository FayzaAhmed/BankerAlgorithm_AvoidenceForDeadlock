import java.util.ArrayList;

public class Banker {
    public int rows;
    public int columns;
    public int [] available ;
    public int [][] maximum ;
    public int [][] allocation ;
    public int [][] need;
    public String state;
    public ArrayList processesSequence = new  ArrayList<Integer>();

    public Banker(int [] available,int [][] maximum , int [][] allocation , int [][] need ,int rows , int columns){
        this.available = available;
        this.maximum = maximum;
        this.allocation = allocation;
        this.need = need;
        this.state = "safe";
        this.rows = rows;
        this.columns = columns;
    }

    public void calculateNewAvailable (int [] RQ ){
        for(int i = 0; i < columns; i++){
            available[i] -= RQ[i];
        }
    }

    public void calculateNewAllocation(int index , int [] RQ){
        for(int i = 0; i < columns; i++){
            allocation[index][i] += RQ[i];
        }
    }

    public void calculateNewNeed(int index , int [] RQ){
        for(int i = 0; i < columns; i++){
            need[index][i] -= RQ[i];
        }
    }

    public void print2D(int [][] matrix){
        for(int i = 0 ; i < rows; i++){
            for(int j = 0 ; j < columns; j++){
                System.out.print(matrix[i][j] + "   ");
            }
            System.out.println();
        }
    }

    public void print1D(int [] arr){
        for(int i = 0 ; i < arr.length; i++)
            System.out.print(arr[i] + "   ");
        System.out.println();
    }

    public void print(){
        System.out.println("Status : " + state);

        System.out.println("The available resource values ");
        print1D(available);

        System.out.println("The maximum demand values  ");
        print2D(maximum);

        System.out.println("The allocation values  ");
        print2D(allocation);

        System.out.println("The need values ");
        print2D(need);

        System.out.println();
    }

    public boolean safetyAlgorithm(){
        int counter = 0;
        int [] work = new int[columns];
        boolean [] finish = new boolean[rows];

        for(int i = 0 ; i < rows; i++){
            finish[i] = false;
        }

        for(int i = 0 ; i < columns; i++){
            work[i] = available[i];
        }

        ArrayList tempList = new ArrayList <Integer>();

        int j ;
        while(counter < rows) {
            for (int i = 0; i < rows; i++) {
                if(!finish[i]) {
                    for (j = 0; j < columns; j++) {
                        if (need[i][j] >  work[j]) {
                            break;
                        }
                    }
                    if(j == columns)
                    {
                        for(j = 0; j < columns; j++)
                            work[j] += allocation[i][j];
                        finish[i] = true;
                        processesSequence.add(i);
                        counter++;
                    }
                }
            }
            tempList.add(counter);
            if(tempList.size() >= 2)
                if(tempList.get(tempList.size() - 1) == tempList.get(tempList.size() - 2)){
                    break;
                }
        }

        for(int i = 0 ; i < rows; i++){
            if(!finish[i])
            {
                state = "unsafe";
                return false;
            }
        }
        state = "safe";

        System.out.print("The sequence <");
        for (int i=0; i<processesSequence.size(); i++){
            System.out.print(" P"+processesSequence.get(i));
        }
        System.out.println(" >");

        processesSequence.clear();
        return true;
    }

    public void bankerAlgorithm(int pIndex , int [] RQ){
        boolean check = true ;
        // first check : if request < need
        for(int i = 0; i < columns ; i++){
            if(RQ[i] > need[pIndex][i]){
                check = false;
                state = "The process P" + pIndex + " has exceeded its maximum claim";
                break;
            }
        }
        // second check : if request < available
        if(check){
            for(int i = 0; i < columns ; i++){
                if(RQ[i] > available[i]){
                    check = false;
                    state = "No enough resources available ,  The process " + pIndex +  " must wait";
                    break;
                }
            }
            // If two checks are satisfied
            if(check){
                calculateNewAvailable(RQ);
                calculateNewAllocation(pIndex , RQ);
                calculateNewNeed(pIndex , RQ);
                boolean result = safetyAlgorithm();
                if(result)
                {
                    System.out.println("The resources are allocated to  P" + pIndex);
                }
                else
                {
                    System.out.println("P" + pIndex + " must wait ");
                }
            }
        }
    }

    public void release (int pIndex , int [] RL) {
        for(int j = 0 ; j < columns; j++) {
            if(RL[j] > allocation[pIndex][j]){
                System.out.println("The process can not release the resources");
                return;
            }
        }
        System.out.println("The process p"+ pIndex+" released the resources");
        for(int j = 0 ; j < columns; j++) {
            allocation[pIndex][j] -= RL[j];
            need[pIndex][j] += RL[j];
            available[j] += RL[j];
        }
    }

    //Select the victim based on the priority assuming that the last process is the lowest priority.
    public void recoveryAlgorithm(int victimIndex){
        int [] processAllocation = new int[columns];
        for(int j=0; j<columns; j++)
            processAllocation[j] = allocation[victimIndex][j];

        release(victimIndex, processAllocation);
        safetyAlgorithm();
    }
}