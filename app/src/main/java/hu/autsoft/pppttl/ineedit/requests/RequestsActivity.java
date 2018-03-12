package hu.autsoft.pppttl.ineedit.requests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import hu.autsoft.pppttl.ineedit.R;
import hu.autsoft.pppttl.ineedit.model.Request;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.RequestCreateOrEditDialog;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.SaveRequestCallbackListener;
import hu.autsoft.pppttl.ineedit.requestdetails.RequestDetailsActivity;

/**
 * Created by pppttl on 2018. 02. 26..
 */

public class RequestsActivity extends AppCompatActivity implements RequestsView, SaveRequestCallbackListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewRequests)
    RecyclerView recyclerView;

    @Inject
    RequestRecyclerViewAdapter adapter;

    @Inject
    RequestsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        setupRecyclerView(recyclerView);
    }

    @OnClick(R.id.fabRequests)
    public void fabClick(View view) {
        RequestCreateOrEditDialog dialog = new RequestCreateOrEditDialog();
        dialog.show(view, getString(R.string.create_request), this, null);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Request> requests = new ArrayList<>(presenter.getRequests());
        adapter.updateRequests(requests);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void navigateToRequest(String requestID) {
        Intent intent = new Intent(this, RequestDetailsActivity.class);
        intent.putExtra(RequestDetailsActivity.REQUEST_ID, requestID);
        startActivity(intent);
    }

    @Override
    public void onRequestDataChanged(List<Request> requests) {
        if (adapter != null) {
            adapter.updateRequests(requests);
        }
    }

    @Override
    public void onRequestSave(Request request) {
        presenter.saveRequest(request);
    }
}