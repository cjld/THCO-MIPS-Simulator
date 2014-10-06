/*
 * SimulatorApp.java
 */
package simulator;

import simulator.ui.SimulatorView;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import simulator.core.Simulator;
import simulator.ui.Terminal;

/**
 * The main class of the application.
 */
public class SimulatorApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        simulator = new Simulator();
        view = new SimulatorView(this);
        new Terminal();
        view.setConfig(simulator.getUiConfig());
        show(view);
        view.openTab(0);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SimulatorApp
     */
    public static SimulatorApp getApplication() {
        return Application.getInstance(SimulatorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SimulatorApp.class, args);
    }

    public String getBinaryCode() {
        return simulator.getBinaryCode();
    }

    @Action
    public Task run() {
        return new RunTask(org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class));
    }

    private class RunTask extends org.jdesktop.application.Task<Object, Void> {

        RunTask(org.jdesktop.application.Application app) {
            super(app);
            simulator.getUiConfig().running = true;
            simulator.getUiConfig().stepping = true;
            view.update();
        }

        @Override
        protected Object doInBackground() {
            setMessage("Running...");
            simulator.run();
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            setMessage("Stopped.");
            simulator.getUiConfig().stepping = false;
            view.update();
        }
    }

    @Action
    public Task compile() {
        return new CompileTask(org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class));
    }

    private class CompileTask extends org.jdesktop.application.Task<Object, Void> {

        CompileTask(org.jdesktop.application.Application app) {
            super(app);
            view.save();
            source = view.getSourceCode();
        }

        @Override
        protected Object doInBackground() {
            setMessage("Start compiling...");
            simulator.compile(source);
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            setMessage("Compiling finished.");
            view.update();
        }
        private String source;
    }

    @Action
    public void stop() {
        simulator.stop();
        view.update();
    }

    @Action
    public Task step() {
        return new StepTask(org.jdesktop.application.Application.getInstance(simulator.SimulatorApp.class));
    }

    private class StepTask extends org.jdesktop.application.Task<Object, Void> {

        StepTask(org.jdesktop.application.Application app) {
            super(app);
            if (simulator.getUiConfig().running == false) {
                view.openTab(1);
            }
            simulator.getUiConfig().running = true;
            simulator.getUiConfig().stepping = true;
            view.update();
        }

        @Override
        protected Object doInBackground() {
            setMessage("Running...");
            simulator.step();
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            setMessage("Paused.");
            simulator.getUiConfig().stepping = false;
            view.update();
        }
    }
    private Simulator simulator;
    private SimulatorView view;
}
