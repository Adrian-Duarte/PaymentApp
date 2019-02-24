package com.example.paymentapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paymentapp.R;
import com.example.paymentapp.models.User;
import com.example.paymentapp.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    // Constants
    private final int BODY_VIEW = 0;
    private final int EMPTY_VIEW = 2;

    // Attributes
    private Context context;
    private List<User> fullUsers;
    private List<User> users;
    private ValueFilter valueFilter;

    // Constructors
    public UserRecyclerViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.fullUsers = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case EMPTY_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_recycler_view, parent, false);
                return new EmptyViewHolder(view);
            case BODY_VIEW:
            default:
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
                return new BodyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            // Get current item
            User user = users.get(position);

            // Update UI
            BodyViewHolder bodyViewHolder = ((BodyViewHolder) holder);
            GlideUtils.load(context, user.getAvatar(), bodyViewHolder.ivAvatar);
            bodyViewHolder.tvFullName.setText(user.getFullName());
        }
    }


    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        }
        if (users.size() == 0) {
            return 1;
        }
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (users.size() == 0) {
            return EMPTY_VIEW;
        }
        return BODY_VIEW;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    // Inner classes
    class BodyViewHolder extends RecyclerView.ViewHolder {

        // Attributes
        ImageView ivAvatar;
        TextView tvFullName;

        // Constructor
        BodyViewHolder(View bodyView) {
            super(bodyView);
            ivAvatar = bodyView.findViewById(R.id.iv_avatar);
            tvFullName = bodyView.findViewById(R.id.tv_full_name);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        // Attributes
        TextView tvEmpty;

        // Constructor
        EmptyViewHolder(View emptyView){
            super(emptyView);
            tvEmpty = emptyView.findViewById(R.id.tv_empty);
        }
    }

    class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<User> usersResult = new ArrayList<>();
                for (User user: fullUsers) {
                    if(user.getFullName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        usersResult.add(user);
                    }
                }
                results.count = usersResult.size();
                results.values = usersResult;
            } else {
                results.count = fullUsers.size();
                results.values = fullUsers;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users = (List<User>) results.values;
            notifyDataSetChanged();
        }
    }

}