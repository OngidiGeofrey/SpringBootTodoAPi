package com.digitalvisionea.profiletasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.ElementCollection;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
public class UserController {

    @Autowired
    UserRespository UserRespository;

    User loggedInUser;


    @PostMapping("/user/get")
    public User getUser(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);

        
        int UserId = Integer.parseInt(body.get("id"));
        //log the variable body to the console
        System.out.println(body);
        return  UserRespository.findById(UserId).get();
    }

    @PostMapping("/user/search")
    @ElementCollection
    public List<User> search(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);

        
        String searchTerm = body.get("text");
        return UserRespository.findByFirstNameOrLastNameOrEmailOrPhoneNumberContaining(searchTerm, searchTerm, searchTerm, searchTerm);
    }

    @PostMapping("/user/get-all")
    @ElementCollection
    public List<User> getAll(@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);

        
        return UserRespository.findAll();
    }


    @PostMapping("/register")
    public User registerUser(@RequestBody Map<String, String> body){
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String email = body.get("email");
        //validate email    
        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(email);
        if(!m.matches()){
            throw new IllegalArgumentException("Email is not valid");
        }
        //check if email is in use
        List<User> users = UserRespository.findByEmail(email);
        if(users.size()>0){
            throw new IllegalArgumentException("Email is already in use");
        }
        String phoneNumber = body.get("phoneNumber");
        //validate phone
        p = Pattern.compile("^[0-9]{10}$");
        m = p.matcher(phoneNumber);
        if(!m.matches()){
            throw new IllegalArgumentException("Phone Number is not valid");
        }
        //check if phone is in use
        users = UserRespository.findByPhoneNumber(phoneNumber);
        if(users.size()>0){
            throw new IllegalArgumentException("Phone Number is already in use");
        }
        String password = body.get("password");
        //validate password
        p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        m = p.matcher(password);
        if(!m.matches()){
            throw new IllegalArgumentException("Password is not valid. Must contain at least 8 characters, one uppercase, one lowercase, one number, and one special character");
        }
        User user = new User();
        try {
            return UserRespository.save(new User(firstName, lastName, email, phoneNumber, password));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return user;
        }
    }


    @PostMapping("/user/create")
    public User createUser(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);

        
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String email = body.get("email");
        //validate email    
        Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(email);
        if(!m.matches()){
            throw new IllegalArgumentException("Email is not valid");
        }
        //check if email is in use
        List<User> users = UserRespository.findByEmail(email);
        if(users.size()>0){
            throw new IllegalArgumentException("Email is already in use");
        }
        String phoneNumber = body.get("phoneNumber");
        //validate phone
        p = Pattern.compile("^[0-9]{10}$");
        m = p.matcher(phoneNumber);
        if(!m.matches()){
            throw new IllegalArgumentException("Phone Number is not valid");
        }
        //check if phone is in use
        users = UserRespository.findByPhoneNumber(phoneNumber);
        if(users.size()>0){
            throw new IllegalArgumentException("Phone Number is already in use");
        }
        String password = body.get("password");
        //validate password
        p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        m = p.matcher(password);
        if(!m.matches()){
            throw new IllegalArgumentException("Password is not valid. Must contain at least 8 characters, one uppercase, one lowercase, one number, and one special character");
        }
        User user = new User();
        try {
            return UserRespository.save(new User(firstName, lastName, email, phoneNumber, password));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return user;
        }
    }

    private boolean checkToken(User user,String token){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        return jwtTokenUtil.validateToken(token, user);
    }

    //add authentication
    @PostMapping("/user/login")
    public User login(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String password = body.get("password");
        List<User> users = UserRespository.findByEmail(email);
        if(users.size()==0){
            throw new IllegalArgumentException("Email is not registered");
        }
        User user = users.get(0);
        try {
            if(user.authenticate(email, password)){
                //genetrating token
                JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
                String token = jwtTokenUtil.generateToken(user);
                user.setToken(token);
                //save user
                UserRespository.save(user);
                return user;
            }
            else{
                throw new IllegalArgumentException("Password is incorrect");
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return user;
        }
    }

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

    @PostMapping("/user/update")
    public User updateUser(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);



        int UserId = Integer.parseInt(body.get("id"));
        // getting User
        User User = UserRespository.findById(UserId).get();


        

        if(body.get("firstName")!="" && body.get("firstName")!=null){
            User.setFirstName(body.get("firstName"));
        }
        if(body.get("lastName")!="" && body.get("lastName")!=null){
            User.setLastName(body.get("lastName"));
        }
        if(body.get("email")!="" && body.get("email")!=null){
            String email = body.get("email");
            Pattern p = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
            Matcher m = p.matcher(email);
            if(!m.matches()){
                throw new IllegalArgumentException("Email is not valid");
            }
            User.setEmail(email);
        }
        if(body.get("phoneNumber")!="" && body.get("phoneNumber")!=null){
            String phoneNumber = body.get("phoneNumber");
            Pattern p = Pattern.compile("^[0-9]{10}$");
            Matcher m = p.matcher(phoneNumber);
            if(!m.matches()){
                throw new IllegalArgumentException("Phone Number is not valid");
            }
            User.setPhoneNumber(body.get("phoneNumber"));
        }

        if(body.get("password")!="" && body.get("password")!=null){
            try {
                User.setPassword(body.get("password"));
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return UserRespository.save(User);
    }

    @PostMapping("User/delete")
    public boolean deleteUser(@RequestBody Map<String, String> body,@RequestHeader Map<String, String> headers){

        //read authentication token from headers
        String authorization = headers.get("authorization");
        
        //load middleware
        middleWare(authorization);


        int UserId = Integer.parseInt(body.get("id"));
        UserRespository.deleteById(UserId);
        return true;
    }

}