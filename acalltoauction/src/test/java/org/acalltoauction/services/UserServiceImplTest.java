package org.acalltoauction.services;

import org.acalltoauction.data.models.Duration;
import org.acalltoauction.data.repositories.UserRepository;
import org.acalltoauction.dto.requests.*;
import org.acalltoauction.dto.response.*;
import org.acalltoauction.exceptions.InvalidCredentials;
import org.acalltoauction.exceptions.LotAlreadyExist;
import org.acalltoauction.exceptions.UserAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @Test
    public void UserCanSignUpTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword("password");
        userRequest.setNin("12345672345");
        UserSignUpResponse response = userService.signUp(userRequest);
        assertThat(response,notNullValue());
        UserDeleteRequest userDeleteRequest = new UserDeleteRequest();
        userDeleteRequest.setEmail("test@acalltoauction.com");
        userDeleteRequest.setPassword("password");
        UserDeleteResponse userDeleteResponse = userService.deleteUser(userDeleteRequest);
        assertThat(userDeleteResponse,notNullValue());

    }
    @Test
    public void UserCanLoginTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword("password");
        userRequest.setNin("12345672345");
        UserSignUpResponse response = userService.signUp(userRequest);
        assertEquals("SignUp Successful", response.getMessage());
        assertThat(response,notNullValue());
        assertEquals(1,userRepository.count());
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("test@acalltoauction.com");
        userLoginRequest.setPassword("password");
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        assertThat(userLoginResponse,notNullValue());
        UserDeleteRequest userDeleteRequest = new UserDeleteRequest();
        userDeleteRequest.setEmail("test@acalltoauction.com");
        userDeleteRequest.setPassword("password");
        UserDeleteResponse userDeleteResponse = userService.deleteUser(userDeleteRequest);
        assertThat(userDeleteResponse,notNullValue());
    }
    @Test
    public void DuplicateUserCanNotBeCreatedTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword("password");
        userRequest.setNin("12345672345");
        UserSignUpResponse response = userService.signUp(userRequest);
        assertThat(response,notNullValue());
        UserSignUpRequest secondUserRequest = new UserSignUpRequest();
        secondUserRequest.setEmail("test@acalltoauction.com");
        secondUserRequest.setPassword("password");
        secondUserRequest.setNin("12345672345");
        assertThrows( UserAlreadyExistException.class, ()-> userService.signUp(secondUserRequest));
        UserDeleteRequest userDeleteRequest = new UserDeleteRequest();
        userDeleteRequest.setEmail("test@acalltoauction.com");
        userDeleteRequest.setPassword("password");
        UserDeleteResponse userDeleteResponse = userService.deleteUser(userDeleteRequest);
        assertThat(userDeleteResponse,notNullValue());

    }
    @Test
    public void UserCanNotSignUpWithAnEmptyEmailTest() {
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("  ");
        userRequest.setPassword("password");
        userRequest.setNin("12345672345");
        assertThrows(NullPointerException.class, () -> userService.signUp(userRequest));
    }
    @Test
    public void UserCanNotSignUpWithAnEmptyPasswordTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword(" ");
        userRequest.setNin("12345672345");
        assertThrows( NullPointerException.class, ()-> userService.signUp(userRequest));
    }
    @Test
    public void UserCanNotSignupWithAnEmptyNinTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword("password");
        userRequest.setNin("  ");
        assertThrows( NullPointerException.class, ()-> userService.signUp(userRequest));

    }
    @Test
    public void UserCannotLoginWithAnInvalidCredentialsTest(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@acalltoauction.com");
        userRequest.setPassword("password");
        userRequest.setNin("12345672345");
        UserSignUpResponse response = userService.signUp(userRequest);
        assertThat(response,notNullValue());
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("test@acalltoauction.com");
        userLoginRequest.setPassword("invalidPassword");
        assertThrows(InvalidCredentials.class,()->userService.login(userLoginRequest));
        UserDeleteRequest userDeleteRequest = new UserDeleteRequest();
        userDeleteRequest.setEmail("test@acalltoauction.com");
        userDeleteRequest.setPassword("password");
        UserDeleteResponse userDeleteResponse = userService.deleteUser(userDeleteRequest);
        assertThat(userDeleteResponse,notNullValue());

    }
    @Test
    public void UserCanSignUpAndCreateALot_Test(){
//        UserSignUpRequest userRequest = new UserSignUpRequest();
//        userRequest.setEmail("test@acalltoauction.com");
//        userRequest.setPassword("password");
//        userRequest.setNin("12345672345");
//        UserSignUpResponse response = userServiceImpl.signUp(userRequest);
//        assertThat(response,notNullValue());

        LotCreationRequest lotRequest = new LotCreationRequest();
        lotRequest.setLotName("myNewBag");
        lotRequest.setDescription("BIG Blue dark bag");
        lotRequest.setImageUrl("image.url");
        LotCreationResponse lotCreationResponse = userService.createLot(lotRequest);
        assertThat(lotCreationResponse,notNullValue());

        DeleteLotRequest deleteLotRequest = new DeleteLotRequest();
        deleteLotRequest.setLotName("myNewBag");
        DeleteLotResponse userDeleteResponse = userService.deleteLot(deleteLotRequest);
        assertThat(userDeleteResponse,notNullValue());
    };
    @Test
    public void userCanNotCreateDuplicate_Lot(){
        LotCreationRequest lotRequest = new LotCreationRequest();
        lotRequest.setLotName("myNewBag");
        lotRequest.setDescription("BIG Blue dark bag");
        lotRequest.setImageUrl("image.url");
        LotCreationResponse lotCreationResponse = userService.createLot(lotRequest);
        assertThat(lotCreationResponse,notNullValue());
        LotCreationRequest secondLotRequest = new LotCreationRequest();
        secondLotRequest.setLotName("myNewBag");
        secondLotRequest.setDescription("BIG Blue dark bag");
        secondLotRequest.setImageUrl("image.url");
        assertThrows(LotAlreadyExist.class,()->userService.createLot(secondLotRequest));
        DeleteLotRequest deleteLotRequest = new DeleteLotRequest();
        deleteLotRequest.setLotName("myNewBag");
        DeleteLotResponse userDeleteResponse = userService.deleteLot(deleteLotRequest);
        assertThat(userDeleteResponse,notNullValue());
    }
    @Test
    public void UserCanCheckTheStatusOf_A_Lot_Test(){
        LotCreationRequest lotRequest = new LotCreationRequest();
        lotRequest.setLotName("myNewBag");
        lotRequest.setDescription("BIG Blue dark bag");
        lotRequest.setImageUrl("image.url");
        LotCreationResponse lotCreationResponse = userService.createLot(lotRequest);
        assertThat(lotCreationResponse,notNullValue());
        LotStatusRequest lotStatusRequest = new LotStatusRequest();
        lotStatusRequest.setLotName("myNewBag");
        LotStatusResponse lotStatusResponse =  userService.checkStatus(lotStatusRequest);
        assertThat(lotStatusResponse,notNullValue());
        DeleteLotRequest deleteLotRequest = new DeleteLotRequest();
        deleteLotRequest.setLotName("myNewBag");
        DeleteLotResponse userDeleteResponse = userService.deleteLot(deleteLotRequest);
        assertThat(userDeleteResponse,notNullValue());

    }
    @Test
    public void UserCanCreateAnAuction(){

        LotCreationRequest lotRequest = new LotCreationRequest();
        lotRequest.setLotName("myNewBag");
        lotRequest.setDescription("BIG Blue dark bag");
        lotRequest.setImageUrl("image.url");
        LotCreationResponse lotCreationResponse = userService.createLot(lotRequest);
        assertThat(lotCreationResponse,notNullValue());

        AuctionRequest auctionRequest = new AuctionRequest();
        auctionRequest.setDate("2025-04-29");
        auctionRequest.setStartingBidPrice("2000");
        auctionRequest.setDuration(Duration.THIRTY_SECONDS);
        auctionRequest.setTitle("a big bag");
        auctionRequest.setLotName("myNewBag");
        AuctionResponse auctionResponse = userService.createAuction(auctionRequest);
        assertThat(auctionResponse,notNullValue());
        DeleteAuctionRequest deleteAuctionRequest = new DeleteAuctionRequest();
        deleteAuctionRequest.setAuctionTitle("myNewBag");
        DeleteAuctionResponse auctionDeleteResponse = userService.deleteAuction(deleteAuctionRequest);
        assertThat(auctionDeleteResponse,notNullValue());
        DeleteLotRequest deleteLotRequest = new DeleteLotRequest();
        deleteLotRequest.setLotName("myNewBag");
        DeleteLotResponse userDeleteResponse = userService.deleteLot(deleteLotRequest);
        assertThat(userDeleteResponse,notNullValue());
    }
    
    @Test
    public void passTest(){

    }

}