package com.hhh.spark.starter.models;

import com.github.slugify.Slugify;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "tasks")
public class Task
{
    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String status;

    @Id
    private String slug;

    @Column
    @ElementCollection
    private Set<String> voters;

    // Default constructor for JPA
    public Task() {}

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

    public Task(TaskBuilder taskBuilder)
    {
        this.title = taskBuilder.title;
        this.description = taskBuilder.description;
        this.status = taskBuilder.status;
        this.slug = taskBuilder.slug;
        this.voters = taskBuilder.voters;
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

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setSlug(String slug)
    {
        this.slug = slug;
    }

    public void setVoters(Set<String> voters)
    {
        this.voters = voters;
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

    public class TaskBuilder {
        private String title;
        private String description;
        private String status;
        private String slug;
        private Set<String> voters;

        public TaskBuilder() {}

        public TaskBuilder withTitle(String title)
        {
            this.title = title;
            return this;
        }

        public TaskBuilder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public TaskBuilder withStatus(String status)
        {
            this.status = status;
            return this;
        }

        public TaskBuilder withSlug(String slug)
        {
            this.slug = slug;
            return this;
        }

        public TaskBuilder withVoters(Set<String> voters)
        {
            this.voters = voters;
            return this;
        }

        public Task build()
        {
            return new Task(this);
        }
    }

}
