package com.github.cluelessskywatcher.reginfile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import com.github.cluelessskywatcher.reginfile.layout.ReginWrapLayout;
import com.github.cluelessskywatcher.reginfile.panels.ReginFilePanel;
import com.github.cluelessskywatcher.reginfile.panels.ReginToolbarPanel;

import lombok.Getter;
import lombok.Setter;
import net.sf.image4j.codec.ico.ICODecoder;

public class ReginFrame extends JFrame {
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 700;
    private JPanel explorerWindow;
    private @Setter @Getter File currentDirectory;
    private JTree fileTree;
    private ReginFilePanel selectedPanel;
    private JLabel currentDirectoryLabel;
    
    public ReginFrame() throws Exception {
        this.currentDirectory = null;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        pack();
        setLocationRelativeTo(null);

        setTitle("Reginfile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        setLayout(new BorderLayout());
        List<BufferedImage> icons = getImageIcons("reginfile.ico");
        setIconImages(icons);

        String currentDirString = "";
        if (currentDirectory != null) {
            currentDirString = currentDirectory.getAbsolutePath();
        }

        currentDirectoryLabel = new JLabel(currentDirString);
        add(currentDirectoryLabel, BorderLayout.NORTH);

        ReginToolbarPanel toolbarPanel = new ReginToolbarPanel(this);
        add(toolbarPanel, BorderLayout.NORTH);

        fileTree = new JTree(generateFileTreeModel());
        fileTree.setBackground(ReginColours.LIGHT_SOOT);
        JScrollPane fileTreeScrollPane = new JScrollPane(fileTree);
        fileTreeScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(fileTreeScrollPane, BorderLayout.WEST);

        explorerWindow = new JPanel(new ReginWrapLayout(FlowLayout.LEFT, 10, 10));
        explorerWindow.setBackground(ReginColours.LIGHT_SOOT);
        JScrollPane explorerScrollPane = new JScrollPane(explorerWindow);
        explorerScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(explorerScrollPane, BorderLayout.CENTER);

        fileTree.addTreeSelectionListener(e -> updateExplorerWindow());

        explorerWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    // Clicked away from the file, deselect
                    selectedPanel.setBackground(null);
                    selectedPanel = null;
                }
            }
        });

        updateExplorerWindow();
    }

    private void updateExplorerWindow() {
        explorerWindow.removeAll();

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode.getUserObject() instanceof File) {
            File selectedFile = (File) selectedNode.getUserObject();
            this.currentDirectory = selectedFile;
            File[] files = selectedFile.listFiles();

            if (files != null) {
                for (File file : files) {
                    explorerWindow.add(new ReginFilePanel(
                        file, this, this::highlightAction
                    ));
                }
            }
        }

        explorerWindow.revalidate();
        explorerWindow.repaint();
    }

    public void loadFolder(File directory) {
        explorerWindow.removeAll();
        this.currentDirectory = directory;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                explorerWindow.add(new ReginFilePanel(
                    file, this, this::highlightAction
                ));
            }
        }

        explorerWindow.revalidate();
        explorerWindow.repaint();
    }

    private DefaultMutableTreeNode generateFileTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new File(System.getProperty("user.home")));
        File[] roots = File.listRoots();
        for (File file : roots) {
            root.add(new DefaultMutableTreeNode(file));
        }

        return root;
    }

    public void display() throws IOException {
        setVisible(true);
    }

    public static List<BufferedImage> getImageIcons(String iconName) throws IOException {
        ClassLoader classLoader = ReginFrame.class.getClassLoader();
        File filePath = new File(classLoader.getResource(iconName).getFile());
        List<BufferedImage> images = ICODecoder.read(filePath);
        return images;
    }

    private void highlightAction(ReginFilePanel panel) {
        if (selectedPanel != null && selectedPanel != panel) {
            selectedPanel.setBackground(null);
        }

        panel.setBackground(new Color(196, 196, 196));
        selectedPanel = panel;
    }
}
