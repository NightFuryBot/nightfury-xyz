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

open class CallContext {
    companion object {
        fun of(block: HashMap<String, Any?>.() -> Unit): CallContext = CallContext().also { it.map.block() }
    }

    private val map = HashMap<String, Any?>()

    fun valueOf(key: String): Any? {
        return map[key]
    }

    inline operator fun <reified T> get(key: String): T? {
        return valueOf(key) as? T
    }

    override fun hashCode(): Int = map.size.hashCode()
    override fun equals(other: Any?): Boolean {
        if(other !is CallContext)
            return false
        if(other.map.size != this.map.size)
            return false
        map.keys.forEach {
            if(valueOf(it) != other[it])
                return false
        }

        return true
    }
}