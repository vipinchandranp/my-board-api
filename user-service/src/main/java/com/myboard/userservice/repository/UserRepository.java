package com.myboard.userservice.repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Define custom methods for user data access
    // ...
}

