package com.pankaj.todotask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pankaj.todotask.controller.TaskController;
import com.pankaj.todotask.entity.Task;
import com.pankaj.todotask.service.TaskService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setup() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Task\", \"description\": \"Test Description\", \"dueDate\": \"2025-02-01\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(task.getDueDate().toString()))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()));
    }

    @Test
    void testGetTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setDueDate(LocalDate.now().plusDays(10));
        updatedTask.setCompleted(true);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"dueDate\": \"2025-02-10\", \"completed\": true }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
