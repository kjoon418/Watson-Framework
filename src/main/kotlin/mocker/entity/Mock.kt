package mocker.entity

import framework.constants.HttpMethod
import framework.repository.Entity

class Mock(
    key: Long,
    val method: HttpMethod,
    val endPoint: String,
    var data: String
) : Entity<Long>(key)
