package com.example.swt_app;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SwtTest {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Test");
        shell.setSize(200, 200);
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}

