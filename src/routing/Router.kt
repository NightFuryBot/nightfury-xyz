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

import app.util.Logger
import app.util.production
import kotlin.browser.document
import kotlin.js.Json

/**
 * @author Kaidan Gustave
 */
object Router : Json {
    // All routes (redirects included) mapped to their respective paths
    private val routes = HashMap<String, Route>()

    /** The base path extension, used for development build testing. */
    val EXT = if(production) "" else "/nightfury-xyz/build"

    /** The error path extension. Does not include the base path [extension][EXT]. */
    const val ERROR_EXT = "/error"

    /** A logger for internal usage. */
    val LOGGER = Logger.getLogger(Router::class.js)

    override fun get(propertyName: String): Route {
        return routes[propertyName] ?: httpError(404)
    }

    override fun set(propertyName: String, value: Any?) {
        if(value !is Route)
            return // Nothing to worry about

        routes[propertyName] = value
    }

    fun launch() {
        val location = document.location ?: return LOGGER.error("Could not find page location!\n" +
                                                                "Document URL: ${document.URL}")

        val pathname = location.pathname

        LOGGER.info("Viewing '$pathname'")
        try {
            this[pathname].direct()
        } catch(e: HTTPErrorException) {
            val httpRoutes = routes.values.mapNotNull { it as? ErrorRoute }

            // First we try to grab the HTTPError route for the exception we just caught.
            // There is always a chance we haven't defined this error response yet, so in that
            // case we will show 500.
            // TODO At a certain point, I would like to make this a wildcard error on it's own.
            val httpRoute = httpRoutes.firstOrNull { it.code == e.error.code }
                            ?: httpRoutes.first { it.code == 500 }

            httpRoute.direct(e)
        }
    }
}