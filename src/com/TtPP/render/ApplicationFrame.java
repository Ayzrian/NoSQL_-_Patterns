package com.TtPP.render;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ApplicationFrame extends Frame {
    public ApplicationFrame() {
        super("Lab 3-4. Database view.");
        createUI();
    }

    protected void createUI () {
        setSize(500, 400);
        setLayout(new BorderLayout());
        setResizable(false);
        setVisible(true);
        center();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    public void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }
}
