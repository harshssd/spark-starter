package com.hhh.spark.starter.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String slug;
    private Set<String> voters;

    public Task(String title, String description, String status)
    {
        this.voters = new HashSet<>();
        this.title = title;
        this.description = description;
        this.status = status;
        try {
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSlug()
    {
        return slug;
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

    public boolean addVoter(String voterUsername)
    {
        return voters.add(voterUsername);
    }

    public int getVoteCount()
    {
        return voters.size();
    }

    public List<String> getVoters()
    {
        return new ArrayList<>(voters);
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
