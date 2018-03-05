package hu.autsoft.pppttl.ineedit.RequestDetails;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hu.autsoft.pppttl.ineedit.Model.Request;
import hu.autsoft.pppttl.ineedit.Requests.RequestsInteractorImpl;

/**
 * Created by pppttl on 2018. 03. 05..
 */

public class RequestDetailsInteractorImpl implements RequestDetailsInteractor {
    private Request request = new Request();
    private RequestDetailsPresenter presenter;

    public RequestDetailsInteractorImpl(final RequestDetailsPresenter presenter, String requestID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child(RequestsInteractorImpl.CHILD_NAME).child(requestID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);
                presenter.updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
