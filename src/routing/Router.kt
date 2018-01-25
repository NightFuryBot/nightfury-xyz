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

/**
 * @author Kaidan Gustave
 */
object Router {
    private val routes = HashSet<Route>()
    internal val ext: String = if(production) "" else "/nightfury-xyz/build"

    val LOGGER: Logger = Logger.getLogger(Router::class.js)

    operator fun plusAssign(route: Route) {
        routes += route
    }

    operator fun minusAssign(route: Route) {
        routes -= route
    }

    fun launch() {
        val location = document.location

        LOGGER.info("Viewing '${location?.pathname}'")
        routes.firstOrNull { it.from == location?.pathname }?.direct()
    }
}