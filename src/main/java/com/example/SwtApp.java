package com.example;

import fi.iki.elonen.NanoHTTPD;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SwtApp {

    public static void main(String[] args) throws IOException {
        // Start the local server to capture events
        EventServer server = new EventServer();
        server.start();

        // Setup SWT display and shell (main window)
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Event Capture Example");
        shell.setSize(500, 400);

        // Create a button that opens the HTML page in the browser
        Button openHtmlButton = new Button(shell, SWT.PUSH);
        openHtmlButton.setText("Open HTML File");
        openHtmlButton.setBounds(150, 150, 200, 50);  // Set button position and size

        // Add click listener to the button
        openHtmlButton.addListener(SWT.Selection, event -> {
            try {
                // Open the index.html in the default web browser
                String url = "https://fclsqa:7081/fcls/";
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("Opened HTML file in the browser: " + url);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        // Open the shell window
        shell.open();

        // Event loop to keep the window open
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose(); // Clean up resources
    }

    // Local HTTP server to capture events
    public static class EventServer extends NanoHTTPD {

        public EventServer() throws IOException {
            super(8081);  // Listen on port 8081
        }

        @Override
        public Response serve(IHTTPSession session) {
            Map<String, String> files = new HashMap<>();  // Initialize the files map

            if (Method.POST.equals(session.getMethod())) {
                StringBuilder postBody = new StringBuilder();
                try {
                    // Parse the POST data
                    session.parseBody(files);  // Ensure files map is passed and initialized

                    // Get the input parameters from the form or request
                    Map<String, String> parms = session.getParms();
                    
                    // Check if parameters were received
                    if (parms.isEmpty()) {
                        System.out.println("No parameters received.");
                    } else {
                        // Log each parameter
                        for (Map.Entry<String, String> entry : parms.entrySet()) {
                            postBody.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }

                        // Print the captured event in the console
                        System.out.println("Captured Event: " + postBody.toString());
                    }
                } catch (IOException | ResponseException e) {
                    e.printStackTrace();
                }
            }

            // Return HTTP OK response with CORS headers
            Response response = newFixedLengthResponse(Response.Status.OK, "application/json", "{\"status\":\"OK\"}");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");

            return response;
        }



    }
}





