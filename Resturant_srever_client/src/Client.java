import jdk.swing.interop.SwingInterOpUtils;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    public static final int ServicePort = 7;
    public static Scanner s;
    static Socket ClientSocket;
    public static DataOutputStream dos;
    public static DataInputStream din;
    public static ArrayList<Integer> ids;

    public Client() {
    }

    public static void main(String[] args) {
        try {
            ids = new ArrayList<Integer>();

            ClientSocket = new Socket(InetAddress.getLoopbackAddress(), 3333);
            OutputStream sout = ClientSocket.getOutputStream();
            din = new DataInputStream(ClientSocket.getInputStream());
            new PrintStream(sout);
            dos = new DataOutputStream(ClientSocket.getOutputStream());
            while (true) {
                while (true) {
                    switch (askUser()) {
                        case 1:
                            signUp();
                            break;
                        case 2:
                            logIn();
                            break;
                    }
                }
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public static int askUser() {
        int y = 0;

        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            System.out.println("what do you want?\n1.sign up\n2.log in");
            y = s.nextInt();
            s.nextLine();
            if (y == 1) {
                dos.writeUTF("SignUp");
            } else if (y == 2) {
                dos.writeUTF("Login");
            }
        } catch (Exception var3) {
        }

        return y;
    }

    public static void signUp() {
        System.out.println("do you want sign up like\n1.admin\n2.customer");
        int choose = s.nextInt();
        s.nextLine();
        if (choose == 1) {
            admin();
        } else if (choose == 2) {
            customer();
        }

    }

    public static void admin() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println("admin");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            String sms = reader.readLine();

            while (true) {
                System.out.println(sms);
                System.out.println("Enter your user name:");
                String username = s.nextLine();
                System.out.println("Enter your password:");
                String password = s.nextLine();
                System.out.println("Enter your verfication code:");
                String code = s.nextLine();
                pout.println(username);
                pout.println(password);
                pout.println(code);
                String response = reader.readLine();
                if (!response.equals("failed")) {
                    System.out.println(response);
                    break;
                }
                else {

                System.out.println("invalid user");
                continue;}
            }
        } catch (Exception var8) {
        }

    }

    public static void customer() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println("customer");

            while (true) {
                System.out.println("Enter your user name:");
                String username = s.nextLine();
                System.out.println("Enter your password:");
                String password = s.nextLine();
                pout.println(username);
                pout.println(password);
                BufferedReader reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
                String response = reader.readLine();
                if (!response.equals("failed")) {
                    System.out.println(response);
                    break;
                }

                System.out.println("invalid user");
            }
        } catch (Exception var6) {
        }

    }

    public static void logIn() throws IOException {
        boolean flag = true;
        System.out.println("admin or customer?!");
        String adminOrCus = s.nextLine();
        System.out.println(adminOrCus);
        dos.writeUTF(adminOrCus);
        while (flag) {
            System.out.println("Enter your user name:");
            String username = s.nextLine();
            System.out.println("Enter your password:");
            String password = s.nextLine();

            try {
                String response = "";
                OutputStream sout = ClientSocket.getOutputStream();
                PrintStream pout = new PrintStream(sout);
                dos.writeUTF(username);
                dos.writeUTF(password);
                dos.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
                System.out.println("GOOD");

                response = reader.readLine();
                char[] x = response.toCharArray();
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < response.length(); i++) {
                    if (x[i] >= 'A' && x[i] <= 'Z' || x[i] >= 'a' && x[i] <= 'z') {
                        result.append(x[i]);

                    }


                }
                System.out.println(result);


                if (response.equals("failed")) {
                    System.out.println("invalid");
                } else if (result.toString().equals("admin")) {
                    flag = askadmin();
                } else if (result.toString().equals("customer")) {
                    flag = askcustomer();
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

    }

    public static boolean askadmin() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(ClientSocket.getOutputStream());
            ObjectInputStream oin = new ObjectInputStream(ClientSocket.getInputStream());
            String allItems = (String) oin.readObject();
            System.out.println(allItems);

            while (true) {
                while (true) {
                    System.out.println("what do you want to do:\n1.add item\n2.remove item\n3.modify\n4.Exit");
                    int choice = s.nextInt();
                    if (choice == 1) {
                        pout.println("add");
                        out.writeObject(new Item());
                        allItems = (String) oin.readObject();
                        System.out.println(allItems);
                    } else {
                        int id;
                        if (choice == 2) {
                            pout.println("remove");
                            System.out.println("Enter item id you want to delete: ");
                            id = s.nextInt();
                            pout.println(id);
                            allItems = (String) oin.readObject();
                            System.out.println(allItems);
                        } else {
                            if (choice != 3) {
                                if (choice == 4) {
                                    Exit();
                                    return false;
                                }

                                return true;
                            }

                            pout.println("modify");
                            System.out.println("Enter the id of the item that you want to modify");
                            id = s.nextInt();
                            s.nextLine();
                            pout.println(id);
                            System.out.println(reader.readLine());
                            pout.println(s.nextLine());
                            System.out.println(reader.readLine());
                            pout.println(s.nextLine());
                            System.out.println(reader.readLine());
                            pout.println(s.nextLine());
                            allItems = (String) oin.readObject();
                            System.out.println(allItems);
                        }
                    }
                }
            }
        } catch (Exception var8) {
            return false;
        }
    }

    public static boolean askcustomer() {
        int count = 0;
        while (true) {
            System.out.println("what do you want to do\nnew order\nlist order\nexit");
            String choice = s.nextLine();
            System.out.println(choice);
            switch (choice) {
                case "neworder":
                    neworder();
                    count++;
                    continue;
                case "listorder":
                    listorder();
                    continue;
                case "exit":
                    Exitnew();
                    return false;

                default:
            }
        }
    }

    public static void neworder() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println(1);
            DataInputStream input = new DataInputStream(ClientSocket.getInputStream());
            System.out.println("sadas");
            String allItems = (String) input.readUTF();
            System.out.println(allItems);
            System.out.println("Enter the id of the item that you want:");


            while (true) {
                String test = "";
                ids.add(s.nextInt());
                s.nextLine();
                System.out.println("do you want to add another item y/n");
                test = s.nextLine();
                if (!test.equals("y")) {
                    dos.writeInt(ids.size());
                    for (var x : ids) {
                        dos.writeInt(x);
                    }
                    String orderItems = (String) input.readUTF();
                    System.out.println(orderItems);
                    break;
                }

                System.out.println("Enter the id of the item that you want:");
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static void listorder() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();

            PrintStream pout = new PrintStream(sout);
            pout.println(2);
            new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            //ObjectInputStream input = new ObjectInputStream(ClientSocket.getInputStream());
            String allOrders = (String) din.readUTF();
            System.out.println(allOrders);
            System.out.println("choose what do you want\n1.Re-order\n2.Show-order");
            int option = s.nextInt();
            if (option == 1) {
                reorder();
            } else if (option == 2) {
                showorder();
            }
        } catch (Exception var7) {
        }

    }

    public static void reorder() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println("reorder");
            System.out.println("Enter your code:");
            int code = s.nextInt();
            pout.println(code);
            System.out.println("code sent");
            new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            //ObjectInputStream input = new ObjectInputStream(ClientSocket.getInputStream());
            String newOrderitems = (String) din.readUTF();
            System.out.println(newOrderitems);


            while (true) {
                System.out.println("what do you want:\n1.add item\n2.remove item\n3.finish");
                int any = s.nextInt();
                int id;
                if (any == 1) {
                    pout.println("add");
                    System.out.println("enter id of item you want: ");
                    id = s.nextInt();
                    s.nextLine();
                    dos.writeInt(id);
                } else if (any == 2) {
                    pout.println("remove");
                    System.out.println("enter id of item you want to remove: ");
                    id = s.nextInt();
                    s.nextLine();
                    dos.writeInt(id);
                } else if (any == 3) {
                    pout.println("finish");
                    s.nextLine();
                    System.out.println("this is your item");
                    System.out.print((String) din.readUTF());
                    break;
                }
            }

        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    public static void showorder() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println("showorder");
            System.out.println("Enter your code:");
            int code = s.nextInt();
            s.nextLine();
            dos.writeInt(code);
            new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            //ObjectInputStream input = new ObjectInputStream(ClientSocket.getInputStream());
            //ObjectOutputStream oout = new ObjectOutputStream(ClientSocket.getOutputStream());
            String Orderdetails = (String) din.readUTF();
            System.out.println(Orderdetails);
            while (true) {
                System.out.println("do you want to add a review for any item in this order? y/n");
                String review = s.nextLine();
                if (review.equals("y")) {
                    dos.writeUTF("addreview");
                    System.out.println("Enter the id of the item that you want to review:");
                    int id = s.nextInt();
                    dos.writeInt(id);
                    s.nextLine();
                    System.out.println("Enter your review : ");
                    dos.writeUTF(s.nextLine());
                } else {
                    dos.writeUTF("exit");
                    break;
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }

    public static void Exit() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println("exit");
        } catch (Exception var2) {
        }

    }

    public static void Exitnew() {
        try {
            OutputStream sout = ClientSocket.getOutputStream();
            PrintStream pout = new PrintStream(sout);
            pout.println(3);
        } catch (Exception var2) {
        }

    }

    static {
        s = new Scanner(System.in);
        ClientSocket = null;
    }
}
