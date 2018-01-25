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
package app

import app.util.BOT_INVITE_LINK
import app.util.DISCORD_SERVER_LINK
import app.util.GITHUB_ORG
import views.Index
import routing.Router
import routing.redirect
import routing.route
import views.Projects

/**
 * @author Kaidan Gustave
 */
fun main(args: Array<String>) {
    route("/")         { Index() }
    route("/projects") { Projects() }

    // Link /home back to /
    redirect("/home", "/", internal = true)

    redirect("/invite", BOT_INVITE_LINK)
    redirect("/support", DISCORD_SERVER_LINK)
    redirect("/github", "$GITHUB_ORG/NightFury")

    Router.launch()
}