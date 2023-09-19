package com.digitalvisionea.profiletasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.ElementCollection;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRespository UserRespository;

    User loggedInUser;

    private void middleWare(String authorization){
        //get jwt token from basic auth authorization (Bearer <token>)
        String token = authorization.substring(7).trim();
      
        //get user from token
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        String email = jwtTokenUtil.getUsernameFromToken(token);

        List<User> users = UserRespository.findByEmail(email);
        if(users.size()==0){
            throw new IllegalArgumentException("User is not authorized");
        }
        User loggedInUser = users.get(0);

        //validate token
        if(!checkToken(loggedInUser, token)){
            throw new IllegalArgumentException("Token is not valid");
        }

        this.loggedInUser = loggedInUser;
    }

    private boolean checkToken(User user,String token){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        return jwtTokenUtil.validateToken(token, user);
    }

    @GetMapping("/task")
    @ElementCollection
    public List<Task> index(){
        return taskRepository.findAll();
    }

    @GetMapping("/task/{id}")
    public Task show(@PathVariable String id){
        int taskId = Integer.parseInt(id);
        return  taskRepository.findById(taskId).get();
    }

    @PostMapping("/task/get")
    public Task getTask(@RequestBody Map<String, String> body){
        int taskId = Integer.parseInt(body.get("id"));
        //log the variable body to the console
        System.out.println(body);
        return  taskRepository.findById(taskId).get();
    }

    @PostMapping("/task/search")
    @ElementCollection
    public List<Task> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return taskRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    @PostMapping("/task")
    public Task create(@RequestBody Map<String, String> body){
        String title = body.get("title");
        String content = body.get("content");
        return taskRepository.save(new Task(title, content, 0,0,0));
    }

    @PutMapping("/task/{id}")
    public Task update(@PathVariable String id, @RequestBody Map<String, String> body){
        int taskId = Integer.parseInt(id);
        // getting task
        Task task = taskRepository.findById(taskId).get();
        task.setTitle(body.get("title"));
        task.setContent(body.get("content"));
        return taskRepository.save(task);
    }

    @DeleteMapping("task/{id}")
    public boolean delete(@PathVariable String id){
        int taskId = Integer.parseInt(id);
        taskRepository.deleteById(taskId);
        return true;
    }

    @PostMapping("/task/get-all")
    @ElementCollection
    public List<Task> getAll(@RequestHeader Map<String, String> headers){
        //read authentication token from headers
        String authorization = headers.get("authorization");

        //load middleware
        middleWare(authorization);

        return taskRepository.findAll();
    }


    @GetMapping("/task/user/{userId}")
    @ElementCollection
    public List<Task> getTasksByUserId(@PathVariable String userId) {
        try {
            int userIdInt = Integer.parseInt(userId);
            // Find tasks by user ID
            return taskRepository.findByUserId(userIdInt);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid user ID format");
        } catch (Exception e) {
            throw new CustomException(500, "Internal Server error");
        }
    }


    @PostMapping("/task/create")
    public Task createTask(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);


        String title = body.get("title");
        String content = body.get("content");
        int status = Integer.parseInt(body.get("status"));
        int priority = Integer.parseInt(body.get("priority"));
        int userId = Integer.parseInt(body.get("userId"));
        //set date to now

        Date modifiedOn = new Date();
        int modifiedBy = this.loggedInUser.getId();
        return taskRepository.save(new Task(title, content,status,priority,userId,modifiedOn,modifiedBy));
    }
@PostMapping("/task/update")
public Task updateTask(@RequestBody Map<String, String> body) {
    try {
        int taskId = Integer.parseInt(body.get("id"));
        // getting task
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            throw new CustomException(404, "Task not found");
        }

        task.setTitle(body.get("title"));
        task.setContent(body.get("content"));
        return taskRepository.save(task);
    } catch (NumberFormatException e) {
        throw new CustomException(400, "Invalid task ID format");
    } catch (Exception e) {
        throw new CustomException(500, "Internal Server error");
    }
}

    @PostMapping("task/delete")
    public Task deleteTask(@RequestBody Map<String, String> body){
        int taskId = Integer.parseInt(body.get("id"));

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            throw new CustomException(404, "Task not found");
        } else {
            taskRepository.deleteById(taskId);
            return task;
        }
    }

}