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
import kotlinext.js.invoke
import kotlinext.js.require
import react.*
import react.dom.div
import react.dom.h1
import react.dom.p
import views.components.NavigationBar
import kotlin.browser.document

@Suppress("Unused")
val indexCss: dynamic = require("styles/index.css")

class Index : RComponent<RProps, RState>() {
    private val navBar = NavigationBar()

    override fun RState.init() {
        document.title = "NightFury"
    }

    override fun RBuilder.render() {
        navBar.renderInto(this)
        div(classes = "center-div") {
            h1(classes = "center-div-header") { + "NightFury" }
            p(classes = "center-div-paragraph")  { + "A Discord Bot For Your Server" }
        }
    }
}
