package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.paymentapp.R;
import com.example.paymentapp.adapters.UserRecyclerViewAdapter;
import com.example.paymentapp.api.APIClient;
import com.example.paymentapp.api.APIInterface;
import com.example.paymentapp.models.User;
import com.example.paymentapp.models.Payment;
import com.example.paymentapp.models.data.UserData;
import com.example.paymentapp.utils.CustomProgressBar;
import com.example.paymentapp.utils.TinyDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserSearchActivity extends BaseSearchActivity  implements UserRecyclerViewAdapter.OnItemClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ListUserSearchActivity.class);
    }

    // Constants
    private static final int PER_PAGE = 12;

    // Attributes
    private APIInterface apiInterface;
    private CustomProgressBar customProgressBar;
    private List<User> users;
    private RecyclerView rvUsers;
    private UserRecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout srlUsers;

    // Override methods and callbacks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setListeners();
        getUsers();
    }

    @Override
    public boolean addBackButton() {
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_list_user;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.all_users;
    }

    @Override
    public void onClickCloseButton() {
        recyclerViewAdapter.getFilter().filter("");
    }

    @Override
    public void onQueryTextResult(String query) {
        recyclerViewAdapter.getFilter().filter(query);
    }

    @Override
    public void onItemClick(User user) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();
        payment.setUser(user);
        tinyDB.putPayment(payment);

        startActivity(AmountActivity.getStartIntent(this));
    }

    // Private methods
    private void getUsers() {
        Call<UserData> call = apiInterface.getUsers(PER_PAGE);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                customProgressBar.hide();
                srlUsers.setRefreshing(false);

                // Check if data is correct
                if(!response.isSuccessful()) {
                    showGenericError();
                    return;
                }
                UserData userData = response.body();
                if(userData==null) {
                    showGenericError();
                    return;
                }
                users = userData.getUsers();
                if(users==null) {
                    showGenericError();
                    return;
                }
                // Success
                updateUsers();
            }
            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                call.cancel();
                customProgressBar.hide();
                srlUsers.setRefreshing(false);
                showGenericError();
            }
        });
    }

    private void initialize() {
        apiInterface = APIClient.getMockClient().create(APIInterface.class);
        customProgressBar = new CustomProgressBar(this);
        customProgressBar.show();
        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvUsers.setItemAnimator(new DefaultItemAnimator());
        srlUsers = findViewById(R.id.srl_users);
    }

    private void setListeners() {
        srlUsers.setOnRefreshListener(onRefreshListener);
    }

    private void updateUsers() {
        recyclerViewAdapter = new UserRecyclerViewAdapter(this, users);
        rvUsers.setAdapter(recyclerViewAdapter);
    }

    // Private methods
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getUsers();
        }
    };

}
