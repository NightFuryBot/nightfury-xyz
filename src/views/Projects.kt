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

import app.util.renderInto
import views.components.NavigationBar
import kotlinext.js.invoke
import kotlinext.js.require
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1
import react.dom.p
import kotlin.browser.document
import kotlin.js.Json
import kotlin.js.json

@Suppress("Unused")
val projectsCss: dynamic = require("styles/projects.css")

const val projectDiv = "project-div"
const val projectHeader = "project-header"
const val projectDescriptionDiv = "project-description-div"
const val projectDescriptionParagraph = "project-description-paragraph"

class Projects : RComponent<RProps, RState>() {
    private val navBar: NavigationBar = NavigationBar()

    private val projects: Array<Json> = arrayOf(
        json(
            "name" to "NightFury",
            "description" to "A Discord Bot for Your Server!"
        )
    )

    override fun RState.init() {
        document.title = "Projects"
    }

    override fun RBuilder.render() {
        navBar.renderInto(this)
        projects.forEach {
            val name = it["name"] as String
            val description = it["description"] as String
            div(classes = projectDiv) {
                h1(classes = projectHeader) {
                    + name
                }
                div(classes = projectDescriptionDiv) {
                    p(classes = projectDescriptionParagraph) {
                        + description
                    }
                }
            }
        }
    }
}