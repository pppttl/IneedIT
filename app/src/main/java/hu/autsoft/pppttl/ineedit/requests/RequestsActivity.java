package hu.autsoft.pppttl.ineedit.requests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import hu.autsoft.pppttl.ineedit.R;
import hu.autsoft.pppttl.ineedit.login.LoginActivity;
import hu.autsoft.pppttl.ineedit.model.Request;
import hu.autsoft.pppttl.ineedit.profile.ProfileActivity;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.RequestCreateOrEditDialog;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.SaveRequestCallbackListener;
import hu.autsoft.pppttl.ineedit.requestdetails.RequestDetailsActivity;

/**
 * Created by pppttl on 2018. 02. 26..
 */

public class RequestsActivity extends AppCompatActivity implements RequestsContract.RequestsView, SaveRequestCallbackListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewRequests)
    RecyclerView recyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Inject
    RequestRecyclerViewAdapter adapter;

    @Inject
    RequestsContract.RequestsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        ButterKnife.bind(this);

        setupRecyclerView(recyclerView);
        setupDrawerLayout();
    }

    @OnClick(R.id.fabRequests)
    public void fabClick(View view) {
        RequestCreateOrEditDialog dialog = new RequestCreateOrEditDialog();
        dialog.show(view, getString(R.string.create_request), this, null);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Request> requests = new ArrayList<>(presenter.getRequests());
        adapter.updateRequests(requests);
        recyclerView.setAdapter(adapter);
    }

    private void setupDrawerLayout() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView email = header.findViewById(R.id.currEmail);
        TextView username = header.findViewById(R.id.currUsername);
        email.setText(presenter.getCurrentUserEmail());
        String usernameString = presenter.getCurrentUsername();
        username.setText(usernameString != null ? usernameString : "No displayname given");

        navigationView.setCheckedItem(R.id.nav_all_requests);

        header.findViewById(R.id.profilePicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });
        setTitle(getString(R.string.activity_all_requests));
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID, presenter.getCurrentUID());
        startActivity(intent);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            presenter.logout();
            Intent intent = new Intent(RequestsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_all_requests) {
            setTitle(getString(R.string.activity_all_requests));
            adapter.filterRequests(null);
        } else if (id == R.id.nav_pending_requests) {
            setTitle(getString(R.string.activity_pending_requests));
            adapter.filterRequests(Request.Status.PENDING);
        } else if (id == R.id.nav_accepted_requests) {
            setTitle(getString(R.string.activity_accepted_requests));
            adapter.filterRequests(Request.Status.ACCEPTED);
        } else if (id == R.id.nav_denied_requests) {
            setTitle(getString(R.string.activity_denied_requests));
            adapter.filterRequests(Request.Status.DENIED);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
