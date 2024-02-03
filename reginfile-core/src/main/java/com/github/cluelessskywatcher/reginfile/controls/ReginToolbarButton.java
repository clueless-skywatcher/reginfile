package com.github.cluelessskywatcher.reginfile.controls;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ReginToolbarButton extends JButton {
    private static final Insets MARGINS = new Insets(0, 0, 0, 0);

    public ReginToolbarButton(String name, ImageIcon icon) {
        super(icon);
        setName(name);
        setSize(16, 16);
        setMargin(MARGINS);
        setToolTipText(name);
    }
}
