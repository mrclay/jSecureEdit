/*
 * App.java
 */

package org.mrclay.SecureEdit;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import java.io.*;
import org.mrclay.crypto.PBE;

/**
 * The main class of the application.
 */
public class App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new EditorView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of OnLockerApp
     */
    public static App getApplication() {
        return Application.getInstance(App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(App.class, args);
    }

    public static final String ENCRYPTED_TOKEN = "SecureEdit :: http://www.mrclay.org/ :: ";

    public File openedFile = new File("");

    public PBE cipher = null;

    public boolean cipherIsReady() {
        return (cipher != null);
    }

    public void updateCipher(char[] passphrase) throws Exception {
        try {
            cipher = new PBE(passphrase);
        } catch (Exception e) {
            throw new Exception("Failed to create cipher: " + e.getMessage());
        }
    }
}
