/* File: TunaMessageBodyReader.java
 * Author: Stanley Pieda
 * The code and methods in this file originally came from Edgar Martinez et. al. (n.d.)
 * They have been modified slightly to work with a DemoStuff application server for educational purposes.
 * Edgar Martinez, Susan Moxley, and Juan Quezada (n.d.). Developing an Enterprise Application with JavaFX 2.0 and Java EE 7. [online]. Retrieved from
 * http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/javafx_json_tutorial/javafx_javaee7_json_tutorial.html
*/

import entity.Tuna;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.END_OBJECT;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event.START_OBJECT;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes({"application/json"})
public class TunaMessageBodyReader implements MessageBodyReader<List<Tuna>>{
    
    @Override
public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, 
                                           MediaType mt) {
    return true;
}

@Override
public List<Tuna> readFrom(Class<List<Tuna>> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException,  WebApplicationException {
    if (mt.getType().equals("application") && mt.getSubtype().equals("json")) {
      Tuna Tuna = new Tuna();
      List<Tuna> Tunas = new ArrayList();
      JsonParser parser = Json.createParser(in);
      while (parser.hasNext()) {
        JsonParser.Event event = parser.next();
        switch (event) {
          case START_OBJECT:
            Tuna = new Tuna();
            break;
          case END_OBJECT:
            Tunas.add(Tuna);
            break;
          case KEY_NAME:
            String key = parser.getString();
            parser.next();
            //Check the key and set the proper field
            switch (key) {
              case "id":
                Tuna.setId(parser.getLong());
                break;
              case "recordNumber":
                Tuna.setRecordNumber(Integer.parseInt(parser.getString()));
                break;
              case "omega":
                Tuna.setOmega(parser.getString());
                break; 
              case "lambda":
                  Tuna.setLambda(parser.getString());
              case "uuid":
                  Tuna.setUuid(parser.getString());
              default:
                break;
            }
            break;
          default:
            break;
        }
      }
      return Tunas;
    }
    throw new UnsupportedOperationException("Not supported MediaType: " + mt);
  }

}
