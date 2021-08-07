package ir.roocket.sinadalvand.todo.data.local

import ir.roocket.sinadalvand.todo.data.model.User
import ir.roocket.sinadalvand.todo.data.persistence.dao.UserDao

/**
 * save token into database
 * @property userDao UserDao
 * @constructor
 */
class DbValutor(val userDao: UserDao) : Valutor {

    override suspend fun saveUser(user: User) {
        userDao.save(user)
    }

    override suspend fun getUser(): User? {
        return userDao.getUser()
    }

    override suspend fun removeUser() {
        return userDao.delete()
    }

}