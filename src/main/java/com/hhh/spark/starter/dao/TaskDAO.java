package com.hhh.spark.starter.dao;

import com.hhh.spark.starter.models.Task;

import java.util.List;

/**
 * Task Data Access Object.
 */
public interface TaskDAO
{
    boolean add(Task task);

    List<Task> findAll();

    Task findBySlug(String slug);
}
