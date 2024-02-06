package it.live.brainbox.service;

import it.live.brainbox.entity.User;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.payload.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserService {


    ResponseEntity<ApiResponse> updateUser(Long userId, UserDTO userDTO , Integer addCoin);

    User getUserById(Long userId);

    Page<?> getAllUserByPage(int page, int size);

    ResponseEntity<ApiResponse> deleteMyAccount();

}
