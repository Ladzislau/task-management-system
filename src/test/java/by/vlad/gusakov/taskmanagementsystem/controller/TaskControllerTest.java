package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.*;
import by.vlad.gusakov.taskmanagementsystem.service.CommentService;
import by.vlad.gusakov.taskmanagementsystem.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    private final static String TASKS_ENDPOINT = "/api/tasks";
    private final static String TASK_BY_ID_ENDPOINT = "/api/tasks/1";
    private final static String TASK_COMMENTS_ENDPOINT = "/api/tasks/1/comments";
    private final static String TASK_COMMENT_BY_ID_ENDPOINT = "/api/tasks/1/comments/1";
    private final static String TASKS_BY_USER_ID_ENDPOINT = "/api/tasks/user-tasks/1";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void getUserTasks_validRequest_200TaskPageResponseReturned() throws Exception {
        TaskPageResponse taskPageResponse = TestDataFactory.createTaskPageResponse();

        when(taskService.getUserTasks(any(), any(), any())).thenReturn(taskPageResponse);

        mockMvc.perform(get(TASKS_BY_USER_ID_ENDPOINT)
                        .param("page", "0")
                        .param("size", "10")
                        .param("filterRole", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", notNullValue()))
                .andExpect(jsonPath("$.totalPage", notNullValue()))
                .andExpect(jsonPath("$.totalElements", notNullValue()))
                .andExpect(jsonPath("$.tasks", notNullValue()));
    }

    @Test
    @WithMockUser
    void getTasksById_validRequest_200TaskResponseReturned() throws Exception {
        TaskResponse taskResponse = TestDataFactory.createTaskResponse();

        when(taskService.getTaskById(any())).thenReturn(taskResponse);

        mockMvc.perform(get(TASK_BY_ID_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task", notNullValue()));
    }

    @Test
    @WithMockUser
    void createTask_validRequest_200PublicationCreated() throws Exception {
        CreateTaskRequest createTaskRequest = TestDataFactory.createCreateTaskRequest();
        CreateTaskResponse createTaskResponse = TestDataFactory.createCreateTaskResponse();

        when(taskService.createTask(any())).thenReturn(createTaskResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, TASKS_ENDPOINT, createTaskRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.createdTask", notNullValue()));
    }

    @Test
    @WithMockUser
    void createTask_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         *  title is invalid
         *  priority is invalid
         */
        CreateTaskRequest createTaskRequest = new CreateTaskRequest(
                "", "test", null, 1L);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, TASKS_ENDPOINT, createTaskRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(2))));
    }

    @Test
    @WithMockUser
    void updateTask_validRequest_200PublicationUpdated() throws Exception {
        UpdateTaskRequest updateTaskRequest = TestDataFactory.createUpdateTaskRequest();

        UpdateTaskResponse updateTaskResponse = TestDataFactory.createUpdateTaskResponse();

        when(taskService.updateTask(any(), any())).thenReturn(updateTaskResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, TASK_BY_ID_ENDPOINT, updateTaskRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.updatedTask", notNullValue()));
    }

    @Test
    @WithMockUser
    void updateTask_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         *  title is invalid
         */
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(
                new String(new char[10000]), "test", null, null, 2L);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, TASK_BY_ID_ENDPOINT, updateTaskRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(1))));
    }

    @Test
    @WithMockUser
    void deleteTask_validRequest_200PublicationDeleted() throws Exception {
        DeleteTaskResponse deleteTaskResponse = TestDataFactory.createDeleteTaskResponse();

        when(taskService.deleteTaskById(any())).thenReturn(deleteTaskResponse);

        mockMvc.perform(delete(TASK_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @WithMockUser
    void getCommentById_validRequest_200CommentListResponseReturned() throws Exception {
        CommentResponse commentResponse = TestDataFactory.createCommentResponse();

        when(commentService.getCommentById(any(), any())).thenReturn(commentResponse);

        mockMvc.perform(get(TASK_COMMENT_BY_ID_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", notNullValue()));
    }

    @Test
    @WithMockUser
    void getTaskComments_validRequest_200CommentPageResponseReturned() throws Exception {
        CommentPageResponse commentPageResponse = TestDataFactory.createCommentPageResponse();

        when(commentService.getCommentsByTask(any(), any())).thenReturn(commentPageResponse);

        mockMvc.perform(get(TASK_COMMENTS_ENDPOINT)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", notNullValue()))
                .andExpect(jsonPath("$.totalPage", notNullValue()))
                .andExpect(jsonPath("$.totalElements", notNullValue()))
                .andExpect(jsonPath("$.comments", notNullValue()));
    }

    @Test
    @WithMockUser
    void postComment_validRequest_200CommentPosted() throws Exception {
        CreateCommentRequest createCommentRequest = TestDataFactory.createCreateCommentRequest();
        CreateCommentResponse createCommentResponse = TestDataFactory.createCreateCommentResponse();

        when(commentService.postComment(any(), any())).thenReturn(createCommentResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, TASK_COMMENTS_ENDPOINT, createCommentRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.createdComment", notNullValue()));
    }

    @Test
    @WithMockUser
    void postComment_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         * text is invalid
         */
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, TASK_COMMENTS_ENDPOINT, createCommentRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(1))));
    }

    @Test
    @WithMockUser
    void updateComment_validRequest_200CommentUpdated() throws Exception {
        UpdateCommentRequest updateCommentRequest = TestDataFactory.createUpdateCommentRequest();
        UpdateCommentResponse updateCommentResponse = TestDataFactory.createUpdateCommentResponse();

        when(commentService.updateComment(any(), any(), any())).thenReturn(updateCommentResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, TASK_COMMENT_BY_ID_ENDPOINT, updateCommentRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.updatedComment", notNullValue()));
    }

    @Test
    @WithMockUser
    void updateComment_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         * text is invalid
         */
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(null);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, TASK_COMMENT_BY_ID_ENDPOINT, updateCommentRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(1))));
    }

    @Test
    @WithMockUser
    void deleteComment_validRequest_200CommentDeleted() throws Exception {
        DeleteCommentResponse deleteCommentResponse = TestDataFactory.createDeleteCommentResponse();

        when(commentService.deleteComment(any())).thenReturn(deleteCommentResponse);

        mockMvc.perform(delete(TASK_COMMENT_BY_ID_ENDPOINT).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }


    private MockHttpServletRequestBuilder prepareRequest(HttpMethod httpMethod, String endpoint, Object object) throws Exception {
        String requestBody = objectMapper.writeValueAsString(object);

        if (httpMethod.equals(POST)) {
            return post(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody);
        } else if (httpMethod.equals(PATCH)) {
            return patch(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody);
        } else {
            throw new UnsupportedOperationException("Unsupported HTTP method");
        }
    }

}