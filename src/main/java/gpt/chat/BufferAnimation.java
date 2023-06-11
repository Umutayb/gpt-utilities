package gpt.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class BufferAnimation {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AnimationPanel animationPanel = new AnimationPanel();
        frame.setGlassPane(animationPanel);
        animationPanel.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(300,300));
        frame.setVisible(true);
    }

    public BufferAnimation() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static class AnimationPanel extends JPanel {
        private double angle;
        private double extent;

        private final double angleDelta = -1;
        private final double extentDelta = -5;

        private boolean flip = false;

        public AnimationPanel() {
            setOpaque(false);
            Timer timer = new Timer(10, e -> {
                angle += angleDelta;
                extent += extentDelta;
                if (Math.abs(extent) % 360.0 == 0) {
                    angle = angle - extent;
                    flip = !flip;
                    if (flip)
                        extent = 360.0;
                    else
                        extent = 0.0;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // For semi-transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setPaint(Color.GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setPaint(Color.BLACK);

            Arc2D.Double arc = new Arc2D.Double((double) (getWidth() - 100) / 2, (double) (getHeight() - 100) / 2, 100, 100, angle, extent, Arc2D.OPEN);
            BasicStroke stroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
            g2d.setStroke(stroke);
            g2d.setColor(Color.black);
            g2d.draw(arc);
            g2d.dispose();
        }

    }
}