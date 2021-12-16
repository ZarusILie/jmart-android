package LazaruslieJmartKD.jmart_android.model;

import java.util.Date;

/**
 * abstract class Invoice
 *
 * @author (Lazaruslie Karsono)
 */

public class Invoice extends Serializable {
    public final Date date;
    public int buyerId;
    public int productId;
    public int complaintId;
    public Rating rating;

    public enum Status {
        WAITING_CONFIRMATION, CANCELLED, ON_PROGRESS,
        ON_DELIVERY, COMPLAINT, FINISHED, FAILED, DELIVERED
    }

    public enum Rating {
        NONE, BAD, NEUTRAL, GOOD
    }

    class Record {
        public Date date;
        public String message;
        public Status status;
    }

    protected Invoice(int buyerId, int productId) {
        this.buyerId = buyerId;
        this.productId = productId;
        this.date = new Date();
        this.rating = Rating.NONE;
        this.complaintId = -1;
    }
}
