package com.github.cluelessskywatcher.reginfile.panels;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.github.cluelessskywatcher.reginfile.ImageLoadUtils;
import com.github.cluelessskywatcher.reginfile.ReginColours;
import com.github.cluelessskywatcher.reginfile.ReginFrame;
import com.github.cluelessskywatcher.reginfile.controls.ReginToolbarButton;

public class ReginToolbarPanel extends JToolBar {
    public ReginToolbarPanel(ReginFrame reginFrame) throws IOException {
        JButton refreshButton = new ReginToolbarButton("Refresh", ImageLoadUtils.loadImage("refresh.png"));
        JButton upOneLevelButton = new ReginToolbarButton("Up one level", ImageLoadUtils.loadImage("uponelevel.png"));

        refreshButton.addActionListener((ActionEvent e) -> {
            System.out.println("Running refresh");
        });
        upOneLevelButton.addActionListener((ActionEvent e) -> {
            if (reginFrame.getCurrentDirectory() != null) {
                reginFrame.loadFolder(reginFrame.getCurrentDirectory().getParentFile());
            }
        });
        setBackground(ReginColours.NIGHT_GREY);
        add(refreshButton);
        addSeparator();
        add(upOneLevelButton);
    }
}
