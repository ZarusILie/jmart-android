package LazaruslieJmartKD.jmart_android;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import LazaruslieJmartKD.jmart_android.model.Product;

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
        View view = Inflater_recycler.inflate(R.layout.activity_my_recycler_view_adapter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product productName = Data_recycler.get(position);
        holder.myTextView.setText(productName.toString());
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
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.productName_TV);
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


    void setClickListener(ItemClickListener itemClickListener) {
        this.ClickListener_recycler = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}