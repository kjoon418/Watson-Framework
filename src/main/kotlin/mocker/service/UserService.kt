package mocker.service

import mocker.dto.request.UserSignInRequestDto
import mocker.dto.request.UserSignUpRequestDto
import mocker.dto.response.TokenDto

interface UserService {
    fun signUp(requestDto: UserSignUpRequestDto): TokenDto

    fun signIn(requestDto: UserSignInRequestDto): TokenDto
}
