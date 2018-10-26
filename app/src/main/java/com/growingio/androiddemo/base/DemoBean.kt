package com.growingio.androiddemo.base

import com.liz.lib_demodesc.DemoBeanTag

/**
 * classDesc: DemoBean , 用它来分组 demo
 */

@DemoBeanTag
class DemoBean(val demoDesc: String, val demoGroup: String, val isMethod: Boolean, val className: Class<Any>, val task: Task) {
    interface Task {
        @DemoBeanTag
        fun execute()
    }
}