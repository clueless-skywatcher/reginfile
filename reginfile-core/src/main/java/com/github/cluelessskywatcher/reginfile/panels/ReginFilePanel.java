package com.github.cluelessskywatcher.reginfile.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import com.github.cluelessskywatcher.reginfile.ImageLoadUtils;
import com.github.cluelessskywatcher.reginfile.ReginColours;
import com.github.cluelessskywatcher.reginfile.ReginFrame;

import me.marnic.jiconextract2.JIconExtract;
import net.sf.image4j.codec.ico.ICODecoder;

@SuppressWarnings("unused")
public class ReginFilePanel extends JPanel {
    private Consumer<ReginFilePanel> highlightAction;
    private ReginFrame mainFrame;
    
    public ReginFilePanel(File file, ReginFrame mainFrame, Consumer<ReginFilePanel> highlightAction) {
        this.highlightAction = highlightAction;
        this.mainFrame = mainFrame;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(null);
        setPreferredSize(new Dimension(60, 60));

        String fileName = FileSystemView.getFileSystemView().getSystemDisplayName(file);

        ImageIcon icon;
        if (file.getName().toLowerCase().endsWith(".ico")) {
            icon = new ImageIcon(getIconImage(file));
        }
        else if (file.getName().toLowerCase().endsWith(".exe")) {
            icon = new ImageIcon(JIconExtract.getIconForFile(32, 32, file.getAbsolutePath()));
            icon = resizeIcon(icon, 32, 32);
        }
        else {
            icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file, 32, 32);
            icon = resizeIcon(icon, 32, 32);
        }

        JLabel iconLabel = new JLabel(icon);
        JLabel fileNameLabel = new JLabel(fileName, SwingConstants.CENTER);

        int maxWidth = 1000;
        iconLabel.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
        fileNameLabel.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        fileNameLabel.setForeground(Color.WHITE);

        // Set vertical alignment for the labels
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        fileNameLabel.setAutoscrolls(true);
        
        add(iconLabel);
        add(fileNameLabel);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                highlightAction.accept(ReginFilePanel.this);
                if (event.getClickCount() == 2) {
                    if (file.isDirectory()) {
                        mainFrame.loadFolder(file);
                    }
                    else {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            public void mouseEntered(MouseEvent event) {
                setBackground(ReginColours.RAIN_CLOUD);
            }

            public void mouseExited(MouseEvent event) {
                setBackground(null);
            }
        });

        setToolTipText(fileName);
    }

    private Image getIconImage(File file) {
        List<BufferedImage> images;
        try {
            images = ICODecoder.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        for (BufferedImage image : images) {
            if (image.getWidth() == 32 && image.getHeight() == 32) {
                return image;
            }
        }
        return null;
    }

    private Image getResourceIconImage(File file) {
        try {
            return ReginFrame.getImageIcons(file.getName()).get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void openFile(File file) {
        System.out.println("Opening file: " + file.getAbsolutePath());
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(height, width, Image.SCALE_DEFAULT));
    }
}
