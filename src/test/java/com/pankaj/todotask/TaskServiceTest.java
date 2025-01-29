package com.pankaj.todotask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pankaj.todotask.entity.Task;
import com.pankaj.todotask.repository.TaskRepository;
import com.pankaj.todotask.service.TaskService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals(task.getTitle(), createdTask.getTitle());
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        var tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);
        assertNotNull(foundTask);
        assertEquals(task.getTitle(), foundTask.getTitle());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        task.setTitle("Updated Task");
        Task updatedTask = taskService.updateTask(1L, task);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTitle());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }
}

