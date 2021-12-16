package LazaruslieJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import LazaruslieJmartKD.jmart_android.model.Payment;
import LazaruslieJmartKD.jmart_android.model.Product;

/**
 * class MyRecyclerViewTransactionAdapter
 *
 * @author (Lazaruslie Karsono)
 */

public class MyRecyclerViewTransactionsAdapter extends RecyclerView.Adapter<MyRecyclerViewTransactionsAdapter.ViewHolder> {
    private static final Gson gson = new Gson();
    private List<Payment> Data_Recycler;
    private List<Product> Transaction_Products;
    private LayoutInflater Inflater_Recycler;
    private ItemClickListener Click_Listener;

    MyRecyclerViewTransactionsAdapter(Context context, List<Payment> data, List<Product> transactionProducts) {
        this.Inflater_Recycler = LayoutInflater.from(context);
        this.Data_Recycler = data;
        this.Transaction_Products = transactionProducts;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater_Recycler.inflate(R.layout.my_recycler_view_transactions_adapter, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Payment paymentName = Data_Recycler.get(position);
        Product productName = Transaction_Products.get(position);
        if(productName.toString().length() >= 38){
            holder.Invoice_Name.setTextSize(12.0f);
            holder.Invoice_Name.setMaxEms(10);
        }
        holder.Invoice_Name.setText(productName.toString());
        holder.Invoice_Status.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.Amount_Transaction.setText("x"+String.valueOf(paymentName.productCount));
        holder.TotalPrice_Transaction.setText(String.valueOf(Math.round(productName.price * paymentName.productCount* 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.ShipmentPlan_Transaction.setText(("INSTANT"));
                break;
            case 1:
                holder.ShipmentPlan_Transaction.setText(("SAME_DAY"));
                break;
            case 2:
                holder.ShipmentPlan_Transaction.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.ShipmentPlan_Transaction.setText(("KARGO"));
                break;
            default:
                holder.ShipmentPlan_Transaction.setText(("REGULER"));
                break;
        }
        holder.ProductId_Transaction.setText("Product ID: "+ paymentName.productId);
        holder.Invoice_Transaction.setText("Seller ID: " + productName.accountId);
        holder.Address_Transaction.setText(paymentName.shipment.address);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return Data_Recycler.size();
    }

    //Refresh the list by notify if list data has been updated
    public void refresh(List<Payment> data) {
        this.Data_Recycler = data;
        notifyDataSetChanged();
    }

    public void refreshProducts(List<Product> transactionProducts) {
        this.Transaction_Products = transactionProducts;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Invoice_Name;
        TextView Invoice_Status;
        TextView Amount_Transaction;
        TextView TotalPrice_Transaction;
        TextView ShipmentPlan_Transaction;
        TextView ProductId_Transaction;
        TextView Invoice_Transaction;
        TextView Address_Transaction;
        Button Transaction_CancelBtn;

        ViewHolder(View itemView) {
            super(itemView);
            Invoice_Name = itemView.findViewById(R.id.NameTransaction);
            Invoice_Status = itemView.findViewById(R.id.StatusTransaction);
            Amount_Transaction = itemView.findViewById(R.id.AmountTransaction);
            TotalPrice_Transaction = itemView.findViewById(R.id.TotalPriceTransaction);
            ShipmentPlan_Transaction = itemView.findViewById(R.id.ShipmentPlanTransaction);
            ProductId_Transaction = itemView.findViewById(R.id.ProductIdTransaction);
            Invoice_Transaction = itemView.findViewById(R.id.InvoiceStore);
            Address_Transaction = itemView.findViewById(R.id.AddressTransaction);
            Transaction_CancelBtn = itemView.findViewById(R.id.TransactionCancelBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (Click_Listener != null) Click_Listener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return Data_Recycler.get(id).toString();
    }
    int getClickedItemId(int id){ return Data_Recycler.get(id).id;}

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.Click_Listener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}