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
    private val routes = HashMap<String, Route>()
    val ext: String = if(production) "" else "/nightfury-xyz/build"

    val LOGGER = Logger.getLogger(Router::class.js)

    override fun get(propertyName: String): Route {
        return routes[propertyName] ?: httpError(404)
    }

    override fun set(propertyName: String, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun launch() {
        val location = document.location ?:
                       return LOGGER.error("Could not find page location!\n" +
                                           "Document URL: ${document.URL}")

        LOGGER.info("Viewing '${location.pathname}'")
        try {
            this[location.pathname].direct()
        } catch(e: HTTPErrorException) {

        }
    }
}