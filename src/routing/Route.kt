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

import app.annotations.LateinitFriendly
import react.RComponent
import react.RProps
import react.RState
import react.dom.render
import kotlin.browser.document

interface Route {
    val from: String
    fun direct()
}

interface ContextRoute<out C> : Route {
    val contextGen: () -> C
    val context: C
}

abstract class AbstractRoute(override val from: String): Route {
    override fun toString(): String = from
    override fun hashCode(): Int = from.hashCode()
    override fun equals(other: Any?): Boolean {
        if(other !is AbstractRoute)
            return false

        return from == other.from
    }
}

abstract class AbstractComponentRoute(override val from: String): Route {
    protected abstract val generate: () -> RComponent<RProps, RState>
    protected abstract val element: String

    override fun direct() {
        render(generate().render(), document.getElementById(element))
    }
}

abstract class AbstractComponentContextRoute<C>(override val from: String): Route, ContextRoute<C> {
    protected abstract val generate: (C) -> RComponent<RProps, RState>
    protected abstract val element: String

    @LateinitFriendly
    abstract override var context: C

    override fun direct() {
        context = contextGen()
        render(generate(context).render(), document.getElementById(element))
    }
}

abstract class ComponentProvidedContextRoute<C, out Comp: RComponent<*,*>>(override val from: String): Route {
    protected abstract val generate: (C) -> Comp
    protected abstract val element: String

    @LateinitFriendly
    abstract var context: C

    fun direct(context: C) {
        this.context = context
        direct()
    }

    override fun direct() {
        // Context should be provided beforehand.
        // This is for calls that depend on internal systems, and should be used
        // SPARINGLY to prevent consistency, backwards compatibility, and less
        // hardcoding.
        render(generate(context).render(), document.getElementById(element))
    }
}
