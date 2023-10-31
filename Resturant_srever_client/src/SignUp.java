
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/*
    The Purpose of the SignUp class is to handle all the SignUp operations
    @Constructor -> it take a Socket, a HashMap<String, String> , a HashMap<String, Customer>.
    @getAdminsAccounts & getCustomerAccounts -> getter for the hashmap to update the data in MultiThreadedServer
    @signup -> its the main method of this class which continues in admin signup or customer signup
    @admin -> its used to check if the user signed up as admin correctly or not and save his account info
    @customer -> its used to check if the user signed up as customer correctly or not and save his account info


 */

public class SignUp {
    private static int numberOfAdmin = 1;

    private Socket singUpSocket;
    private HashMap<String, String> adminsAccounts;
    private HashMap<String, Customer> customerAccounts;

    public SignUp(Socket singUpSocket, HashMap<String, String> adminsAccounts, HashMap<String, Customer> customerAccounts) {
        this.singUpSocket = singUpSocket;
        this.adminsAccounts = adminsAccounts;
        this.customerAccounts = customerAccounts;
    }

    public HashMap<String, String> getAdminsAccounts() {
        return adminsAccounts;
    }

    public HashMap<String, Customer> getCustomerAccounts() {
        return customerAccounts;
    }

    public void signup() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(singUpSocket.getInputStream()));
            String choose = reader.readLine();
            if (choose.equals("admin")) {
                admin();
                System.out.println("admin success");
            } else if (choose.equals("customer")) {
                customer();
                System.out.println("customer success");
            }
        } catch (Exception e) {

        }
    }

    private void admin() {
        try {
            OutputStream sout = singUpSocket.getOutputStream();
            FileOutputStream fout1 = new FileOutputStream("admin.out");
            ObjectOutputStream oout1 = new ObjectOutputStream(fout1);
            PrintStream pout = new PrintStream(sout);
            BufferedReader reader = new BufferedReader(new InputStreamReader(singUpSocket.getInputStream()));
            String s = "" + genID(numberOfAdmin++);
            pout.println("Here is the code" + s);
            while (true) {
                String username = reader.readLine();
                String password = reader.readLine();
                String code = reader.readLine();
                if (!code.equals(s) || adminsAccounts.containsKey(username) || customerAccounts.containsKey(username)) {
                    pout.println("failed");
                    continue;
                } else {
                    adminsAccounts.put(username, password);
                    oout1.writeObject(adminsAccounts);
                    pout.println("you have successfully signed up!");
                    break;
                }
            }
        } catch (Exception e) {

        }
    }

    private void customer() {
        try {

            OutputStream sout = singUpSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            FileOutputStream fout1 = new FileOutputStream("customer.out");
            ObjectOutputStream oout1 = new ObjectOutputStream(fout1);
            BufferedReader reader = new BufferedReader(new InputStreamReader(singUpSocket.getInputStream()));

            while (true) {
                String username = reader.readLine();
                String password = reader.readLine();

                if (adminsAccounts.containsKey(username) || customerAccounts.containsKey(username)) {
                    pout.println("failed");


                } else {


                    customerAccounts.put(username, new Customer(username, password));
                    oout1.writeObject(customerAccounts);
                    pout.println("you have successfully signed up!");


                    break;
                }
            }
        } catch (Exception e) {

        }
    }

    private int genID(int x) {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) {
            ids.add(i);
        }
        Collections.shuffle(ids);
        return ids.get(x);
    }


}
