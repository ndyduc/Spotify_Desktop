package org.music;

import org.music.Activity.Home;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;


class ndyduc{
    private static final Logger logger = LoggerFactory.getLogger(ndyduc.class);

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        SwingUtilities.invokeLater(Home::new);
    }
}