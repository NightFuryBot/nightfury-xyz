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
import react.dom.render
import kotlin.browser.document

/**
 * @author Kaidan Gustave
 */
interface Route {
    val from: String
    fun direct()
}

// Simple class for rendering a react component when
// a route is requested.
class ComponentRoute(
    override val from: String,
    // We store the component as a function so we don't
    // waste memory on components we won't render.
    private val component: () -> RComponent<RProps, RState>,
    private val target: String
): Route {
    override fun direct() {
        // Render the component to the target element and boom, done.
        render(component().render(), document.getElementById(target))
    }
}

// Redirect route, pushes requests from one URI to another
class RedirectRoute(override val from: String, private val to: String): Route {
    override fun direct() {
        // Redirected via Location#assign
        document.location!!.assign(to)
    }
}

fun redirect(from: String, to: String, internal: Boolean = false) {
    // Register to Router
    Router += RedirectRoute(from, if(internal) "${document.origin}$to" else to)
}

fun route(from: String, target: String = "root", component: () -> RComponent<RProps, RState>) {
    // Register to Router
    Router += ComponentRoute("${Router.ext}$from", component, target)
}