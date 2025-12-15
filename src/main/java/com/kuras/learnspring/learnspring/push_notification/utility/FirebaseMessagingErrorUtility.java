package com.kuras.learnspring.learnspring.push_notification.utility;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;

public final class FirebaseMessagingErrorUtility {
    public static MessagingErrorCode getErrorCode(Exception ex) {
        if (ex instanceof FirebaseMessagingException fme) {
            return fme.getMessagingErrorCode();
        }
        return null;
    }

    public static boolean isDeadTokenError(MessagingErrorCode code) {
        return code == MessagingErrorCode.UNREGISTERED
                || code == MessagingErrorCode.INVALID_ARGUMENT
                || code == MessagingErrorCode.SENDER_ID_MISMATCH; // bazı invalid’lerde de token çöp olabilir
    }

    public static boolean isTemporaryError(MessagingErrorCode code) {
        return code == MessagingErrorCode.UNAVAILABLE
                || code == MessagingErrorCode.INTERNAL
                || code == MessagingErrorCode.THIRD_PARTY_AUTH_ERROR
                || code == MessagingErrorCode.QUOTA_EXCEEDED;
    }
}
