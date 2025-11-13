package mocker.action

import framework.action.Action
import framework.action.ActionResponse
import framework.action.HttpAction
import framework.constants.HttpMethod
import framework.dto.Request
import framework.security.RequireAuthorize
import framework.security.Role
import framework.security.extractKey
import framework.component.ComponentProvider
import mocker.dto.request.MockCreateRequestDto
import mocker.dto.request.MockUpdateRequestDto
import mocker.dto.response.MockResponseDto
import mocker.service.MockService

private val mockService = ComponentProvider.get(MockService::class.java)

@HttpAction(
    method = [HttpMethod.POST],
    path = "/mocks"
)
@RequireAuthorize(Role.USER)
class CreateMock : Action<MockCreateRequestDto, Unit> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: MockCreateRequestDto
    ): ActionResponse<Unit> {
        val userKey = extractKey(request.bearerToken)

        mockService.create(body, userKey.toLong())

        return ActionResponse.noContent()
    }
}

@HttpAction(
    method = [HttpMethod.GET],
    path = "/mocks"
)
@RequireAuthorize(Role.USER)
class ReadMocks : Action<Unit, List<MockResponseDto>> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: Unit
    ): ActionResponse<List<MockResponseDto>> {
        val userKey = extractKey(request.bearerToken).toLong()

        val response = mockService.read(userKey)

        return ActionResponse(
            body = response
        )
    }
}

@HttpAction(
    method = [HttpMethod.PUT],
    path = "/mocks/{mockKey}"
)
@RequireAuthorize(Role.USER)
class UpdateMock : Action<MockUpdateRequestDto, Unit> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: MockUpdateRequestDto
    ): ActionResponse<Unit> {
        val userKey = extractKey(request.bearerToken).toLong()
        val mockKey = pathVariables["mockKey"]!!.toLong()

        mockService.update(body, userKey, mockKey)

        return ActionResponse.noContent()
    }
}

@HttpAction(
    method = [HttpMethod.DELETE],
    path = "/mocks/{mockKey}"
)
@RequireAuthorize(Role.USER)
class DeleteMock : Action<Unit, Unit> {
    override fun act(
        request: Request,
        pathVariables: Map<String, String>,
        body: Unit
    ): ActionResponse<Unit> {
        val userKey = extractKey(request.bearerToken).toLong()
        val mockKey = pathVariables["mockKey"]!!.toLong()

        mockService.delete(userKey, mockKey)

        return ActionResponse.noContent()
    }
}
