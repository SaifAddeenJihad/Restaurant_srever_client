import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    public int code;
    private ArrayList<Item> items = new ArrayList();
    private Date dateCreated = new Date();
    public Order(){}
    public Order(int code) {
        this.code = code;
    }

    public Order(int code, Order order) {
        this.code = code;
        for (int i = 0; i < items.size(); i++)
            items.add(order.items.get(i));
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public double orderValue() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++)
            sum += items.get(i).getPriceWithTax();
        return sum;
    }

    public String showalliteams() {
        String s = "";
        for (int i = 0; i < items.size(); i++) {
            s += items.get(i).toString() + "\n";
        }
        return s;
    }

    public Item getitem(int code) {
        Item item = null;
        for (int i = 0; i < items.size(); i++) {
            if (code == items.get(i).id)
                item = items.get(i);
        }
        return item;
    }

    public void removeitem(int code) {
        for (int i = 0; i < items.size(); i++) {
            if (code == items.get(i).id) {
                items.remove(i);
                break;
            }
        }

    }

    public double getprice() {
        double price = 0;
        for (int i = 0; i < items.size(); i++) {
            price += items.get(i).getPriceWithTax();
        }
        return price;
    }


}
