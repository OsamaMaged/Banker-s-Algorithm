package BankerAlgo;

import java.util.ArrayList;
import java.util.Scanner;

class bankers {

    public int need[][], allocate[][], available[], n, m, request[], temp[];
    ArrayList<String> Queue = new ArrayList<String>();
    boolean safe;
    boolean ZeroNeed;

    public void input() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter no. of processes(n) : ");
        n = sc.nextInt();
        System.out.println("Enter no. of resources(m)  : ");
        m = sc.nextInt();
        need = new int[n][m];
        allocate = new int[n][m];
        available = new int[m];
        temp = new int[m];
        safe = true;

        System.out.print("Enter Available Matrix :");
        for (int j = 0; j < m; j++) {
            available[j] = sc.nextInt();
            temp[j] = available[j];
        }

        //fill allocate with zeros
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocate[i][j] = 0;
            }
        }

        //fill need matrix with random number
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = (int) (Math.random() * 10);
            }

        }

        //Print need matrix
        System.out.println("Need matrix :");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + i + "  ");
            for (int j = 0; j < m; j++) {
                System.out.print(need[i][j] + " ");
            }
            System.out.println("");
        }

        Safe();
        if (!safe) {
            return;
        }
        System.out.print("Safe Queue:");
        for (int i = 0; i < Queue.size(); i++) {
            System.out.print(Queue.get(i) + " ");
        }

        System.out.println("");

        while (!ZeroNeed) {
            Requests();
            System.out.println("");
        }
        System.out.print("Final Available is ( ");
        for (int i = 0; i < m; i++) {
            System.out.print(temp[i] + " ");
        }
        System.out.print(" )");

    }

    public boolean Safe() {
        boolean flag[] = new boolean[n];
        int count = 0;
        while (count < n) {
            boolean error = true;
            for (int i = 0; i < n; i++) {
                if (!flag[i] && check(i)) {
                    for (int j = 0; j < m; j++) {
                        available[j] = available[j] + allocate[i][j];
                    }
                    Queue.add("P" + i);
                    count++;
                    flag[i] = true;
                    error = false;
                }
            }
            if (error) {
                safe = false;
                break;
            }
        }
        if (count == n) {
            safe = true;
            System.out.println("Safe");
            return true;
        } else {
            safe = false;
            System.out.println("not Safe");
            return false;
        }
    }

    public boolean check(int i) {
        // check if all resources for i process can be allocated or not 
        for (int j = 0; j < m; j++) {
            if (available[j] < need[i][j]) {
                return false;
            }
        }
        return true;
    }

    public boolean checkRequest(int i) {
        for (int j = 0; j < m; j++) {
            if (request[j] > need[i][j] || request[j] > available[j]) {
                System.out.println("Request of P" + i + " > need or available");
                return false;
            }
        }
        return true;
    }

    public void Requests() {
        request = new int[m];
        int x = (int) (Math.random() * (n));

        for (int i = 0; i < m; i++) {//the available starting the request
            available[i] = temp[i];
        }
        //get random request
        for (int j = 0; j < m; j++) {
            request[j] = (int) (Math.random() * 12);
        }

        //print request
        System.out.print("\nP" + x + " requests ( ");
        for (int j = 0; j < m; j++) {
            System.out.print(request[j] + " ");
        }
        System.out.println(")");

        //check if request access is denied or granted
        if (checkRequest(x)) {
            for (int j = 0; j < m; j++) {
                {
                    available[j] -= request[j];
                    allocate[x][j] += request[j];
                    need[x][j] -= request[j];
                }
            }
            if (!Safe()) {
                System.out.println("Request rejected");
                for (int i = 0; i < m; i++) {//resets available
                    // available[i] = work[i];
                    available[i] = temp[i];
                }
                for (int j = 0; j < m; j++) {
                    allocate[x][j] -= request[j];
                    need[x][j] += request[j];
                }
                System.out.print("Available is ( ");
                for (int i = 0; i < m; i++) {
                    System.out.print(temp[i] + " ");
                }
                System.out.print(" )");

            } else {
                for (int i = 0; i < m; i++) {//resets available

                    available[i] = temp[i] - request[i];

                    temp[i] = available[i];

                }
                System.out.println("Request accepted");
                System.out.print("New Allocation is (");
                for (int i = 0; i < m; i++) {
                    System.out.print(allocate[x][i] + " ");
                }
                System.out.println(")");
                System.out.print("Available is ( ");
                for (int i = 0; i < m; i++) {
                    System.out.print(temp[i] + " ");
                }
                System.out.println(" )");
            }
        } else {
            System.out.println("Request denied");
            System.out.print("Available is ( ");
            for (int i = 0; i < m; i++) {
                System.out.print(temp[i] + " ");
            }
            System.out.print(" )");
        }

        ZeroNeed = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (need[i][j] > 0) {
                    ZeroNeed = false;
                    break;
                }
            }
        }

        boolean EndProcess = false;
        int zeros = 0;
        for (int j = 0; j < m; j++) {
            if (need[x][j] == 0) {
                zeros++;
            }
        }
        if (zeros == m) {
            EndProcess = true;
        }
        if (EndProcess) {
            System.out.println("Need of process " + x + " is now zero");
            for (int j = 0; j < m; j++) {
                temp[j] += allocate[x][j];
                allocate[x][j] = 0;
            }
        }
    }
}

public class BankerAlgo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bankers b = new bankers();
        b.input();

    }

}
