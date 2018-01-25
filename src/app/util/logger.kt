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
@file:Suppress("Unused", "ObjectPropertyName", "MemberVisibilityCanBePrivate")
package app.util

var defaultLogLevel: LogLevel = getInitialLogLevel()

private fun getInitialLogLevel(): LogLevel {
    val production = try { js("process.env.NODE_ENV === 'production'").unsafeCast<Boolean>() } catch(t: Throwable) {
        // If we fail in any way to get the state production
        Logger.GLOBAL.warn("Could not determine default logger level, will set to DEBUG for now.")
        return LogLevel.DEBUG
    }

    if(production) {
        // We are in production
        return LogLevel.INFO
    }

    // We are in dev
    return LogLevel.DEBUG
}

interface Logger {
    companion object {
        val GLOBAL: Logger = getLogger("Global", LogLevel.DEBUG) // Debugging tool

        fun getLogger(name: String, level: LogLevel = defaultLogLevel): Logger = NamedLogger(name, level)
        fun getLogger(clazz: JsClass<*>, level: LogLevel = defaultLogLevel): Logger = NamedLogger(clazz.name, level)
    }

    fun log(level: LogLevel, msg: String)

    fun trace(msg: String) = log(LogLevel.TRACE, msg)
    fun debug(msg: String) = log(LogLevel.DEBUG, msg)
    fun info(msg: String) = log(LogLevel.INFO, msg)
    fun warn(msg: String) = log(LogLevel.WARN, msg)
    fun error(msg: String) = log(LogLevel.ERROR, msg)
}

@Suppress("MemberVisibilityCanBePrivate")
enum class LogLevel(val highlight: (String) -> String) {
    TRACE({ it }),
    DEBUG(::yellow),
    INFO(::green),
    WARN(::red),
    ERROR({ bold(red(it)) });

    infix fun doesNotAccept(other: LogLevel): Boolean = !accepts(other)
    infix fun accepts(other: LogLevel): Boolean = ordinal <= other.ordinal

    fun setAsGlobalLevel() {
        defaultLogLevel = this
    }
}

class NamedLogger(private val name: String, private val level: LogLevel): Logger {
    override fun log(level: LogLevel, msg: String) {
        if(this.level doesNotAccept level)
            return

        val toLog = "[${level.highlight(name)}] [${level.highlight("$level")}]: ${level.highlight(msg)}"

        console.log(toLog)
    }
}
