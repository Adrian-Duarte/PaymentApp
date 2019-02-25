package com.example.paymentapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paymentapp.R;
import com.example.paymentapp.models.Bank;
import com.example.paymentapp.utils.GlideUtils;

import java.util.List;

public class BankRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Interfaces
    public interface OnItemClickListener {
        void onItemClick(Bank bank);
    }

    // Constants
    private final int BODY_VIEW = 0;
    private final int EMPTY_VIEW = 2;

    // Attributes
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<Bank> banks;

    // Constructors
    public BankRecyclerViewAdapter(Context context, List<Bank> banks) {
        this.context = context;
        this.banks = banks;
        this.onItemClickListener = (OnItemClickListener) context;
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
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
                return new BodyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            // Get current item
            Bank bank = banks.get(position);

            // Update UI
            BodyViewHolder bodyViewHolder = ((BodyViewHolder) holder);
            GlideUtils.load(context, bank.getSecureThumbnail(), bodyViewHolder.ivSecureThumbnail);
            bodyViewHolder.tvName.setText(bank.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (banks == null) {
            return 0;
        }
        if (banks.size() == 0) {
            return 1;
        }
        return banks.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (banks.size() == 0) {
            return EMPTY_VIEW;
        }
        return BODY_VIEW;
    }

    // Inner classes
    class BodyViewHolder extends RecyclerView.ViewHolder {

        // Attributes
        ImageView ivSecureThumbnail;
        TextView tvName;

        // Constructor
        BodyViewHolder(View bodyView) {
            super(bodyView);
            ivSecureThumbnail = bodyView.findViewById(R.id.iv_secure_thumbnail);
            tvName = bodyView.findViewById(R.id.tv_name);
            bodyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(banks.get(getAdapterPosition()));
                }
            });
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

}