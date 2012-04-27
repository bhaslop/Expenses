package com.framework.utils

import org.codehaus.groovy.grails.commons.ApplicationHolder

class BeanUtils {
	static getBean(String beanName) {
		ApplicationHolder.application.mainContext.getBean(beanName)
	}
}
