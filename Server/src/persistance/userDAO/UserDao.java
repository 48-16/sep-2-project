package persistance.userDAO;

import dtos.model.User;

public interface UserDao {
    public User getUserById(int id);
    public User getUserByUsername(String username);
    public void createUser(User user);
    public void updateUser(User user);
    public void deleteUser(int id);
}
