package hu.autsoft.pppttl.ineedit.requestdetails;

import hu.autsoft.pppttl.ineedit.model.Comment;
import hu.autsoft.pppttl.ineedit.model.Request;
import hu.autsoft.pppttl.ineedit.mvp.BasePresenter;

/**
 * Created by pppttl on 2018. 03. 05..
 */

public class RequestDetailsPresenterImpl extends BasePresenter<RequestDetailsContract.RequestDetailsView, RequestDetailsContract.RequestDetailsInteractor>
        implements RequestDetailsContract.RequestDetailsPresenter {

    public RequestDetailsPresenterImpl(RequestDetailsContract.RequestDetailsView view, String requestID) {
        attachView(view);
        attachInteractor(new RequestDetailsInteractorImpl(this, requestID));
    }

    public RequestDetailsPresenterImpl(RequestDetailsContract.RequestDetailsView view, RequestDetailsContract.RequestDetailsInteractor interactor) {
        //Only for testing
        this.view = view;
        this.interactor = interactor;
    }

    public Request getRequest() {
        return interactor.getRequest();
    }

    @Override
    public void updateUI() {
        view.updateUI();
    }

    @Override
    public void updateRequest(Request request) {
        interactor.updateRequest(request);
    }

    @Override
    public String getUserID() {
        return interactor.getUserID();
    }

    @Override
    public void sendComment(Comment comment) {
        interactor.sendComment(comment);
    }

    @Override
    public void closeUI() {
        view.closeUI();
    }
}
