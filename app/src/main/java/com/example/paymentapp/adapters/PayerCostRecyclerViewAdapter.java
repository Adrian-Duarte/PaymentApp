package com.example.paymentapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paymentapp.R;
import com.example.paymentapp.models.PayerCost;

import java.util.List;

public class PayerCostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Interfaces
    public interface OnItemClickListener {
        void onItemClick(PayerCost payerCost);
    }

    // Constants
    private final int BODY_VIEW = 0;
    private final int EMPTY_VIEW = 2;

    // Attributes
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<PayerCost> payerCosts;

    // Constructors
    public PayerCostRecyclerViewAdapter(Context context, List<PayerCost> payerCosts) {
        this.context = context;
        this.payerCosts = payerCosts;
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
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payer_cost, parent, false);
                return new BodyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            // Get current item
            PayerCost payerCost = payerCosts.get(position);

            // Update UI
            BodyViewHolder bodyViewHolder = ((BodyViewHolder) holder);
            bodyViewHolder.tvRecommendedMessage.setText(payerCost.getRecommendedMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (payerCosts == null) {
            return 0;
        }
        if (payerCosts.size() == 0) {
            return 1;
        }
        return payerCosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (payerCosts.size() == 0) {
            return EMPTY_VIEW;
        }
        return BODY_VIEW;
    }

    // Inner classes
    class BodyViewHolder extends RecyclerView.ViewHolder {

        // Attributes
        TextView tvRecommendedMessage;

        // Constructor
        BodyViewHolder(View bodyView) {
            super(bodyView);
            tvRecommendedMessage = bodyView.findViewById(R.id.tv_recommended_message);
            bodyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(payerCosts.get(getAdapterPosition()));
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