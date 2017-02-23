package ucr.group1.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

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
        context.put("nombre", "Gonzalo");
        context.put("nombre2", "PERRO");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        /* show the World */
        System.out.println(writer.toString());
    }


}
