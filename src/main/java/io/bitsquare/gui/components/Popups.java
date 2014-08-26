/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.gui.components;

import io.bitsquare.BitSquare;

import com.google.bitcoin.store.BlockStoreException;

import com.google.common.base.Throwables;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class Popups {

    // Information
    public static void openInformationPopup(String title, String message) {
        openInformationPopup(title, message, null);
    }

    public static void openInformationPopup(String title, String message, String masthead) {
        Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .showInformation();
    }

    // Confirm
    public static Action openConfirmPopup(String title, String message) {
        return openConfirmPopup(title, message, null);
    }

    public static Action openConfirmPopup(String title, String message, String masthead) {
        List<Action> actions = new ArrayList<>();
        actions.add(Dialog.Actions.OK);
        actions.add(Dialog.Actions.CANCEL);
        return openConfirmPopup(title, message, masthead, actions);
    }

    public static Action openConfirmPopup(String title, String message, String masthead, List<Action> actions) {
        return Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .actions(actions)
                .showConfirm();
    }

    // Warning
    public static void openWarningPopup(String message) {
        openWarningPopup("Warning", message, null);
    }

    public static void openWarningPopup(String title, String message) {
        openWarningPopup(title, message, null);
    }

    public static void openWarningPopup(String title, String message, String masthead) {
        Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .showWarning();
    }

    // Error
    public static Action openErrorPopup(String message) {
        return openErrorPopup("Error", message);
    }

    public static Action openErrorPopup(String title, String message) {
        return openErrorPopup(title, message, null);
    }

    public static Action openErrorPopup(String title, String message, String masthead) {
        return Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .showError();
    }

    // Exception
    public static Action openExceptionPopup(Throwable throwable) {
        return openExceptionPopup(throwable, "Exception", "That should not have happened...");
    }

    public static Action openExceptionPopup(Throwable throwable, String title, String message) {
        return openExceptionPopup(throwable, title, message, null);
    }

    public static Action openExceptionPopup(Throwable throwable, String title, String message, String masthead) {
        return Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .showException(throwable);
    }

    // Support handling of uncaught exception from any thread (also non gui thread)
    public static void handleUncaughtExceptions(Throwable throwable) {
        // while dev
        throwable.printStackTrace();

        Runnable runnable = () -> {
            if (Throwables.getRootCause(throwable) instanceof BlockStoreException) {
                Action response = Popups.openErrorPopup("Application already running",
                        "This application is already running and cannot be started twice.", "");
                if (response == Dialog.Actions.OK) Platform.exit();
            }
            else {
                Action response = Popups.openExceptionPopup(throwable, "Exception", "",
                        "A critical error has occurred.\nPlease copy the exception details and send a bug report to " +
                                "bugs@bitsquare.io.");
                if (response == Dialog.Actions.OK) Platform.exit();
            }
        };

        if (Platform.isFxApplicationThread()) runnable.run();
        else Platform.runLater(runnable);
    }

    // custom
    public static void openInsufficientMoneyPopup() {
        openWarningPopup("Not enough money available", "There is not enough money available. Please pay in first to " +
                "your wallet.", null);
    }

    public static Action openRegistrationMissingPopup(String title, String message, String masthead,
                                                      List<Dialogs.CommandLink> commandLinks, int selectedIndex) {
        return Dialogs.create()
                .owner(BitSquare.getPrimaryStage())
                .title(title)
                .message(message)
                .masthead(masthead)
                .showCommandLinks(commandLinks.get(selectedIndex), commandLinks);
    }
}