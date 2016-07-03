package com.hhh.spark.starter;

import com.hhh.spark.starter.model.SimpleTaskDAO;
import com.hhh.spark.starter.model.TaskDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

/**
 * @Author Harsha
 *
 * Main method which runs the application
 */
public class Main
{
    public static void main(String[] args)
    {
        staticFileLocation("/public");

        TaskDAO taskDAO = new SimpleTaskDAO();

        get("/", (request, response) -> {
            Map<String, String> model = new HashMap<String, String>();
            model.put("username", request.cookie("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", ((request, response) -> {
            Map<String, String> model = new HashMap<String, String>();
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model, "sign-in.hbs");
        }), new HandlebarsTemplateEngine());
    }
}
