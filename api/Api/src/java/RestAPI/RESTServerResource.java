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

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import javax.ejb.Stateless;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.*;
import org.json.JSONArray;
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
        String s = "<html><body><h1>";
        //return "<html><body><h1>Hello !</h1></body></html>";
        // Udskift med din egen databasedriver og -URL
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("USE test;");
            ResultSet rs = statement.executeQuery("SELECT  * FROM MyGuests;");

            while (rs.next()) {
                String navn = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                System.out.println(navn + " " + lastname);
                s = s.concat(navn + " " + lastname + "\n");
            }
        } catch (Exception e) {
            System.exit(1);
        }
        s = s.concat("...</h1></body></html>");
        return s;

    }

    @Path("json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doThis(@QueryParam("test") String x) {
        JSONArray response = new JSONArray();
        //  JSONObject response = new JSONObject();

        Connection connection = null;
        Statement statement = null;
        JSONObject jo = new JSONObject();

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("USE Test01;");
            ResultSet rs = statement.executeQuery("SELECT * FROM TrendlogRecords WHERE VALUE<100 AND VALUE >1 AND Trendlogid=11;");
            //statement.executeUpdate("USE test;");
            //ResultSet rs = statement.executeQuery("SELECT  * FROM MyGuests;");

            while (rs.next()) {
                String id = rs.getString("Id");
                String value = rs.getString("Value");
                System.out.println("id: " + id + " , value: " + value);
                jo.put("Id", id);
                jo.put("Value", value);
                response.put(jo);
            }

            jo.put("testAfTest", x);

            //while (rs.next()) {
            /*
            System.out.println("Rhello my friend");
            JSONObject jo = new JSONObject();
            String navn = rs.getString(0);
             */
 /*
            statement.executeUpdate("USE test;");
            ResultSet rs = statement.executeQuery("SELECT  * FROM MyGuests;");

            while (rs.next()) {
                JSONObject jo = new JSONObject();
                String navn = rs.getString("firstname");
                String lastname = rs.getString("lastname");

                jo.put("firstname", navn);
                jo.put("lastname", lastname);
                response.put(jo);

            }
             */
        } catch (Exception e) {
            System.exit(1);
        }

        return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
    }

    @Path("Average")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response average(@Context UriInfo info) {
        //@QueryParam("ID") String buildingID
        //Info
        String buildingID = info.getQueryParameters().getFirst("ID");
        String time = info.getQueryParameters().getFirst("TIME");
        //T for today
        //W for last 7 days
        //M for month

        //Answer
        JSONObject response = new JSONObject();

        //Hardcoded buildings
        ArrayList<String> buildingList = new ArrayList<>();
        buildingList.add("450");
        buildingList.add("424");
        buildingList.add("412");

        String final_id = "0";

        for (int i = 0; i < buildingList.size(); i++) {

            switch (buildingID) {
                case "450":
                    final_id = "120";
                    break;
                case "424":
                    final_id = "127";
                    break;
                case "412":
                    final_id = "135";
                    break;
                default:
                    return Response.status(Response.Status.BAD_REQUEST).build();
            }

        }

        //  JSONObject response = new JSONObject();
        Connection connection = null;
        Statement statement = null;
        JSONObject jo = new JSONObject();

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();

            statement.executeUpdate("USE Test01;");
            ResultSet rs = statement.executeQuery("SELECT * FROM TrendlogRecords WHERE VALUE<94 AND VALUE > 2 AND Trendlogid=" + final_id + ";");
            //statement.executeUpdate("USE test;");
            //ResultSet rs = statement.executeQuery("SELECT  * FROM MyGuests;");

            ArrayList<Double> DataArray = new ArrayList<>();
            while (rs.next()) {
                //String id = rs.getString("Id");
                String s_value = rs.getString("Value").replace(',', '.');
                Double value = Double.valueOf(s_value);
                DataArray.add(value);

                //System.out.println("value: " + value);
                //jo.put("Id", id);
            }

            double sum = 0;
            for (int i = 0; i < DataArray.size(); i++) {
                sum += DataArray.get(i);
            }
            double average = sum / DataArray.size();

            //Put in final answer
            jo.put("Value", average);
            response.put("Value", jo);

            return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }

    /*
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
         
        return Response.ok(response.toString(), MediaType.APPLICATION_JSON).build();
    }
     */
}
