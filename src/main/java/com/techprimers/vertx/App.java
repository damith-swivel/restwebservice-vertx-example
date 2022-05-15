package com.techprimers.vertx;


import java.util.concurrent.TimeUnit;

import io.vertx.core.http.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import io.netty.util.Timeout;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class App extends AbstractVerticle {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");
	static JsonObject json;
    public static void main(String[] args) {
    	
		int port  = Integer.parseInt(args[0]);
    	PropertyConfigurator.configure(System.getProperty("user.dir")+"\\log4j.properties");
    	final HttpServerOptions httpServerOptions = new HttpServerOptions();
    	httpServerOptions.setAcceptBacklog(10000);
    	DeploymentOptions options = new DeploymentOptions().setInstances(30);
    	final Vertx vertx = Vertx.vertx();
    	final HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
    	final Router router = Router.router(vertx);
    	vertx.deployVerticle((App.class), options);

          	  Route OperatorTypeCheck = router
					  //GET Endpoint
	                     .get("/:operator/:msisdn/getsubscribertype")
	                     .produces("application/json")
	                     .handler(routingContext -> {
							 String operator = routingContext.request().getParam("operator");
							 String msisdn = routingContext.request().getParam("msisdn");
	                    	 HttpServerResponse response = routingContext.response();
	                      //  response.setChunked(true);
	                         String JsonResponse = "{\n" + 
	                         		"  \"getSubscriberTypeResponse\": {\n" + 
	                         		"    \"msisdn\": \""+operator+"\",\n" +
	                         		"    \"imsi\": \""+msisdn+"\",\n" +
	                         		"    \"userType\": \"postpaid\",\n" + 
	                         		"    \"userStatus\": \"Active\",\n" + 
	                         		"    \"mvno\": \"No\"\n" + 
	                         		"  }\n" + 
	                         		"}";  

	        	 	                       
	        	 	                      vertx.setTimer(TimeUnit.MILLISECONDS.toMillis(1000), l -> {
	        	 	                    	  json = new JsonObject(JsonResponse);
	             	                         response.putHeader("Content-Type", "application/json; charset=UTF8")
	             	                         .setStatusCode(200);
	             	                        response.end(Json.encodePrettily(json));
	             	                       LOGGER.info("----- called getsubscribertype on port"+port);
	         	                          });
	                    });
        httpServer
                .requestHandler(router::accept)
                .listen(port);

    }
}
