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
package views

import views.components.NavigationBar
import kotlinext.js.invoke
import kotlinext.js.require
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

@Suppress("Unused")
val projectsCss: dynamic = require("styles/projects.css")

class Projects : RComponent<RProps, RState>() {
    private val navBar: NavigationBar = NavigationBar()

    override fun RBuilder.render() {
        with(navBar) { this@render.render() }
        // TODO Projects Page
    }
}