package mocker.service

import framework.component.Component
import framework.exception.IllegalRequestException
import framework.security.PasswordEncoder
import framework.security.Role
import framework.security.createToken
import mocker.dto.request.UserSignInRequestDto
import mocker.dto.request.UserSignUpRequestDto
import mocker.dto.response.TokenDto
import mocker.entity.User
import mocker.repository.userRepository

@Component
class UserServiceImpl : UserService {
    override fun signUp(requestDto: UserSignUpRequestDto): TokenDto {
        val user = User(
            key = requestDto.key,
            id = requestDto.id,
            password = PasswordEncoder.encode(requestDto.password)
        )

        userRepository.save(user)

        return TokenDto(createToken(user.key, listOf(Role.USER)))
    }

    override fun signIn(requestDto: UserSignInRequestDto): TokenDto {
        val user = userRepository.findAll()
            .firstOrNull { it.id == requestDto.id && PasswordEncoder.isMatch(requestDto.password, it.password) }
            ?: throw IllegalRequestException("부적절한 아이디 혹은 비밀번호입니다.")

        return TokenDto(createToken(user.key, listOf(Role.USER)))
    }
}
