package com.example.madclassproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.LinkedList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    Context ct;
    LayoutInflater mInflater;
    LinkedList<Product> mProductList;

    public ProductListAdapter(Context context, LinkedList<Product> ProductList){
        mInflater = LayoutInflater.from(context);
        this.mProductList = ProductList;
        ct=context;
    }

    /*@Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create view from layout
        View mItemView = mInflater.inflate(R.layout.product_layout, parent, false);
        return new ProductListViewHolder(mItemView, this);
    }*/

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        public TextView tv_product_title, tv_product_price, tv_product_qty, tv_product_id;
        public ImageView btn_inc, btn_dec;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_product_id = itemView.findViewById(R.id.tv_product_id);
            tv_product_title = itemView.findViewById(R.id.tv_product_title);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            tv_product_qty = itemView.findViewById(R.id.tv_product_qty);
            btn_inc = itemView.findViewById(R.id.btn_inc);
            btn_dec = itemView.findViewById(R.id.btn_dec);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.product_layout,parent,false);
        return new MyViewHolder(mItemView);

    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Product mCurrent = mProductList.get(position);
        holder.tv_product_id.setText(String.valueOf(mCurrent.getId()));
        holder.tv_product_title.setText(mCurrent.getTitle());
        holder.tv_product_price.setText(String.format("%.2f", mCurrent.getPrice()));
        holder.tv_product_qty.setText("0");
        //holder.tv_product_qty.setText(String.valueOf(mCurrent.getQty()));

        if (ct.getClass().getSimpleName().equals("PaymentActivity")) {
            holder.btn_inc.setVisibility(View.GONE);
            holder.btn_dec.setVisibility(View.GONE);
            holder.tv_product_qty.setVisibility(View.GONE);
        }

        holder.btn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tv_product_qty.getText().toString());
                qty = qty + 1;
                holder.tv_product_qty.setText(Integer.toString(qty));
                float productPrice = Float.parseFloat(holder.tv_product_price.getText().toString());
                int itemID = Integer.parseInt(holder.tv_product_id.getText().toString());
                OrderActivity.addItem(productPrice, qty, itemID);
            }
        });
        holder.btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.tv_product_qty.getText().toString());
                if (qty > 0) {
                    qty = qty - 1;
                    holder.tv_product_qty.setText(Integer.toString(qty));
                    float productPrice = Float.parseFloat(holder.tv_product_price.getText().toString());
                    int itemID = Integer.parseInt(holder.tv_product_id.getText().toString());
                    OrderActivity.subItem(productPrice, qty, itemID);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();

    }

}