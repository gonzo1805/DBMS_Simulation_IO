package ucr.group1.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import ucr.group1.simulation.Simulation;
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

    int kConcurrentConection;
    int nVerificationServers;
    int pExecutionServers;
    int mTransactionServers;
    int tTimeout;
    boolean slowMode;
    int timeBetEvents;
    int simulationTime;
    int amountOfRuns;
    Simulation simulation;

    public void crea(String fileName, String whichSimulation) {
         /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("whichSimulation", whichSimulation);
        context.put("ifIndex", "<a href=\"index.html\">Regresar</a>");
        insertGeneralParameters(context);
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        StringWriter finalWriter = new StringWriter();
        ve.mergeTemplate("./src/main/resources/htmlGenerator.vcss", "utf-8", context, finalWriter);
        String html = finalWriter.toString();


        try {
            File file = new File("./src/main/resources/" + fileName + ".html");
            file.delete();
            file.createNewFile();
            Files.write(Paths.get("./src/main/resources/" + fileName + ".html"), html.getBytes(), StandardOpenOption.APPEND);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createIndex(List<String> links, SimulationsStatistics stats) {
         /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("whichSimulation", "en General");

        insertGeneralParameters(context);
        fillStats(stats, context);

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        context.put("ifIndex", "<h1>Estadísticas específicas por corrida</h1>");
        context.put("listOfSimulations", links);
        t.merge(context, writer);
        StringWriter finalWriter = new StringWriter();


        ve.mergeTemplate("./src/main/resources/htmlGenerator.vcss", "utf-8", context, finalWriter);
        String html = finalWriter.toString();


        try {
            File file = new File("./src/main/resources/index.html");
            file.delete();
            file.createNewFile();
            Files.write(Paths.get("./src/main/resources/index.html"), html.getBytes(), StandardOpenOption.APPEND);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void fillStats(SimulationsStatistics stats, VelocityContext context) {
        for (int i = 0; i < 5; i++) {
            fillStatsPerModule(stats, context, i);
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
        context.put("avgRejectedConections", stats.getNumberOfRejectedQueries());
        // Avg Time for connections
        context.put("avgTimePerConection", stats.getAvgLifespanOfQuery());
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
