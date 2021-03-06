package hu.autsoft.pppttl.ineedit.requestdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import hu.autsoft.pppttl.ineedit.R;
import hu.autsoft.pppttl.ineedit.model.Comment;
import hu.autsoft.pppttl.ineedit.model.Request;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.RequestCreateOrEditDialog;
import hu.autsoft.pppttl.ineedit.requestcreateoredit.SaveRequestCallbackListener;

public class RequestDetailsActivity extends AppCompatActivity implements RequestDetailsContract.RequestDetailsView, SaveRequestCallbackListener, AdapterView.OnItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detailsURL)
    TextView urlView;
    @BindView(R.id.detailsPrice)
    TextView priceView;
    @BindView(R.id.detailsStatus)
    TextView statusView;
    @BindView(R.id.edittext_chatbox)
    EditText chatbox;
    @BindView(R.id.button_chatbox_send)
    Button sendButton;
    @BindView(R.id.reyclerview_message_list)
    RecyclerView recyclerView;
    @BindView(R.id.adminStatusSpinner)
    Spinner adminStatusSpinner;
    @BindView(R.id.issuedByLayout)
    LinearLayout issuedBylayout;
    @BindView(R.id.detailsIssuer)
    TextView detailsIssuer;

    @Inject
    RequestDetailsContract.RequestDetailsPresenter presenter;
    @Inject
    CommentRecyclerViewAdapter adapter;

    public static final String REQUEST_ID = "request_id";
    private static final String REQUEST_NAME = "request_name";

    String requestID;
    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestID = getIntent().getStringExtra(REQUEST_ID);
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        ButterKnife.bind(this);

        adminStatusSpinner.setOnItemSelectedListener(this);
        List<Request.Status> statuses = new ArrayList<>(Arrays.asList(Request.Status.values()));
        ArrayAdapter<Request.Status> statusArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminStatusSpinner.setAdapter(statusArrayAdapter);

        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupRecyclerView(recyclerView);
    }

    private void updateAdmin() {
        if (presenter.isCurrentUserAdmin()) {
            adminStatusSpinner.setVisibility(View.VISIBLE);
            adminStatusSpinner.setSelection(request.getStatusID());
            statusView.setVisibility(View.GONE);
            issuedBylayout.setVisibility(View.VISIBLE);
            detailsIssuer.setText(request.getIssuerEmail());
        } else {
            adminStatusSpinner.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
            issuedBylayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @OnClick(R.id.fabRequestDetails)
    public void fabClick(View view) {
        RequestCreateOrEditDialog dialog = new RequestCreateOrEditDialog();
        dialog.show(view, getString(R.string.edit_request), this, request);
    }

    @OnClick(R.id.button_chatbox_send)
    public void sendClick() {
        String email = presenter.getUserEmail();
        String message = chatbox.getText().toString();
        Comment comment = new Comment(email, message);

        presenter.sendComment(comment);
        chatbox.setText("");
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> comments = request == null ? new ArrayList<Comment>() : new ArrayList<>(request.getComments());
        adapter.updateComments(this.getApplicationContext(), comments, presenter.getUserEmail());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI() {
        request = presenter.getRequest();
        if (request == null) {
            finish();
        } else {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(request.getName());

            urlView.setText(request.getLink().length() > 0 ? request.getLink() : getString(R.string.n_a));
            priceView.setText(request.getPrice() > 0 ? Integer.toString(request.getPrice()) : getString(R.string.n_a));
            statusView.setText(request.getStatus().toString());
            adapter.updateComments(this.getBaseContext(), request.getComments(), presenter.getUserEmail());

            updateAdmin();
        }

    }

    @Override
    public void closeUI() {
        finish();
    }

    @Override
    public String getSelectedRequestId() {
        return requestID;
    }

    @Override
    public void onRequestSave(Request request) {
        presenter.updateRequest(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Note: Checking whether the status is update is crucial, because saving the request invokes this function, too.
        switch (i) {
            case 0:
                if (!request.getStatus().equals(Request.Status.PENDING)) {
                    request.setStatus(Request.Status.PENDING);
                    onRequestSave(request);
                }
                break;
            case 1:
                if (!request.getStatus().equals(Request.Status.ACCEPTED)) {
                    request.setStatus(Request.Status.ACCEPTED);
                    onRequestSave(request);
                }
                break;
            case 2:
                if (!request.getStatus().equals(Request.Status.ACCEPTED)) {
                    request.setStatus(Request.Status.ACCEPTED);
                    onRequestSave(request);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
