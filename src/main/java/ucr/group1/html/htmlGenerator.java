package ucr.group1.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Gonzalo on 2/20/2017.
 */
public class htmlGenerator {

    public void crea() {
         /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        Template t = ve.getTemplate("./src/main/resources/htmlGenerator.vcss");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("stabilityFirstModule", "Gonzalo");
        context.put("avgRejectedConections", "PERRO");
        context.put("newLine", "<li><a href=http://brackets.io>Brackets.io</a></li>");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        /* show the World */
        System.out.println(writer.toString());
        StringWriter finalWriter = new StringWriter();
        ve.mergeTemplate("./src/main/resources/htmlGenerator.vcss", "utf-8", context, finalWriter);
        String html = finalWriter.toString();


        try {
            File file = new File("./src/main/resources/index.html");
            file.delete();
            file.createNewFile();
            Files.write(Paths.get("./src/main/resources/index.html"), html.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
