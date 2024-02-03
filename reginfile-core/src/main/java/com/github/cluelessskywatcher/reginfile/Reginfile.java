package com.github.cluelessskywatcher.reginfile;

import javax.swing.SwingUtilities;

public class Reginfile 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            try {
                ReginFrame explorer = new ReginFrame();
                explorer.display();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
