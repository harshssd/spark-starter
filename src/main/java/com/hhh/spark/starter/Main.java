package com.hhh.spark.starter;

import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Harsha
 */
public class Main
{
    public static void main(String[] args)
    {
        Spark.get("/", (request, response) -> {
            Map<String, String> model = new HashMap<String, String>();
            model.put("username", request.cookie("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        Spark.post("/sign-in", ((request, response) -> {
            Map<String, String> model = new HashMap<String, String>();
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model, "sign-in.hbs");
        }), new HandlebarsTemplateEngine());
    }
}
