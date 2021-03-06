package hu.autsoft.pppttl.ineedit.requestdetails;

import hu.autsoft.pppttl.ineedit.model.Comment;
import hu.autsoft.pppttl.ineedit.model.Request;

/**
 * Created by pppttl on 2018. 03. 12..
 */

public interface RequestDetailsContract {
    /**
     * Created by pppttl on 2018. 03. 05..
     */

    interface RequestDetailsInteractor {

        Request getRequest();

        void updateRequest(Request request);

        String getUserEmail();

        void sendComment(Comment comment);

        void setPresenter(RequestDetailsPresenter presenter);

        boolean isCurrentUserAdmin();
    }

    interface RequestDetailsPresenter {

        Request getRequest();

        void updateUI();

        void updateRequest(Request request);

        String getUserEmail();

        void sendComment(Comment comment);

        void closeUI();

        String getSelectedRequestId();

        boolean isCurrentUserAdmin();
    }

    interface RequestDetailsView {

        void updateUI();

        void closeUI();

        String getSelectedRequestId();
    }
}
