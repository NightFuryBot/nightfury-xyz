/*
 * Copyright 2018 Kaidan Gustave
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("MemberVisibilityCanBePrivate")
package routing

import app.util.niceName
import react.RProps
import kotlin.js.Json

/**
 * The documentation of each status code was copied from
 * [restapitutorial.com](http://www.restapitutorial.com/httpstatuscodes.html).
 *
 * All credits to them.
 *
 * @author Kaidan Gustave
 */
enum class HTTPError(val code: Int, meaning: String? = null) {

    /**
     * #### 400 - Bad Request ####
     *
     * The request could not be understood by the server due to
     * malformed syntax. The client SHOULD NOT repeat the request
     * without modifications.
     *
     * @exception HTTPErrorException 400 - Bad Request
     */
    BAD_REQUEST(400),

    /**
     * #### 401 - Unauthorized ####
     *
     * The request requires user authentication. The response MUST
     * include a WWW-Authenticate header field (section 14.47) containing
     * a challenge applicable to the requested resource. The client MAY
     * repeat the request with a suitable Authorization header field
     * (section 14.8). If the request already included Authorization
     * credentials, then the 401 response indicates that authorization has
     * been refused for those credentials. If the 401 response contains the
     * same challenge as the prior response, and the user agent has already
     * attempted authentication at least once, then the user SHOULD be
     * presented the entity that was given in the response, since that
     * entity might include relevant diagnostic information. HTTP access
     * authentication is explained in "HTTP Authentication: Basic and Digest
     * Access Authentication".
     *
     * @exception HTTPErrorException 401 - Unauthorized
     */
    UNAUTHORIZED(401),

    /**
     * #### 404 - Not Found ####
     *
     * The server has not found anything matching the Request-URI.
     * No indication is given of whether the condition is temporary
     * or permanent. The 410 (Gone) status code SHOULD be used if the
     * server knows, through some internally configurable mechanism,
     * that an old resource is permanently unavailable and has no
     * forwarding address. This status code is commonly used when the
     * server does not wish to reveal exactly why the request has been
     * refused, or when no other response is applicable.
     *
     * @exception HTTPErrorException 404 - Not Found
     */
    NOT_FOUND(404),

    /**
     * #### 500 - Internal Server Error ####
     *
     * The server encountered an unexpected condition which
     * prevented it from fulfilling the request.
     *
     * @exception HTTPErrorException 500 - Internal Server Error
     */
    INTERNAL_SERVER_ERROR(500);

    val meaning: String = meaning ?: niceName

    fun emit(message: String? = null): Nothing = throw HTTPErrorException(this, message = message)

    override fun toString(): String = "$code - $meaning"

    companion object {
        fun ofCode(code: Int): HTTPError = values().firstOrNull { it.code == code }
                                           ?: NOT_FOUND // This counts as a "not found"
    }
}

class HTTPErrorException(
    val error: HTTPError,
    val details: Json? = null,
    message: String? = null
): RuntimeException(), RProps {
    override val message = message ?: "$error"
}

fun httpError(code: Int, message: String? = null): Nothing = HTTPError.ofCode(code).emit(message)
