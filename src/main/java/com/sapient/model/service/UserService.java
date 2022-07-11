package com.sapient.model.service;

import com.sapient.exception.UserNotFoundException;
import com.sapient.exception.UsernameAlreadyTakenException;
import com.sapient.model.beans.User;
import com.sapient.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    public User createUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            Optional<String> phoneNumber) throws UsernameAlreadyTakenException {
        if (usernameTaken(username)) {
            throw new UsernameAlreadyTakenException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        try {
            user.setPhoneNumber(phoneNumber.get());
        } catch (Exception e) {}
        userDao.save(user);
        return user;
    }

    public void deleteUser(String passwordHash) throws UserNotFoundException {
        for(User user: userDao.findAll()){
            if(user.getPasswordHash().equals(passwordHash)){
                userDao.delete(user);
                return;
            }
        }
        throw new UserNotFoundException();
    }

    public User getUser(Integer id) throws UserNotFoundException {
        User user = userDao.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException();
        }
        return user;
    }

    public Boolean userExists(Integer id){
        try{
            userDao.findById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Boolean usernameTaken(String username) {
        for(User user: userDao.findAll()){
            if(username.equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    public User getByUsername(String username) throws UserNotFoundException {
        for (User user : userDao.findAll()) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }
}
