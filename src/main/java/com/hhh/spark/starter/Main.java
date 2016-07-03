package com.hhh.spark.starter;

import com.hhh.spark.starter.model.NotFoundException;
import com.hhh.spark.starter.model.SimpleTaskDAO;
import com.hhh.spark.starter.model.Task;
import com.hhh.spark.starter.model.TaskDAO;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.before;
import static spark.Spark.exception;
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

    private static final String FLASH_MESSAGE_KEY = "flash_message";

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
                setFlashMessage(request, "Oops!! Please sign in first!!");
                response.redirect("/");
                halt();
            }
        });

        get("/", (request, response) -> {
            Map<String, String> model = new HashMap();
            model.put("username", request.attribute("username"));
            model.put("flashMessage", captureFlashMessage(request));
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
            model.put("flashMessage", captureFlashMessage(request));
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

        get("tasks/:slug", (request, response) -> {
            Map<String, Object> model = new HashMap();
            model.put("task", taskDAO.findBySlug(request.params("slug")));
            return new ModelAndView(model, "task.hbs");
        }, new HandlebarsTemplateEngine());

        post("/tasks/:slug/vote", (request, response) -> {
            Task task = taskDAO.findBySlug(request.params("slug"));
            boolean voted = task.addVoter(request.attribute("username"));
            if(voted) {
                setFlashMessage(request, "Thanks for your vote!");
            } else {
                setFlashMessage(request, "Sorry, you've already Voted!!");
            }
            response.redirect("/tasks");
            return null;
        });

        exception(NotFoundException.class, ((exception, request, response) -> {
            response.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            response.body(html);
        }));
    }

    private static void setFlashMessage(Request request, String message)
    {
        request.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request request)
    {
        if(request.session(false) == null) return null;
        if(!request.session().attributes().contains(FLASH_MESSAGE_KEY)) return null;
        return (String) request.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request request)
    {
        String message = getFlashMessage(request);
        if(message != null) {
            request.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}
