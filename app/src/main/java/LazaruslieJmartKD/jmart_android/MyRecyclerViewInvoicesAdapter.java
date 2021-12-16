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
 * class MyRecyclerViewInvoiceAdapter
 *
 * @author (Lazaruslie Karsono)
 */

public class MyRecyclerViewInvoicesAdapter extends RecyclerView.Adapter<MyRecyclerViewInvoicesAdapter.ViewHolder> {

    private static final Gson gson = new Gson();
    private List<Payment> Data_Recycler;
    private List<Product> Invoice_Products;
    private LayoutInflater Inflater_Recycler;
    private ItemClickListener Click_Listener;

    MyRecyclerViewInvoicesAdapter(Context context, List<Payment> data, List<Product> invoiceProducts) {
        this.Inflater_Recycler = LayoutInflater.from(context);
        this.Data_Recycler = data;
        this.Invoice_Products = invoiceProducts;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater_Recycler.inflate(R.layout.my_recycler_view_invoices_adapter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Payment paymentName = Data_Recycler.get(position);
        Product productName = Invoice_Products.get(position);
        if(productName.toString().length() >= 38){
            holder.Invoice_Name.setTextSize(12.0f);
            holder.Invoice_Name.setMaxEms(10);
        }
        else{ }
        holder.Invoice_Name.setText(productName.toString());
        holder.Invoice_Status.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.Invoice_Amount.setText("x"+String.valueOf(paymentName.productCount));
        holder.Invoice_TotalPrice.setText(String.valueOf(Math.round(productName.price * paymentName.productCount * 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.Invoice_ShipmentPlan.setText(("INSTANT"));
                break;
            case 1:
                holder.Invoice_ShipmentPlan.setText(("SAME_DAY"));
                break;
            case 2:
                holder.Invoice_ShipmentPlan.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.Invoice_ShipmentPlan.setText(("KARGO"));
                break;
            default:
                holder.Invoice_ShipmentPlan.setText(("REGULER"));
                break;
        }
        holder.Product_ID.setText("Product ID: "+paymentName.productId);
        holder.Invoice_BuyerID.setText("Buyer ID: " + paymentName.buyerId);
        holder.Invoice_Address.setText(paymentName.shipment.address);

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

    public void refreshProducts(List<Product> invoiceProducts) {
        this.Invoice_Products = invoiceProducts;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Invoice_Name;
        TextView Invoice_Status;
        TextView Invoice_Amount;
        TextView Invoice_TotalPrice;
        TextView Invoice_ShipmentPlan;
        TextView Product_ID;
        TextView Invoice_BuyerID;
        TextView Invoice_Address;
        Button Accept_Btn;
        Button Cancel_Btn;

        ViewHolder(View itemView) {
            super(itemView);
            Invoice_Name = itemView.findViewById(R.id.InvoiceName);
            Invoice_Status = itemView.findViewById(R.id.InvoiceStatus);
            Invoice_Amount = itemView.findViewById(R.id.InvoiceAmount);
            Invoice_TotalPrice = itemView.findViewById(R.id.InvoiceTotalPrice);
            Invoice_ShipmentPlan = itemView.findViewById(R.id.InvoiceShipmentPlan);
            Product_ID = itemView.findViewById(R.id.ProductId);
            Invoice_BuyerID = itemView.findViewById(R.id.InvoiceBuyerId);
            Invoice_Address = itemView.findViewById(R.id.InvoiceAddress);
            Accept_Btn = itemView.findViewById(R.id.AccBtn);
            Cancel_Btn = itemView.findViewById(R.id.InvoiceCancelBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (Click_Listener != null) Click_Listener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return Data_Recycler.get(id).toString();
    }

    int getClickedItemId(int id) {
        return Data_Recycler.get(id).id;
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.Click_Listener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

