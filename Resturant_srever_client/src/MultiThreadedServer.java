import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
/*
The Purpose of the MultiThreadedServer class is to creat new thread when it s gey invoked from the main server
@Constructor -> it take a Socket.
@doSer -> its the method that deserialize 1)item.out that save the Arraylist of items
                                          2)admin.out the save the HashMap of adminsAccount
                                          3)customer.out the save the HashMap of customerAccount
@run -> its the main method of multi threading which will ne invoked when i start the thread
*/
public class MultiThreadedServer extends Thread {
    Socket socket;
    DataInputStream din;
    BufferedReader br;
    DataOutputStream dout;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    private static HashMap<String, Customer> customersAccount;
    private static HashMap<String, String> adminsAccount;
    static ArrayList<Item> items;


    public MultiThreadedServer(Socket so) {
        this.socket = so;
    }

    public static void doSer() throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream("admin.out");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                // Read the vector back from the list
                Object obj1 = objectInputStream.readObject();

                // Cast back to a vector
                adminsAccount = (HashMap<String, String>) obj1;
            } catch (ClassCastException | ClassNotFoundException cce) {
                adminsAccount = new HashMap<String, String>();
            }
            fileInputStream.close();
        } catch (FileNotFoundException | EOFException fileNotFoundException) {
            // Create a blank vector
            adminsAccount = new HashMap<String, String>();
        }
        try {

            FileInputStream fileInputStream1 = new FileInputStream("customer.out");
            ObjectInputStream objectInputStream1 = new ObjectInputStream(fileInputStream1);
            try {
                // Read the vector back from the list
                Object obj2 = objectInputStream1.readObject();
                // Cast back to a vector
                customersAccount = (HashMap<String, Customer>) obj2;
            } catch (ClassCastException | ClassNotFoundException cce) {
                customersAccount = new HashMap<String, Customer>();
            }
            fileInputStream1.close();
        } catch (FileNotFoundException | EOFException fileNotFoundException) {
            // Create a blank vector
            customersAccount = new HashMap<String, Customer>();

        }
        try {
            FileInputStream fileInputStream = new FileInputStream("items.out");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                // Read the vector back from the list
                Object obj1 = objectInputStream.readObject();

                // Cast back to a vector
                items = (ArrayList<Item>) obj1;
            } catch (ClassCastException | ClassNotFoundException cce) {

                items = new ArrayList<Item>();
            }
            fileInputStream.close();
        } catch (FileNotFoundException | EOFException fileNotFoundException) {
            // Create a blank vector

            items = new ArrayList<Item>();
        }
    }






    @Override
    public void run() {
        try {

            din = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
            dout = new DataOutputStream(socket.getOutputStream());

            while (true) {

                String userInput = "";
                userInput = (String) din.readUTF();
                if(userInput.equals("SignUp"))
                {

                    SignUp XsignUp = new SignUp(socket,adminsAccount,customersAccount);
                    XsignUp.signup();
                    adminsAccount= XsignUp.getAdminsAccounts();
                    customersAccount =XsignUp.getCustomerAccounts();
                }
                else if(userInput.equals("Login")) {

                    String userInput2 ="";
                    userInput2 = (String)din.readUTF();

                    if(userInput2.equals("admin"))
                    {
                        AdminLogin XadminLogin = new AdminLogin(socket,adminsAccount,items);
                        XadminLogin.loginAsAdmin();
                    }
                    else if(userInput2.equals("customer"))
                    {
                        CustomerLogin customerLogin = new CustomerLogin(socket, customersAccount, items);
                        customerLogin.loginAsCustomre();
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

