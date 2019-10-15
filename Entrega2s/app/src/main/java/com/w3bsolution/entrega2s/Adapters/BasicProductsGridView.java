package com.w3bsolution.entrega2s.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.w3bsolution.entrega2s.Domain.Category;
import com.w3bsolution.entrega2s.Domain.Product;
import com.w3bsolution.entrega2s.R;

import java.util.List;

public class BasicProductsGridView extends BaseAdapter {
    private List<Product> productList;
    private List<Category> categoryList;
    private Context context;
    private boolean logged_user;
    View view;
    LayoutInflater layoutInflater;

    public BasicProductsGridView(List<Product> productList, List<Category> categoryList, Context context, boolean logged_user) {
        this.productList = productList;
        this.categoryList = categoryList;
        this.context = context;
        this.logged_user = logged_user;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_product_adapter, null);
            //getting fields
            ImageView imageView = view.findViewById(R.id.grid_column_image);
            ImageView grid_column_menu = view.findViewById(R.id.grid_column_menu);
            TextView title = view.findViewById(R.id.grid_column_title);
            TextView category = view.findViewById(R.id.grid_column_category);
            TextView price = view.findViewById(R.id.grid_column_price);
            if(!logged_user){
                grid_column_menu.setVisibility(View.GONE);
            }
            //setting fields
            title.setText(productList.get(position).getTitle());
            price.setText("$" + String.valueOf(productList.get(position).getPrice_to_show()));
            String cat = "";
            for (Category c : categoryList) {
                if (c.getCategory_id() == productList.get(position).getCategory_id()) {
                    cat = c.getCategory();
                    break;
                }
            }
            category.setText(cat);
            Picasso.with(context)
                    .load(productList.get(position).getProduct_image_one())
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
        return view;
    }
}
