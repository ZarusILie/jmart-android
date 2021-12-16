package LazaruslieJmartKD.jmart_android.model;

/**
 * class Account
 *
 * @author (Lazaruslie Karsono)
 */

public class Account extends Serializable {
    public double balance;
    public String email;
    public String name;
    public String password;
    public Store store;

    public Account(String name, String email, String password, double balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }
}
