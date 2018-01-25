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
package app.util

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

val production: Boolean by lazy {
    try {
        js("process.env.NODE_ENV === 'production'").unsafeCast<Boolean>()
    } catch(t: Throwable) {
        Logger.GLOBAL.warn("Could not determine production state of application!")
        false // We should never be in production if something like this is failing anyways
    }
}

inline fun <P: RProps, S: RState, reified C: RComponent<P, S>>
    C.renderInto(builder: RBuilder) = with(builder) { this.render() }

inline val <reified E: Enum<E>> E.niceName: String
    inline get() = name.split('_').joinToString(" ") { it.toLowerCase().capitalize() }
