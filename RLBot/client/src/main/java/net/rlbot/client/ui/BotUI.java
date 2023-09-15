package net.rlbot.client.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.client.farm.minimal.DisableRenderCallbacks;
import net.rlbot.client.farm.minimal.FpsDrawListener;
import net.rlbot.client.script.handler.ScriptHandler;
import net.rlbot.client.script.loader.ScriptLoader;
import net.rlbot.client.script.loader.ScriptWrapper;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.hooks.DrawCallbacks;

import javax.inject.Singleton;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Slf4j
@Singleton
@SuppressWarnings({"deprecation", "removal"})
public class BotUI {

    private static final DrawCallbacks DISABLE_RENDER_CALLBACKS = new DisableRenderCallbacks();

    private JFrame frame;

    private final Applet applet;

    private final Client client;

    private final ScriptHandler scriptHandler;

    private final ScriptLoader scriptLoader;

    private final FpsDrawListener fpsDrawListener;

    @Inject
    public BotUI(Applet applet, Client client, ScriptHandler scriptHandler, ScriptLoader scriptLoader, FpsDrawListener fpsDrawListener) {
        this.applet = applet;
        this.client = client;
        this.scriptHandler = scriptHandler;
        this.scriptLoader = scriptLoader;
        this.fpsDrawListener = fpsDrawListener;
    }

    public void init(boolean farm) throws InterruptedException, InvocationTargetException {

        SwingUtilities.invokeAndWait(() -> {

            FlatLightLaf.setup(new FlatDarculaLaf());

            frame = new JFrame();
            frame.setResizable(false);
            frame.setTitle("OSRSBot");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                     shutdownClient();
                }
            });

            var topPanel = createTopPanel(frame, farm);
            frame.getContentPane().add(topPanel, BorderLayout.PAGE_START);

            var gamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            gamePanel.setSize(Constants.GAME_FIXED_SIZE);
            gamePanel.add(applet);

            frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
        });
    }

    private JPanel createTopPanel(JFrame parent, boolean farm) {
        var topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        if(!farm) {
            var menuBar = new JMenuBar();
            var fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            var disableRenderMenuItem = new JCheckBoxMenuItem("Disable Rendering");
            disableRenderMenuItem.addActionListener(e -> {
                if(disableRenderMenuItem.isSelected()) {
                    client.setDrawCallbacks(DISABLE_RENDER_CALLBACKS);
                    return;
                }

                client.setDrawCallbacks(null);
            });
            fileMenu.add(disableRenderMenuItem);

            var lowFpsMenuItem = new JCheckBoxMenuItem("Low FPS");
            lowFpsMenuItem.addActionListener(e -> {
                if(lowFpsMenuItem.isSelected()) {
                    fpsDrawListener.setEnabled(true);
                    return;
                }

                fpsDrawListener.setEnabled(false);
            });
            fileMenu.add(lowFpsMenuItem);

            var exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(e -> {
                shutdownClient();
            });
            fileMenu.add(exitMenuItem);

            topPanel.add(menuBar);
        }

        topPanel.add(Box.createHorizontalGlue());

        if(!farm) {
            var startButton = new JButton("Start");
            startButton.addActionListener(e -> {

                if(this.scriptHandler.isScriptRunning())
                    return;

                scriptLoader.loadScripts();

                var scriptSelector = createScriptSelector(parent, scriptLoader.getScripts());
                scriptSelector.pack();
                scriptSelector.setVisible(true);
            });

            topPanel.add(startButton);
        }

        var pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {

            if(!this.scriptHandler.isScriptRunning())
                return;

            this.scriptHandler.setPaused(!this.scriptHandler.isPaused());
        });

        topPanel.add(pauseButton);

        if(!farm) {
            var stopButton = new JButton("Stop");
            stopButton.addActionListener(e -> {
                if(!scriptHandler.isScriptRunning())
                    return;

                scriptHandler.stopScript();
            });

            topPanel.add(stopButton);
        }

        return topPanel;
    }

    private static String[] SCRIPT_SELECTOR_COLUMN_NAMES = new String[] { "Name", "Author", "Version"};

    @SuppressWarnings("unchecked")
    private JDialog createScriptSelector(JFrame parent, Set<ScriptWrapper> scripts) {

        var dialog = new JDialog(parent, true);

        dialog.setResizable(false);
        dialog.setMinimumSize(new Dimension(300, 150));
        dialog.setSize(new Dimension(300, 150));
        dialog.setLocationRelativeTo(null);

        var scriptData = getScriptData(scripts);
        var table = new JTable(scriptData, SCRIPT_SELECTOR_COLUMN_NAMES);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        var scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dialog.add(scrollPane, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());

        var startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            var selectedRowIndex = table.getSelectedRow();

            var script = (ScriptWrapper)scriptData[selectedRowIndex][SCRIPT_SELECTOR_COLUMN_NAMES.length];

            try {
                this.scriptHandler.startScript(script.newInstance());
                dialog.setVisible(false);
                dialog.dispose();
            } catch (Exception ex) {
                log.error("Exception when instantiating script", ex);
            }
        });

        buttonPanel.add(startButton);

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.dispose();
        });

        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.PAGE_END);

        return dialog;
    };

    private static Object[][] getScriptData(Set<ScriptWrapper> scripts) {

        var result = new Object[scripts.size()][SCRIPT_SELECTOR_COLUMN_NAMES.length + 1];

        var i = 0;
        for(var script : scripts) {

            result[i] = new Object[] {
                    script.getName(),
                    script.getAuthor(),
                    script.getVersion(),
                    script
            };
            i++;
        }

        return result;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.toFront();
        });
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return frame.getGraphicsConfiguration();
    }

    public void shutdownClient()
    {
        new Thread(() ->
        {
            if (applet != null)
            {
                // The client can call System.exit when it's done shutting down
                // if it doesn't though, we want to exit anyway, so race it
                int clientShutdownWaitMS;
                if (applet instanceof Client)
                {
                    ((Client) applet).stopNow();
                    clientShutdownWaitMS = 1000;
                }
                else
                {
                    // it will continue rendering for about 4 seconds before attempting shutdown if its vanilla
                    applet.stop();
                    frame.setVisible(false);
                    clientShutdownWaitMS = 6000;
                }

                try
                {
                    Thread.sleep(clientShutdownWaitMS);
                }
                catch (InterruptedException ignored)
                {
                }
            }
            System.exit(0);
        }, "RuneLite Shutdown").start();
    }
}
