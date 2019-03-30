/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package RestAPI;

import javax.ejb.Stateless;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import org.json.JSONArray;
import org.json.JSONObject;
//import com.sun.xml.internal.ws.streaming.XMLStreamReaderException;

/**
 * REST Web Service
 *
 * @author mkuchtiak
 */
@Stateless
@Path("")
public class RESTServerResource {

    /**
     * Retrieves representation of an instance of helloworld.HelloWorldResource
     *
     * @return an instance of java.lang.String
     */
    @Path("test")
    @GET
    @Produces("text/html")
    public String getGreeting() {
        return "<html><body><h1>Hello !</h1></body></html>";
    }

    @Path("closeAllGames")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postcloseAllGames(String content) {
        JSONObject recived = new JSONObject(content);
        JSONObject response = new JSONObject();
        String token = recived.getString("token");
        String username = recived.getString("username");
        /*
        //Authenticate
        if (Storage.tokenMap.get(username).equals(token)) {
            //TODO: check for "admin" user
            
            if (!Storage.admins.contains(username)){
                System.out.println("Unautherized closeAllGames call by user: "+ username);
                response.put("pictureUrl", "https://http.cat/401");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response.toString()).build();
            }
            
            
            int gamesClosed;
            try {
                ServerSpil_Interface ServerSpil_inst = (ServerSpil_Interface) Naming.lookup("rmi://130.225.170.205:1099/ServerSpil_Snyd_RMI");
                gamesClosed = (ServerSpil_inst.closegames());
                
                response = recived;
                response.put("gamesClosed", gamesClosed);
                response.remove("token");
                
                System.out.println("All games closed by user: "+ username);
                response.put("pictureUrl", "https://http.cat/200");
                return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
                //System.err.println(SDA);

            } catch (java.rmi.ServerError e) {
                //Der var ingen spil
                //e.printStackTrace();
                System.out.println("No running games to close. By user: "+username);
                response.put("gamesClosed", 0);
                response.put("pictureUrl", "https://http.cat/200");
                return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
                //return Response.noContent().build();
            } catch (java.rmi.UnmarshalException e) {
                e.printStackTrace();
                System.out.println("The RMI-server lorte fejl :(");
                
                response.put("pictureUrl", "https://http.cat/500");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
            } catch (Exception e) {
                //Serveren er nok ikke oppe
                e.printStackTrace();
                System.out.println("The RMI-server is probably not running :(");
                
                response.put("pictureUrl", "https://http.cat/500");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
            }

        } else {
            //not valid user/token
            System.out.println("User failed to authenticate with username" + username + " and token: " + token);
            response.put("pictureUrl", "https://http.cat/401");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response.toString()).build();
        }

    }
    
    @Path("closeGame")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postcloseGame(String content) {
        JSONObject recived = new JSONObject(content);
        JSONObject response = new JSONObject();
        String token = recived.getString("token");
        String username = recived.getString("username");

        //Authenticate
        if (Storage.tokenMap.get(username).equals(token)) {

            int port;
            try {
                ServerSpil_Interface ServerSpil_inst = (ServerSpil_Interface) Naming.lookup("rmi://130.225.170.205:1099/ServerSpil_Snyd_RMI");
                port = ServerSpil_inst.closegame(recived.getInt("port"), username);
                
                response = recived;
                response.put("port", port);
                response.remove("token");
                
                System.out.println("closeGame worked on port: "+ port+" by user: "+ username);
                response.put("pictureUrl", "https://http.cat/200");
                return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();

            } catch (java.rmi.ServerError e) {
                //No game found and closed
                //e.printStackTrace();
                
                System.out.println("No game matched port:"+recived.getInt("port")+" with username: " + username);
                response.put("pictureUrl", "https://http.cat/400");
                return Response.status(Response.Status.BAD_REQUEST).entity(response.toString()).build();
                //return Response.noContent().build();
            } catch (java.rmi.UnmarshalException e) {
                e.printStackTrace();
                System.out.println("The RMI-server lorte fejl :(");
                
                response.put("pictureUrl", "https://http.cat/500");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
            } catch (Exception e) {
                //Serveren er nok ikke oppe
                e.printStackTrace();
                System.out.println("The RMI-server is probably not running :(");
                
                response.put("pictureUrl", "https://http.cat/500");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
            }

        } else {
            //not valid user/token
            System.out.println("User failed to authenticate with username" + username + " and token: " + token);
            response.put("pictureUrl", "https://http.cat/401");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response.toString()).build();
        }

    }
        

    @Path("games")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGames() {
        //TODO return list of games gotten from Server Spil object over RMI
        JSONObject response = new JSONObject();
        ArrayList<SpilData> SDA = new ArrayList<SpilData>();
        
        try {
            ServerSpil_Interface ServerSpil_inst = (ServerSpil_Interface) Naming.lookup("rmi://130.225.170.205:1099/ServerSpil_Snyd_RMI");
            SDA = (ServerSpil_inst.getGames());
            System.out.println("RMI returned a list that was not empty");
            //System.err.println(SDA);

        } catch (java.rmi.ServerError e) {
            //Der var ingen spil
            //e.printStackTrace();
            System.out.println("RMI returned a list that was empty");
            JSONArray games = new JSONArray();
            return Response.ok(games.toString(), MediaType.APPLICATION_JSON).build();
            //return Response.noContent().build();
        } catch (java.rmi.UnmarshalException e) {
            e.printStackTrace();
            System.out.println("The RMI-server lorte fejl :(");

            response.put("pictureUrl", "https://http.cat/500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
        } catch (Exception e) {
            //Serveren er nok ikke oppe
            e.printStackTrace();
            System.out.println("The RMI-server is probably not running :(");

            response.put("pictureUrl", "https://http.cat/500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.toString()).build();
        }

        JSONArray games = new JSONArray();

        for (SpilData SpilData : SDA) {
            JSONObject game = new JSONObject();
            game.put("spillere", SpilData.getSpillere());
            game.put("terninger", SpilData.getTerninger());
            //game.put("token", 42);
            game.put("brugernavn", SpilData.getBrugernavn());
            game.put("port", SpilData.getID());
            games.put(game);

        }
        System.out.println("GET on getGames");

        return Response.ok(games.toString(), MediaType.APPLICATION_JSON).build();
    }
         */
        return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
    }
}
