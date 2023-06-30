package gpt.chat;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
//Theme Class to Change the default color to green color
class ColorTitleBar extends DefaultMetalTheme {

    public ColorTitleBar() {
        getWindowTitleInactiveBackground();
        getWindowTitleBackground();
        getPrimaryControlHighlight();
        getPrimaryControlDarkShadow();
        getPrimaryControl();
        getControlHighlight();
        getControlDarkShadow();
        getControl();
    }
    public ColorUIResource getWindowTitleInactiveBackground() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getWindowTitleBackground() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getPrimaryControlHighlight() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getPrimaryControlDarkShadow() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getPrimaryControl() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getControlHighlight() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getControlDarkShadow() {
        return new ColorUIResource(Color.black);
    }

    public ColorUIResource getControl() {
        return new ColorUIResource(Color.black);
    }
}
