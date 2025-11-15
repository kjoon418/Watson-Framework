# Watson Framework
간단한 프레임워크를 직접 구현한다.
직접 구현한 프레임워크를 통해 간단한 HTTP 서버 애플리케이션을 구현한다.

## 목표
- Kotlin의 숙련도를 높인다.
- Framework에 의존하지 않고 간단한 웹 서버를 구현해본다.
  - 기존 프레임워크의 구현을 그대로 따라가기보단, 본인이 생각한 구현안으로 해결하려 시도한다.
- 아직 미숙할지라도 여러 기술을 사용해보고, 앞으로 학습해야 할 기술의 필요성을 체감한다.
- 복잡한 시스템을 구현하면서도 SOLID 원칙을 준수해보는 경험을 한다.

## 기능 요구사항
### 핵심 기능
- [x] 서버를 이용해 클라이언트와 HTTP를 기반으로 통신한다.
   - [x] 계층 아키텍처를 통해, 핵심 비즈니스 로직과 통신 로직을 분리한다.
- [x] DI를 구현한다.
- [x] DB 접근을 추상화한다.
- [x] 예외가 발생해도 애플리케이션이 종료되지 않도록 하고, 예외를 전역 처리할 수 있게 한다.
- [x] watson.yml 파일을 이용해 애플리케이션에 환경변수를 주입한다.
- [x] 프레임워크를 기반으로 모킹 애플리케이션을 구현한다.

### 추가 기능
- [x] JWT 기반의 인증/인가를 구현한다.
- [x] 비밀번호 해싱을 구현한다.
- [ ] 라이브러리에 의존한 기능을 직접 구현한다.

## 프레임워크 아키텍처
### Action
- `Action`
  - 사용자의 요청을 처리하는 비즈니스 로직의 진입점 역할을 한다.
  - 요청 페이로드의 타입과 응답 페이로드의 타입을 선언한다.
- `ActionResponse`
  - 요청에 대한 응답의 상태코드와 페이로드를 지닌다.
- `HttpActionScanner`
  - `HttpAction` 애노태이션이 붙은 `Action`을 탐색해 라우팅에 관한 정보와 함께 반환한다.
- `HttpAction`
  - `Action`이 `HttpActionScanner`의 스캔 대상이 되도록 만든다.
  - 라우팅에 관한 정보(HTTP Method, Path)를 선언한다.
- `Actions`
  - 라우트 정보와 함께 `Action`을 저장하는 일급 컬렉션이다.
  - `Action`의 라우트 정보가 중복되지 않음을 보장한다.

### Router
- `PathTemplate`
  - Path variable을 포함할 수 있는 Path를 지닌다.
  - 실제 요청 Path가 원본 Path와 일치하는지를 판단한다.
- `RouteKey`
  - Method와 Path를 지닌다.
- `DefaultRouter`
  - 클라이언트 요청의 Method와 Path를 기반으로 적절한 `Action`을 찾아 반환한다.
  - 
- `PathVariableExtractor`
  - 원본 경로를 기반으로, 실제 요청 경로에 담긴 경로 변수를 추출한다.

### Component
- `ComponentScanner`
  - 컴포넌트를 스캔하여 인스턴스를 생성하고 `ComponentInstances`에 저장한다.
- `ComponentInstances`
  - 컴포넌트의 클래스 정보와 그 인스턴스 정보를 관리한다.
- `ComponentProvider`
  - 클래스 정보를 기반으로 컴포넌트 인스턴스를 찾아 반환한다.
  - 컴포넌트에 대한 DI를 제공한다.
- `Component`
  - 클래스가 `ComponentScanner`의 스캔 대상이 되도록 만든다.

### Config(Configuration)
- `FrameworkConfig`
  - 프레임워크를 구성하는 객체의 의존성을 적절히 주입하여 인스턴스를 생성한다.
- `PropertyLoader`
  - yml 파일로부터 속성 값을 읽는다.
- `ConfigProperty`
  - 프레임워크에서 사용하는 yml 속성 값의 경로와 기본값을 정의한다.

### Dispatcher
- `Dispatcher`
  - 요청을 처리해 응답을 반환하는 흐름을 결정한다.

### Request
- `Request`
  - 요청에 대한 값을 담는다.
- `RequestBody`
  - 요청의 페이로드에 해당하는 값을 지닌다.
  - 페이로드 값과 Content-Type을 지닌다.
- `RequestContext`
  - 요청의 헤더, 목적, 경로, 쿼리 파라미터를 지닌다.
- `HttpRequestMapper`
  - `HttpServletRequest`를 `Request`로 변환한다.
- `JsonRequestBodyMapper`
  - `contentType`이 `application/json`인 `RequestBody.body`를 적절한 타입의 객체로 변환한다.

### Response
- `Response`
  - 응답에 대한 값을 담는다.
- `JsonResponseMapper`
  - `ActionResponse`의 `body`를 적절한 JSON 문자열로 변환한다.
- `HttpResponseWriter`
  - `Response`의 값을 통해 HTTP 응답을 작성한다.

### Repository
- `Entity`
  - `Repository`에 관리될 엔티티를 의미한다.
  - 유일해야 하는 `key` 값을 지닌다.
- `MemoryRepository`
  - `Repository`의 구현체다.
  - `Entity`를 메모리에서 관리한다.
- `RepositoryBuilders`
  - `Repository`의 제네릭 타입과 구현체 생성 함수를 저장한다.
- `RepositoryScanner`
  - 프로젝트에 존재하는 `Repository`의 하위 인터페이스를 스캔한다.
  - 주입받은 `Repository` 구현 정책에 따라 `RepositoryBuilders`에 구현체 생성 함수를 등록한다.
- `RepositoryProvider`
  - `Repository`에 대한 DI를 제공한다.

### Security
- `Role`
  - 클라이언트의 권한을 의미한다.
- `JwtSecurity`
  - JWT 기반의 인증/인가 기능을 제공한다.
- `RequireAuthorize`
  - `Action`이 요구하는 사용자 권한을 정의한다.
- `AuthorizationValidator`
  - `RequireAuthorize` 애노태이션이 붙은 `Action`에 한해 JWT의 권한을 검증한다.
- `PasswordEncoder`
  - 비밀번호 해싱 기능을 제공한다.

### Server
- `HttpServer`, `Servlet`
  - 클라이언트와의 HTTP 통신을 담당한다.

### FrameworkStarter
- `FrameworkStarter`
  - 프레임워크 동작의 시작점을 담당한다.

### Exception
- `ExceptionResponseHandler`
  - 예외를 기반으로 `Response`를 생성한다.

