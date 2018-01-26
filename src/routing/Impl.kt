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
package routing

import react.RComponent
import react.RProps
import react.RState
import views.errors.ErrorView
import kotlin.browser.document

const val DEFAULT_ELEMENT = "root"

// Redirect route, pushes requests from one URI to another
class RedirectRoute(override val from: String, private val to: String): Route {
    override fun direct() {
        // Redirected via Location#assign
        document.location!!.assign(to)
    }
}


// We save the code so we can use it to find the correct component to generate later.
class ErrorRoute(val code: Int, from: String, override val element: String):
    ComponentProvidedContextRoute<HTTPErrorException, ErrorView>(from) {

    override val generate: (HTTPErrorException) -> ErrorView = { ErrorView(it) }
    override lateinit var context: HTTPErrorException
}


fun route(from: String, element: String = DEFAULT_ELEMENT, generate: () -> RComponent<RProps, RState>) {
    val path = Router.EXT + from
    Router[path] = object : AbstractComponentRoute(path) {
        override val generate = generate
        override val element = element
    }
}

fun <C: Any> contextRoute(from: String, element: String = DEFAULT_ELEMENT,
                          contextGen: () -> C,
                          componentGen: (C) -> RComponent<RProps, RState>) {
    val path = Router.EXT + from

    Router[path] = object : AbstractComponentContextRoute<C>(path) {
        override lateinit var context: C
        override val element = element
        override val contextGen = contextGen
        override val generate = componentGen
    }
}

fun error(code: Int, element: String = DEFAULT_ELEMENT) {
    val path = "${Router.EXT}${Router.ERROR_EXT}/$code"
    Router[path] = ErrorRoute(code, path, element)
}

fun redirect(from: String, to: String, internal: Boolean = false) {
    val path = "${Router.EXT}$from"
    Router[path] = RedirectRoute(path, if(internal) "${document.origin}$to" else to)
}