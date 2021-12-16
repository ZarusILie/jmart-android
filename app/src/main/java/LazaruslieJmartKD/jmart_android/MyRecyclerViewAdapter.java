package LazaruslieJmartKD.jmart_android;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import LazaruslieJmartKD.jmart_android.model.Product;

/**
 * class MyRecyclerViewAdapter
 *
 * @author (Lazaruslie Karsono)
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Product> Data_recycler;
    private LayoutInflater Inflater_recycler;
    private ItemClickListener ClickListener_recycler;

    MyRecyclerViewAdapter(Context context, List<Product> data) {
        this.Inflater_recycler = LayoutInflater.from(context);
        this.Data_recycler = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater_recycler.inflate(R.layout.my_recycler_view_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product productName = Data_recycler.get(position);
        holder.Product_Name.setText(productName.toString());
        holder.Product_Price.setText(String.valueOf(Math.round(productName.price * 100.00) / 100.00));
        holder.Product_Category.setText(productName.category.toString());
        if(productName.toString().length() >= 36){
            holder.Product_Name.setTextSize(18.0f);
            holder.Product_Name.setMaxEms(14);
        }
    }


    @Override
    public int getItemCount() {
        return Data_recycler.size();
    }

    public void refresh(List<Product> data) {
        this.Data_recycler = data;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Product_Name;
        TextView Product_Price;
        TextView Product_Category;

        ViewHolder(View itemView) {
            super(itemView);
            Product_Name = itemView.findViewById(R.id.ProductName_TV);
            Product_Price = itemView.findViewById(R.id.ProductPrice_TV);
            Product_Category = itemView.findViewById(R.id.ProductCategory_TV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (ClickListener_recycler != null) ClickListener_recycler.onItemClick(view, getAdapterPosition());
        }
    }


    String getItem(int id) {
        return Data_recycler.get(id).toString();
    }

    int getClickedItemId(int id) {
        return Data_recycler.get(id).id;
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.ClickListener_recycler = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}