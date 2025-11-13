package mocker.action

import framework.action.Action
import framework.action.ActionResponse
import framework.action.HttpAction
import framework.constants.HttpMethod
import framework.constants.HttpStatus
import framework.dto.Request
import framework.component.ComponentProvider
import mocker.dto.response.TokenDto
import mocker.dto.request.UserSignInRequestDto
import mocker.dto.request.UserSignUpRequestDto
import mocker.service.UserService

private val userService = ComponentProvider.get(UserService::class.java)

@HttpAction(
    method = [HttpMethod.POST],
    path = "/users"
)
class SignUp : Action<UserSignUpRequestDto, TokenDto> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: UserSignUpRequestDto
    ): ActionResponse<TokenDto> {
        val response = userService.signUp(body)

        return ActionResponse(status = HttpStatus.CREATED, body = response)
    }
}

@HttpAction(
    method = [HttpMethod.GET],
    path = "/users"
)
class SignIn : Action<UserSignInRequestDto, TokenDto> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: UserSignInRequestDto
    ): ActionResponse<TokenDto> {
        val response = userService.signIn(body)

        return ActionResponse(body = response)
    }
}
