package com.demo.demoapplication.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.demoapplication.R;
import com.demo.demoapplication.ui.models.SearchListModel;
import com.demo.demoapplication.ui.utils.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class AdapterListUserInfo extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<SearchListModel> items = new ArrayList<>();
    private List<SearchListModel> itemsFilter = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view,String name, String email, Integer obj, int position);
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListUserInfo(Context context, List<SearchListModel> items1, int animation_type) {
        this.items = items1;
        this.itemsFilter = items1;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,email;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            email = (TextView) v.findViewById(R.id.email);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            SearchListModel p =  items.get(position);
            view.name.setText(p.getName());
            view.email.setText(p.getEmail());
            Glide.with(ctx).load(p.getImage()).into(view.image);
            setAnimation(view.itemView, position);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view,p.getName(),p.getEmail(), items.get(position).getImage(), position);
                    }
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }

    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                items = (ArrayList<SearchListModel>) results.values; // has the filtered values
                notifyDataSetChanged(); // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the results of a filtering operation in values
                ArrayList<SearchListModel> FilteredArrList = new ArrayList<SearchListModel>();

                if (itemsFilter == null) {
                    itemsFilter = new ArrayList<SearchListModel>(items); // saves the original data in mOriginalValues
                }

/********
 *
 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
 * else does the Filtering and returns FilteredArrList(Filtered)
 *
 ********/
                if (constraint == null || constraint.length() == 0) {

// set the Original result to return
                    results.count = itemsFilter.size();
                    results.values = itemsFilter;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < itemsFilter.size(); i++) {
                        String data = itemsFilter.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new SearchListModel(itemsFilter.get(i).getName(),itemsFilter.get(i).getEmail(),itemsFilter.get(i).getImage()));
                        }
                    }
// set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}