package ucr.group1.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import ucr.group1.simulation.Simulation;

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
        context.put("ifIndex", "");
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

    public void createIndex(List<String> links) {
         /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("whichSimulation", "en General");
        insertGeneralParameters(context);
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

    public void createLinks(List<htmlGenerator> list) {

    }

    public void fillStats(Simulation simulation) {

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
