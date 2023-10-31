

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
/*
    The Purpose of the AdminLogin class is to handle all the Login operations for the admin users
    @Constructor -> it take a Socket, a hashmap , an items.
    @loginAsAdmin -> is the main method of this class it should be always called when using this class, its used to check weather there is an account with the username, password
    and start the login operations
    @adminLogin its used to get the operations from the client when we successfully logged in as an admins and send the responses to the clients
    @showAllItems its print all items in an item arrayList as the name clearly shows


 */
public class AdminLogin {
    Socket adminSocket;
    private HashMap<String, String> adminsAccounts;
    private  ArrayList<Item> items;
    private DataInputStream din;
    private DataOutputStream dout;
    PrintStream pout;


    public AdminLogin(Socket adminSocket, HashMap<String, String> adminsAccounts, ArrayList<Item> items) throws IOException {
        this.adminSocket = adminSocket;
        this.adminsAccounts = adminsAccounts;
        this.items = items;
        din = new DataInputStream(adminSocket.getInputStream());
        dout = new DataOutputStream(adminSocket.getOutputStream());
        pout = new PrintStream(adminSocket.getOutputStream());

    }

    public void loginAsAdmin() throws IOException {
        while (true) {
            String userName = din.readUTF();
            String password = din.readUTF();
            if (adminsAccounts.containsKey(userName)) {
                if (password.equals(adminsAccounts.get(userName))) {
                    pout.println("admin");
                    adminLogin();
                    break;
                } else {
                    pout.println("failed");
                }
            } else pout.println("failed");
        }
    }


    private synchronized void adminLogin() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            PrintStream pout = new PrintStream(adminSocket.getOutputStream());
            ObjectInputStream oin = new ObjectInputStream(adminSocket.getInputStream());
            ObjectOutputStream oout = new ObjectOutputStream(adminSocket.getOutputStream());

            String allitems = showAllItems();
            oout.writeObject(allitems);
            while (true) {
                String response = reader.readLine();

                if (response.equals("add")) {

                    items.add((Item) oin.readObject());
                    allitems = showAllItems();
                    oout.writeObject(allitems);
                } else if (response.equals("remove")) {
                    int idRecived = Integer.parseInt(reader.readLine());
                    for (int j = 0; j < items.size(); j++) {
                        if (idRecived == items.get(j).id) items.remove(j);
                    }
                    allitems = showAllItems();
                    oout.writeObject(allitems);
                } else if (response.equals("modify")) {

                    String temp = reader.readLine();
                    int idRecived = Integer.parseInt(temp);
                    for (int j = 0; j < items.size(); j++) {
                        if (idRecived == items.get(j).id) {
                            pout.println("Enter new ID");
                            items.get(j).id = Integer.parseInt(reader.readLine());
                            pout.println("Enter new name");
                            items.get(j).name = (reader.readLine());
                            pout.println("Enter new priceWithoutTax");
                            items.get(j).setPriceWithoutTax(Double.parseDouble(reader.readLine()));
                        }
                    }
                    allitems = showAllItems();
                    oout.writeObject(allitems);
                } else if (response.equals("exit")) {
                    ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("items.out"));
                    out.writeObject(items);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    public String showAllItems() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            s.append(items.get(i).toString()).append("\n");
        }
        return s.toString();
    }




}
