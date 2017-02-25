package ucr.group1.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import ucr.group1.simulation.Simulation;
import ucr.group1.statistics.ModuleStatistics;
import ucr.group1.statistics.QueryStatistics;
import ucr.group1.statistics.SimulationsStatistics;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Created by Gonzalo on 2/20/2017.
 */
public class htmlGenerator {

    /**
     * Main Parameters of the Simulation
     */
    int kConcurrentConection;
    int nVerificationServers;
    int pExecutionServers;
    int mTransactionServers;
    int tTimeout;
    boolean slowMode;
    int timeBetEvents;
    int simulationTime;
    int amountOfRuns;
    Simulation simulation;// A pointer to the simulation

    /**
     * Create the .html for an specific run of the simulation
     *
     * @param fileName        the name of the .html
     * @param whichSimulation a parameter to put on the template structure of the .html
     */
    public void crea(String fileName, String whichSimulation, Simulation simulation) {

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        // Get the template
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");

        VelocityContext context = new VelocityContext();
        // Give it the button to return to the main page and the name
        context.put("whichSimulation", whichSimulation);
        context.put("ifIndex", "<a href=\"index.html\">Regresar</a>");
        // Give it the parameters of all the simulation
        insertGeneralParameters(context);

        fillStatsPerModuleWithModuleStats(simulation.getClientManagementStatistics(), context, 0,
                simulation.getQueryStatistics());
        fillStatsPerModuleWithModuleStats(simulation.getClientManagementStatistics(), context, 1,
                simulation.getQueryStatistics());
        fillStatsPerModuleWithModuleStats(simulation.getQueriesVerificationStatistics(), context, 2,
                simulation.getQueryStatistics());
        fillStatsPerModuleWithModuleStats(simulation.getTransactionsStatistics(), context, 3,
                simulation.getQueryStatistics());
        fillStatsPerModuleWithModuleStats(simulation.getQueriesExecutionStatistics(), context, 4,
                simulation.getQueryStatistics());

        // Merge the template
        StringWriter finalWriter = new StringWriter();
        ve.mergeTemplate("./src/main/resources/htmlGenerator.vcss", "utf-8", context, finalWriter);
        String html = finalWriter.toString();

        try {
            // Create the file, if it exist, delete it and create again
            File file = new File("./src/main/resources/" + fileName + ".html");
            file.delete();
            file.createNewFile();
            Files.write(Paths.get("./src/main/resources/" + fileName + ".html"), html.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the template for the main .html page
     * @param links A list of the other simulations names, it will be send to the template to create the
     *              Hyperlinks to the personal simulation pages
     * @param stats The general stats of the simulation
     */
    public void createIndex(List<String> links, SimulationsStatistics stats) {

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        // Get the template
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");

        VelocityContext context = new VelocityContext();
        // Give the general name
        context.put("whichSimulation", "en General");
        // Fill with all the stats of the simulation
        insertGeneralParameters(context);
        fillStats(stats, context);
        // Because it is the main .html give it an extra section for the other simulations links
        context.put("ifIndex", "<h1>Estadísticas específicas por corrida</h1>");
        // Insert all the other simulations
        context.put("listOfSimulations", links);
        // Merge the Template
        StringWriter finalWriter = new StringWriter();
        ve.mergeTemplate("./src/main/resources/htmlGenerator.vcss", "utf-8", context, finalWriter);
        String html = finalWriter.toString();

        try {
            // Create the file, if it exist, delete it and create again
            File file = new File("./src/main/resources/index.html");
            file.delete();
            file.createNewFile();
            Files.write(Paths.get("./src/main/resources/index.html"), html.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the attributes (parameters) of the simulation
     * @param simulation a pointer to the simulation
     * @param amountOfRuns the amount of runs of the simulation
     * @param kConcurrentConection the k concurrent conections of the simulations
     * @param pExecutionServers the p execution servers of the simulations
     * @param mTransactionServers the m transaction servers of the simulations
     * @param nVerificationServers the n verification servers of the simulations
     * @param timeBetEvents the time between events of the simulations
     * @param simulationTime the time per simulation of the simulations
     * @param tTimeout the t timeout of the simulations
     * @param slowMode if the simulations are on slow mode or not
     */
    public void fillParameters(Simulation simulation, int amountOfRuns, int kConcurrentConection, int pExecutionServers,
                               int mTransactionServers, int nVerificationServers, int timeBetEvents, int simulationTime,
                               int tTimeout, boolean slowMode) {
        this.simulation = simulation;
        this.kConcurrentConection = kConcurrentConection;
        this.nVerificationServers = nVerificationServers;
        this.pExecutionServers = pExecutionServers;
        this.mTransactionServers = mTransactionServers;
        this.tTimeout = tTimeout;
        this.slowMode = slowMode;
        this.timeBetEvents = timeBetEvents;
        this.simulationTime = simulationTime;
        this.amountOfRuns = amountOfRuns;
    }

    /**
     * Caller for the method that fill all the stats of the simulation on the context send by parameter
     * @param stats the stats of the simulation
     * @param context the simulation template to fill the stats
     */
    public void fillStats(SimulationsStatistics stats, VelocityContext context) {
        // There are 5 modules, so we need to fill 5 times per simulation
        for (int i = 0; i < 5; i++) {
            fillStatsPerModule(stats, context, i /*The module*/);
        }
    }

    /**
     * Fill the stats of the module in the module parameter
     * 0 ClientManagementeModule
     * 1 ProcessesManagementModule
     * 2 QueriesVerificationModule
     * 3 TransactionsModule
     * 4 QueriesExecutionModule
     *
     * @param stats   the simulation stats
     * @param context the velocity context to put things
     * @param module  the module, eventually it gonna be the name of the module
     */
    private void fillStatsPerModule(SimulationsStatistics stats, VelocityContext context, int module) {
        // Switch to bypass the implementation of the parameters of the velocity template
        String stringModule = "";
        switch (module) {
            case 0:
                stringModule += "First";
                break;
            case 1:
                stringModule += "Second";
                break;
            case 2:
                stringModule += "Third";
                break;
            case 3:
                stringModule += "Fourth";
                break;
            case 4:
                stringModule += "Fifth";
                break;
        }

        // Avg Rejected querys of
        context.put("avgRejectedConections", stats.getAverageRejectedQueries());
        // Avg Time for connections
        context.put("avgTimePerConection", stats.getAverageLifespan());
        // If it is stable or not in function of rhp
        if (stats.getRho(module) < 1) {
            context.put("stability" + stringModule + "Module", "Estable");
        } else {
            context.put("stability" + stringModule + "Module", "Inestable");
        }
        // Avg size of the queue
        context.put("avgSizeOfTheQueue" + stringModule + "Module", stats.getL_q(module));
        // Income rate
        context.put("lambda" + stringModule + "Module", stats.getLambda(module));
        // Service rate
        context.put("mu" + stringModule + "Module", stats.getMu(module));
        // Percentage of usage
        context.put("rho" + stringModule + "Module", stats.getRho(module));
        // Avg Clients on the system
        context.put("l" + stringModule + "Module", stats.getL(module));
        // Avg Clients on service
        context.put("ls" + stringModule + "Module", stats.getL_s(module));
        // Avg Clients on queue
        context.put("lq" + stringModule + "Module", stats.getL_q(module));
        // Avg time of clients on the system
        context.put("w" + stringModule + "Module", stats.getW(module));
        // Avg time of clients on queue
        context.put("wq" + stringModule + "Module", stats.getW_q(module));
        // Avg time of clients on service
        context.put("ws" + stringModule + "Module", stats.getL_s(module));
    }

    public void fillStatsPerModuleWithModuleStats(ModuleStatistics stats, VelocityContext context, int module,
                                                  QueryStatistics queryStatistics) {
        // Switch to bypass the implementation of the parameters of the velocity template
        String stringModule = "";
        switch (module) {
            case 0:
                stringModule += "First";
                break;
            case 1:
                stringModule += "Second";
                break;
            case 2:
                stringModule += "Third";
                break;
            case 3:
                stringModule += "Fourth";
                break;
            case 4:
                stringModule += "Fifth";
                break;
        }

        // Avg Rejected querys of
        context.put("avgRejectedConections", queryStatistics.getNumberOfRejectedQueries());
        // Avg Time for connections
        context.put("avgTimePerConection", queryStatistics.getAvgLifespanOfQuery());
        // If it is stable or not in function of rhp
        if (stats.getRho() < 1) {
            context.put("stability" + stringModule + "Module", "Estable");
        } else {
            context.put("stability" + stringModule + "Module", "Inestable");
        }
        // Avg size of the queue
        context.put("avgSizeOfTheQueue" + stringModule + "Module", stats.getL_q());
        // Income rate
        context.put("lambda" + stringModule + "Module", stats.getLambda());
        // Service rate
        context.put("mu" + stringModule + "Module", stats.getMu());
        // Percentage of usage
        context.put("rho" + stringModule + "Module", stats.getRho());
        // Avg Clients on the system
        context.put("l" + stringModule + "Module", stats.getL());
        // Avg Clients on service
        context.put("ls" + stringModule + "Module", stats.getL_s());
        // Avg Clients on queue
        context.put("lq" + stringModule + "Module", stats.getL_q());
        // Avg time of clients on the system
        context.put("w" + stringModule + "Module", stats.getW());
        // Avg time of clients on queue
        context.put("wq" + stringModule + "Module", stats.getW_q());
        // Avg time of clients on service
        context.put("ws" + stringModule + "Module", stats.getL_s());
    }

    /**
     * Insert the general parameters of all the simulations on the template
     * @param context the context where we gonna insert the data
     */
    private void insertGeneralParameters(VelocityContext context) {
        context.put("kConcurrentConnections", kConcurrentConection);
        context.put("nVerificationServers", nVerificationServers);
        context.put("pExecutionServers", pExecutionServers);
        context.put("mTransactionServers", mTransactionServers);
        context.put("tTimeout", tTimeout);
        if (slowMode) {
            context.put("slowMode", "Activado");
        } else {
            context.put("slowMode", "Desactivado");
        }
        context.put("timeBetEvents", timeBetEvents);
        context.put("simulationTime", simulationTime);
    }
}
