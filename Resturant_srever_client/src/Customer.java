import java.io.Serializable;
import java.util.ArrayList;
/*
 The purpose of this class is to store the information of the users (clients).
 using this class we can add an order through the addOrder Method
 and print All orders
 and get an order by its code
*/
public class Customer implements Serializable {
    String username;
    String password;
    ArrayList<Order> orders;

    Customer(){}
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        orders = new ArrayList<Order>();

    }

    public void addOrder(Order x) {
        orders.add(x);
    }

    public String printAllOrders() {
        String s = "";
        for (int i = 0; i < orders.size(); i++)
            s += orders.get(i).code + " " + orders.get(i).getDateCreated() + "\n";
        return s;
    }

    public Order getOrder(int code) {
        Order o = null;
        for (int i = 0; i < orders.size(); i++) {
            if (code == orders.get(i).code) {
                o = orders.get(i);
            }
        }

        return o;
    }
    public String getPassword(){
        return password;
    }

}
