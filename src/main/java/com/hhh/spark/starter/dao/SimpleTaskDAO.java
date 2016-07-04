package com.hhh.spark.starter.dao;

import com.hhh.spark.starter.exceptions.NotFoundException;
import com.hhh.spark.starter.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Harsha
 *
 * Simple Implementation of Task DAO.
 */
public class SimpleTaskDAO implements TaskDAO
{
    private List<Task> tasks;

    public SimpleTaskDAO()
    {
        this.tasks = new ArrayList<>();
    }

    @Override
    public boolean add(Task task)
    {
        return tasks.add(task);
    }

    @Override
    public List<Task> findAll()
    {
        return new ArrayList<>(this.tasks);
    }

    @Override
    public Task findBySlug(String slug)
    {
        return tasks.stream()
                .filter(task -> task.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
