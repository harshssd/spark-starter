package com.hhh.spark.starter;

import com.hhh.spark.starter.model.SimpleTaskDAO;
import com.hhh.spark.starter.model.Task;
import com.hhh.spark.starter.model.TaskDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.modelAndView;
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

        // Filters that are processed before any requests.
        before((request, response) -> {
            // Using request attributes to store the values.
            if(request.cookie("username") != null) {
                request.attribute("username", request.cookie("username"));
            }
        });

        before("/tasks", (request, response) -> {
            if(request.attribute("username") == null) {
                response.redirect("/");
                halt();
            }
        });

        get("/", (request, response) -> {
            Map<String, String> model = new HashMap();
            model.put("username", request.attribute("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", ((request, response) -> {
            Map<String, String> model = new HashMap();
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            response.redirect("/");
            return new ModelAndView(model, "sign-in.hbs");
        }), new HandlebarsTemplateEngine());

        get("/tasks", (request, response) -> {
            Map<String, Object> model = new HashMap();
            model.put("tasks", taskDAO.findAll());
            return modelAndView(model, "tasks.hbs");
        }, new HandlebarsTemplateEngine());

        post("/tasks", ((request, response) -> {
            String title = request.queryParams("title");
            String description = request.queryParams("description");
            String status = "NEW";
            Task task = new Task(title, description, status);
            taskDAO.add(task);
            response.redirect("/tasks");
            return null;
        }));
    }
}
