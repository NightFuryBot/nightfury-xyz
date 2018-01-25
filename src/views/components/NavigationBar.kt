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
package views.components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import react.dom.li
import react.dom.ul
import kotlin.browser.document

const val navBarClass  = "nav-bar-ul"
const val navTabClass  = "nav-bar-li"
const val navLinkClass = "nav-bar-a"

fun RBuilder.navTab(label: String, href: String?) {
    li(classes = navTabClass) {
        a(href = href, classes = navLinkClass) { + label }
    }
}

class NavigationBar : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        ul(classes = navBarClass) {
            navTab("Home", document.origin)
            navTab("Docs", null)
            navTab("Invite", "${document.origin}/invite")
            navTab("Support", "${document.origin}/support")
            navTab("GitHub", "${document.origin}/github")
        }
    }
}