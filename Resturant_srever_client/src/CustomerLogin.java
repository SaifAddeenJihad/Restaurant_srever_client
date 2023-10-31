
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CustomerLogin {
    private Socket customerSocket;
    private ObjectOutputStream objectOutputStream;
    private DataInputStream din;
    private HashMap<String, Customer> customerAccounts;
    private  ArrayList<Item> items;
    private DataOutputStream dout;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    static int orderCode = 1;
    PrintStream pout;
    BufferedReader reader;
/*
    The Purpose of the CustomerLogin class is to handle all the Login operations for the customers users
    @Constructor -> it take a Socket, a hashmap , an items.
    @loginAsCustomre -> is the main method of this class it should be always called when using this class, its used to check weather there is an account with the username, password
    and start the login operations
    @customerLogin its used to get the operations from the client when we successfully logged in as an admins and send the responses to the clients
    @showAllItems its print all items in an item arrayList as the name clearly shows
    @doRead  is used to read the ArrayList of ids sent by the clients
    @listOrder

 */



    public CustomerLogin(Socket customerSocket, HashMap<String, Customer> customerAccounts, ArrayList<Item> items) throws IOException {
        this.customerSocket = customerSocket;
        this.customerAccounts = customerAccounts;
        this.items = items;
        objectOutputStream = new ObjectOutputStream(customerSocket.getOutputStream());
        dout = new DataOutputStream(customerSocket.getOutputStream());
        din = new DataInputStream(customerSocket.getInputStream());
        pout = new PrintStream(customerSocket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(customerSocket.getInputStream()));
        oos = new ObjectOutputStream(customerSocket.getOutputStream());


    }

    public void loginAsCustomre() throws IOException {
        while (true) {

            String userName = din.readUTF();

            String password = din.readUTF();

            if (customerAccounts.containsKey(userName)) {
                if (password.equals(customerAccounts.get(userName).getPassword())) {
                    pout.println("customer");
                    customerLogin(userName);
                    break;

                } else {

                    pout.println("failed");
                }
            }else{

                pout.println("failed");}
        }
    }

    private void customerLogin(String username) {
        try {
            String allitems;
            //ObjectOutputStream oout = new ObjectOutputStream(customerSocket.getOutputStream());
            Customer customer = customerAccounts.get(username);

            while (true) {


                int response = Integer.parseInt(reader.readLine()) ;

                switch (response){
                    case 1:{
                        allitems = showAllItems();
                        dout.writeUTF(allitems);
                        ArrayList<Integer> ids = doRead();
                        Order order = new Order(genID(orderCode++));

                        for (Integer id : ids) {
                            for (Item item : items) {
                                if (id == item.id) order.addItem(item);
                            }
                        }
                        customer.addOrder(order);
                        dout.writeUTF(order.showalliteams());
                        ObjectOutputStream oisa = new ObjectOutputStream(new FileOutputStream("customer.out"));
                        oisa.writeObject(customerAccounts);
                        continue;
                    }
                    case 2:{
                        listOrder(username);
                        continue;
                    }
                    case 3:{

                        FileOutputStream fout1 = new FileOutputStream("customer.out");
                        ObjectOutputStream oout1 = new ObjectOutputStream(fout1);
                        oout1.writeObject(customerAccounts);
                        break;
                    }
                }
                break;
                /*if (response==1) {
                    allitems = showAllItems();
                    dout.writeUTF(allitems);
                    ArrayList<Integer> ids = doRead();
                    Order order = new Order(orderCode++);

                    for (Integer id : ids) {
                        for (Item item : items) {
                            if (id == item.id) order.addItem(item);
                        }
                    }
                    customer.addOrder(order);
                    dout.writeUTF(order.showalliteams());
                    ObjectOutputStream oisa = new ObjectOutputStream(new FileOutputStream("customer.out"));
                    oisa.writeObject(customerAccounts);

                } else if (response==2) {
                    listOrder(username);
                } else if (response==3) {
                    System.out.println("exit");
                    FileOutputStream fout1 = new FileOutputStream("customer.out");
                    ObjectOutputStream oout1 = new ObjectOutputStream(fout1);
                    oout1.writeObject(customerAccounts);
                    break;

                }*/
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

    public ArrayList<Integer> doRead() throws IOException, ClassNotFoundException {
        ArrayList<Integer>arrayA = new ArrayList<>();
        int arrayLenght = din.readInt();
        for(int i=0;i<arrayLenght;i++)
        {
            arrayA.add(din.readInt());
        }

        return arrayA;
    }

    private void listOrder(String username) {
        try {


            Customer customer = customerAccounts.get(username);
            dout.writeUTF(customer.printAllOrders());

            String response = (String) reader.readLine();

            if (response.equals("reorder")) {
                reorder(customer);
            } else if (response.equals("showorder")) {

                showOrder(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void reorder(Customer customer) {
        try {

            int ordercode = Integer.parseInt(reader.readLine());

            Order old=customer.getOrder(ordercode);
            ArrayList<Item> oldi= old.getItems();
            int x =genID(ordercode);
            Order neworder = new Order(x);
            for(Item e :oldi )neworder.addItem(e);

            dout.writeUTF(neworder.showalliteams());

            while (true) {
                String choose = reader.readLine();

                if (choose.equals("add")) {

                    int id = din.readInt();
                    for (int i = 0; i < items.size(); i++) {
                        if (id == items.get(i).id) {
                            neworder.addItem(items.get(i));
                            break;
                        }
                    }


                } else if (choose.equals("remove")) {
                    int id = din.readInt();
                    neworder.removeitem(id);

                } else if (choose.equals("finish")) {

                    customer.addOrder(neworder);
                    dout.writeUTF(neworder.showalliteams());
                    break;
                }

            }


        } catch (Exception e) {

        }
    }

    public void showOrder(Customer customer) {
        try {

            int ordercode = din.readInt();
            Order order = customer.getOrder(ordercode);
            dout.writeUTF(order.showalliteams() + "\n Date Created : " + order.getDateCreated() + "\n Order Price : " + order.getprice());
            while (true) {

                String response = (String) din.readUTF();

                synchronized (items){

                if (response.equals("addreview")) {
                    int id=din.readInt();
                    String review=din.readUTF();
                    for (int i = 0; i < items.size(); i++) {
                        if (id == items.get(i).id)
                            items.get(i).addReview(review);
                    }

                } else if (response.equals("exit")) {
                    break;
                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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