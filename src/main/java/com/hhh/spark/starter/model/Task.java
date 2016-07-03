package com.hhh.spark.starter.model;

/**
 * @Author Harsha
 *
 * This represents a Task.
 */
public class Task
{
    private String title;
    private String description;
    private String status;

    public Task(String title, String description, String status)
    {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!title.equals(task.title)) return false;
        if (!description.equals(task.description)) return false;
        return status.equals(task.status);

    }

    @Override
    public int hashCode()
    {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
